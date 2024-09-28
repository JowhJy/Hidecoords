package com.jowhjy.hidecoords.util;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.WorldBorderObfuscator;
import com.jowhjy.hidecoords.mixin.ChunkDeltaUpdateS2CPacketAccessor;
import com.jowhjy.hidecoords.mixin.LightUpdateS2CPacketAccessor;
import com.mojang.datafixers.util.Pair;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import javax.xml.crypto.Data;
import java.util.*;

public class S2CPacketOffsetter {



    public static <T extends PacketListener> Packet<?> offsetPacket(Packet<T> packet, Offset offset, World world) {

        PacketType<? extends Packet<T>> packetType = packet.getPacketId();

        if (packetType.equals(PlayPackets.BUNDLE)) {
            BundleS2CPacket bundleS2CPacket = (BundleS2CPacket) packet;
            ArrayList<Packet<? super ClientPlayPacketListener>> newSubpackets = new ArrayList<>();
            for (Packet<? super ClientPlayPacketListener> subpacket : bundleS2CPacket.getPackets()) {
                newSubpackets.add((Packet<? super ClientPlayPacketListener>) offsetPacket(subpacket, offset, world));
            }
            return new BundleS2CPacket(newSubpackets);
        }
        if (packetType.equals(PlayPackets.BLOCK_UPDATE)) {
            BlockUpdateS2CPacket typedPacket = (BlockUpdateS2CPacket) packet;
            return new BlockUpdateS2CPacket(offset(typedPacket.getPos(), offset), typedPacket.getState());
        }
        if (packetType.equals(PlayPackets.BLOCK_EVENT)) {
            BlockEventS2CPacket typedPacket = (BlockEventS2CPacket) packet;
            return new BlockEventS2CPacket(offset(typedPacket.getPos(), offset), typedPacket.getBlock(), typedPacket.getType(), typedPacket.getData());
        }
        if (packetType.equals(PlayPackets.BLOCK_DESTRUCTION)) {
            BlockBreakingProgressS2CPacket typedPacket = (BlockBreakingProgressS2CPacket) packet;
            return new BlockBreakingProgressS2CPacket(typedPacket.getEntityId(), offset(typedPacket.getPos(), offset), typedPacket.getProgress());
        }
        if (packetType.equals(PlayPackets.ADD_ENTITY)) {
            EntitySpawnS2CPacket typedPacket = (EntitySpawnS2CPacket) packet;
            double x = offsetX(typedPacket.getX(), offset);
            double y = typedPacket.getY();
            double z = offsetZ(typedPacket.getZ(), offset);
            Vec3d velocity = new Vec3d(typedPacket.getVelocityX(), typedPacket.getVelocityY(), typedPacket.getVelocityZ());
            return new EntitySpawnS2CPacket(typedPacket.getEntityId(), typedPacket.getUuid(), x, y, z, typedPacket.getPitch(), typedPacket.getYaw(), typedPacket.getEntityType(), typedPacket.getEntityData(), velocity, typedPacket.getHeadYaw());
        }
        if (packetType.equals(PlayPackets.ADD_EXPERIENCE_ORB)) {
            ExperienceOrbSpawnS2CPacket typedPacket = (ExperienceOrbSpawnS2CPacket) packet;
            double x = offsetX(typedPacket.getX(), offset);
            double y = typedPacket.getY();
            double z = offsetZ(typedPacket.getZ(), offset);
            ExperienceOrbEntity fakeEntity = new ExperienceOrbEntity(world, x, y, z, typedPacket.getExperience());
            fakeEntity.setId(typedPacket.getEntityId());
            EntityTrackerEntry fakeEntityTrackerEntry = new EntityTrackerEntry((ServerWorld) world, fakeEntity, 0, false, null);
            return new ExperienceOrbSpawnS2CPacket(fakeEntity, fakeEntityTrackerEntry);
        }
        if (packetType.equals(PlayPackets.PLAYER_POSITION)) {
            PlayerPositionLookS2CPacket typedPacket = (PlayerPositionLookS2CPacket) packet;
            return new PlayerPositionLookS2CPacket(offsetX(typedPacket.getX(), offset), typedPacket.getY(), offsetZ(typedPacket.getZ(), offset), typedPacket.getYaw(), typedPacket.getPitch(), typedPacket.getFlags(), typedPacket.getTeleportId());
        }
        if (packetType.equals(PlayPackets.PLAYER_LOOK_AT)) {
            LookAtS2CPacket typedPacket = (LookAtS2CPacket) packet;
            double x = offsetX(typedPacket.getTargetPosition(world).getX(), offset);
            double y = typedPacket.getTargetPosition(world).getY();
            double z = offsetZ(typedPacket.getTargetPosition(world).getZ(), offset);
            return new LookAtS2CPacket(typedPacket.getSelfAnchor(), x, y, z);
        }
        if (packetType.equals(PlayPackets.CHUNKS_BIOMES)) {
            ChunkBiomeDataS2CPacket typedPacket = (ChunkBiomeDataS2CPacket) packet;
            List<ChunkBiomeDataS2CPacket.Serialized> data = typedPacket.chunkBiomeData();
            ArrayList<ChunkBiomeDataS2CPacket.Serialized> newData = new ArrayList<>();
            for (ChunkBiomeDataS2CPacket.Serialized serialized : data) {
                newData.add(new ChunkBiomeDataS2CPacket.Serialized(offset(serialized.pos(), offset), serialized.buffer()));
            }
            return new ChunkBiomeDataS2CPacket(newData);
        }
        if (packetType.equals(PlayPackets.EXPLODE)) {
            ExplosionS2CPacket typedPacket = (ExplosionS2CPacket) packet;
            Vec3d playerVelocity = new Vec3d(typedPacket.getPlayerVelocityX(), typedPacket.getPlayerVelocityY(), typedPacket.getPlayerVelocityZ());
            return new ExplosionS2CPacket(offsetX(typedPacket.getX(), offset), typedPacket.getY(), offsetZ(typedPacket.getZ(), offset), typedPacket.getRadius(), typedPacket.getAffectedBlocks(), playerVelocity, typedPacket.getDestructionType(), typedPacket.getParticle(), typedPacket.getEmitterParticle(), typedPacket.getSoundEvent());
        }
        if (packetType.equals(PlayPackets.LEVEL_CHUNK_WITH_LIGHT)) {
            ChunkDataS2CPacket typedPacket = (ChunkDataS2CPacket) packet;

            ChunkPos chunkPos = new ChunkPos(typedPacket.getChunkX(), typedPacket.getChunkZ());
            ChunkPos newChunkPos = offset(chunkPos, offset);

            ((HasAccessiblePos)typedPacket).juhc$setChunkX(newChunkPos.x);
            ((HasAccessiblePos)typedPacket).juhc$setChunkZ(newChunkPos.z);

            return typedPacket;
        }
        if (packetType.equals(PlayPackets.OPEN_SIGN_EDITOR)) {
            SignEditorOpenS2CPacket typedPacket = (SignEditorOpenS2CPacket) packet;
            return new SignEditorOpenS2CPacket(offset(typedPacket.getPos(), offset), typedPacket.isFront());
        }
        if (packetType.equals(PlayPackets.SECTION_BLOCKS_UPDATE)) {
            ChunkDeltaUpdateS2CPacket typedPacket = (ChunkDeltaUpdateS2CPacket) packet;
            ChunkSectionPos oldSectionPos = ((ChunkDeltaUpdateS2CPacketAccessor)typedPacket).getSectionPos();
            ChunkSectionPos newSectionPos = ChunkSectionPos.from(offset(oldSectionPos.toChunkPos(), offset), oldSectionPos.getSectionY());
            ((HasAccessiblePos)typedPacket).juhc$setChunkX(newSectionPos.getX());
            ((HasAccessiblePos)typedPacket).juhc$setChunkZ(newSectionPos.getZ());
            return typedPacket;
        }
        if (packetType.equals(PlayPackets.LEVEL_PARTICLES)) {
            ParticleS2CPacket typedPacket = (ParticleS2CPacket) packet;
            return new ParticleS2CPacket(typedPacket.getParameters(), typedPacket.isLongDistance(), offsetX(typedPacket.getX(), offset), typedPacket.getY(), offsetZ(typedPacket.getZ(), offset), typedPacket.getOffsetX(), typedPacket.getOffsetY(), typedPacket.getOffsetZ(), typedPacket.getSpeed(), typedPacket.getCount());
        }
        if (packetType.equals(PlayPackets.SOUND)) {
            PlaySoundS2CPacket typedPacket = (PlaySoundS2CPacket) packet;

            return new PlaySoundS2CPacket(typedPacket.getSound(),typedPacket.getCategory(), offsetX(typedPacket.getX(),offset),typedPacket.getY(),offsetZ(typedPacket.getZ(),offset), typedPacket.getVolume(),typedPacket.getPitch(),typedPacket.getSeed());
        }
        if (packetType.equals(PlayPackets.SOUND_ENTITY)) {
            PlaySoundFromEntityS2CPacket typedPacket = (PlaySoundFromEntityS2CPacket) packet;

            return new PlaySoundFromEntityS2CPacket(typedPacket.getSound(),typedPacket.getCategory(), Objects.requireNonNull(world.getEntityById(typedPacket.getEntityId())), typedPacket.getVolume(),typedPacket.getPitch(),typedPacket.getSeed());
        }
        if (packetType.equals(PlayPackets.MOVE_VEHICLE_S2C)) {
            VehicleMoveS2CPacket typedPacket = (VehicleMoveS2CPacket) packet;

            Entity dummyEntity = new ArmorStandEntity(world, offsetX(typedPacket.getX(),offset), typedPacket.getY(),offsetZ(typedPacket.getZ(),offset));
            dummyEntity.setAngles(typedPacket.getYaw(),typedPacket.getPitch());

            return new VehicleMoveS2CPacket(dummyEntity);
        }
        // todo we might wanna change this later!
        if (packetType.equals(PlayPackets.SET_DEFAULT_SPAWN_POSITION)) {
            PlayerSpawnPositionS2CPacket typedPacket = (PlayerSpawnPositionS2CPacket) packet;
            return new PlayerSpawnPositionS2CPacket(offset(typedPacket.getPos(), offset), typedPacket.getAngle());
        }
        if (packetType.equals(PlayPackets.FORGET_LEVEL_CHUNK)) {
            UnloadChunkS2CPacket typedPacket = (UnloadChunkS2CPacket) packet;
            return new UnloadChunkS2CPacket(offset(typedPacket.pos(),offset));
        }
        if (packetType.equals(PlayPackets.LIGHT_UPDATE)) {
            LightUpdateS2CPacket typedPacket = (LightUpdateS2CPacket) packet;
            ChunkPos newChunkPos = offset(new ChunkPos(typedPacket.getChunkX(), typedPacket.getChunkZ()),offset);
            ((HasAccessiblePos)typedPacket).juhc$setChunkX(newChunkPos.x);
            ((HasAccessiblePos)typedPacket).juhc$setChunkZ(newChunkPos.z);
            return typedPacket;
        }
        if (packetType.equals(PlayPackets.SET_CHUNK_CACHE_CENTER)) {
            ChunkRenderDistanceCenterS2CPacket typedPacket = (ChunkRenderDistanceCenterS2CPacket) packet;
            ChunkPos oldPos = new ChunkPos(typedPacket.getChunkX(), typedPacket.getChunkZ());
            ChunkPos newPos = offset(oldPos,offset);
            return new ChunkRenderDistanceCenterS2CPacket(newPos.x,newPos.z);
        }
        if (packetType.equals(PlayPackets.TELEPORT_ENTITY)) {
            EntityPositionS2CPacket typedPacket = (EntityPositionS2CPacket) packet;

            Entity dummyEntity = new ArmorStandEntity(world, offsetX(typedPacket.getX(),offset), typedPacket.getY(), offsetZ(typedPacket.getZ(),offset));
            dummyEntity.setOnGround(typedPacket.isOnGround());
            dummyEntity.setAngles(typedPacket.getYaw(), typedPacket.getPitch());
            dummyEntity.setId(typedPacket.getEntityId());

            return new EntityPositionS2CPacket(dummyEntity);
        }
        if (packetType.equals(PlayPackets.LEVEL_EVENT)) {
            WorldEventS2CPacket typedPacket = (WorldEventS2CPacket) packet;

            return new WorldEventS2CPacket(typedPacket.getEventId(),offset(typedPacket.getPos(),offset),typedPacket.getData(),typedPacket.isGlobal());
        }
        if (packetType.equals(PlayPackets.SET_EQUIPMENT)) {
            EntityEquipmentUpdateS2CPacket typedPacket = (EntityEquipmentUpdateS2CPacket) packet;
            List<Pair<EquipmentSlot, ItemStack>> equipmentList = typedPacket.getEquipmentList();
            ArrayList<Pair<EquipmentSlot, ItemStack>> newEquipmentList = new ArrayList<>();
            for (Pair<EquipmentSlot, ItemStack> pair : equipmentList)
                newEquipmentList.add(Pair.of(pair.getFirst(), offset(pair.getSecond(),offset)));

            return new EntityEquipmentUpdateS2CPacket(typedPacket.getEntityId(),newEquipmentList);
        }
        if (packetType.equals(PlayPackets.CONTAINER_SET_SLOT)) {
            ScreenHandlerSlotUpdateS2CPacket typedPacket = (ScreenHandlerSlotUpdateS2CPacket) packet;
            return new ScreenHandlerSlotUpdateS2CPacket(typedPacket.getSyncId(), typedPacket.getRevision(), typedPacket.getSlot(), offset(typedPacket.getStack(),offset));
        }
        if (packetType.equals(PlayPackets.CONTAINER_SET_CONTENT)) {
            InventoryS2CPacket typedPacket = (InventoryS2CPacket) packet;
            List<ItemStack> contents = typedPacket.getContents();
            DefaultedList<ItemStack> newContents = DefaultedList.ofSize(contents.size(), ItemStack.EMPTY);
            for (int i = 0; i < contents.size(); i++)
                newContents.set(i, offset(contents.get(i),offset));

            return new InventoryS2CPacket(typedPacket.getSyncId(), typedPacket.getRevision(), newContents, offset(typedPacket.getCursorStack(),offset));
        }
        if (packetType.equals((PlayPackets.SET_ENTITY_DATA))) {
            EntityTrackerUpdateS2CPacket typedPacket = (EntityTrackerUpdateS2CPacket) packet;
            ArrayList<DataTracker.SerializedEntry<?>> newTrackedValues = new ArrayList<>(typedPacket.trackedValues().size());
            if (typedPacket.trackedValues().isEmpty()) return packet;
            for (int i = 0; i < typedPacket.trackedValues().size(); i++)
            {
                DataTracker.SerializedEntry<?> entry = typedPacket.trackedValues().get(i);
                DataTracker.SerializedEntry<?> newEntry = new DataTracker.SerializedEntry<Object>(entry.id(), (TrackedDataHandler<Object>) entry.handler(), offsetData(entry.value(), offset));
                newTrackedValues.add(newEntry);
            }
            return new EntityTrackerUpdateS2CPacket(typedPacket.id(), newTrackedValues);

        }
        if (packetType.equals(PlayPackets.SET_ENTITY_DATA)) {
            EntityTrackerUpdateS2CPacket typedPacket = (EntityTrackerUpdateS2CPacket) packet;
            ArrayList<DataTracker.SerializedEntry<?>> newTrackedValues = new ArrayList<>(typedPacket.trackedValues().size());
            if (typedPacket.trackedValues().isEmpty()) return packet;
            for (int i = 0; i < typedPacket.trackedValues().size(); i++)
            {
                DataTracker.SerializedEntry<?> entry = typedPacket.trackedValues().get(i);
                DataTracker.SerializedEntry<?> newEntry = new DataTracker.SerializedEntry<Object>(entry.id(), (TrackedDataHandler<Object>) entry.handler(), offsetData(entry.value(), offset));
                newTrackedValues.add(newEntry);
            }
            return new EntityTrackerUpdateS2CPacket(typedPacket.id(), newTrackedValues);

        }





        return packet;
    }

    private static Object offsetData(Object value, Offset offset)
    {
        if (value instanceof ItemStack itemStack) {
            return(offset(itemStack,offset));
        }
        if (value instanceof BlockPos blockPos) {
            return(offset(blockPos,offset));
        }
        if (value instanceof Optional<?> optional) {
            if (optional.isPresent()) return Optional.of(offsetData(optional.get(), offset));
        }
        return value;
    }

    private static Vec3d offset(Vec3d vec3d, Offset offset) {
        return new Vec3d(vec3d.getX() + offset.getX(), vec3d.y, vec3d.getZ() + offset.getZ());
    }

    public static BlockPos offset(BlockPos blockPos, Offset offset)
    {
        return new BlockPos(blockPos.getX() + offset.getX(), blockPos.getY(), blockPos.getZ() + offset.getZ());
    }

    public static double offsetX(double x, Offset offset)
    {
        return x + offset.getX();
    }
    public static double offsetZ(double z, Offset offset)
    {
        return z + offset.getZ();
    }
    public static double offsetX8(double x8, Offset offset)
    {
        return x8 + offset.getX() * 8;
    }
    public static double offsetZ8(double z8, Offset offset)
    {
        return z8 + offset.getZ() * 8;
    }
    public static ChunkPos offset(ChunkPos chunkPos, Offset offset)
    {
        return new ChunkPos(chunkPos.x + offset.getChunkX(), chunkPos.z + offset.getChunkZ());
    }

    public static ItemStack offset(ItemStack itemStack, Offset offset) {
        if (itemStack == null) return null;

        if (itemStack.isOf(Items.COMPASS)) {
            LodestoneTrackerComponent lodestoneComponent = itemStack.getComponents().get(DataComponentTypes.LODESTONE_TRACKER);
            if (lodestoneComponent == null || lodestoneComponent.target().isEmpty()) return itemStack;
            LodestoneTrackerComponent newLodestoneComponent = new LodestoneTrackerComponent(Optional.of(offset(lodestoneComponent.target().get(), offset)), lodestoneComponent.tracked());
            itemStack.set(DataComponentTypes.LODESTONE_TRACKER, newLodestoneComponent);
        }
        return itemStack;
    }


    private static GlobalPos offset(GlobalPos globalPos, Offset offset) {
        return new GlobalPos(globalPos.dimension(), offset(globalPos.pos(),offset));
    }
}
