package com.jowhjy.hidecoords;

import com.jowhjy.hidecoords.mixin.ChunkDeltaUpdateS2CPacketAccessor;
import com.jowhjy.hidecoords.util.HasAccessibleBlockPos;
import com.jowhjy.hidecoords.util.HasAccessibleChunkPos;
import com.jowhjy.hidecoords.util.IChunkDeltaUpdateS2CPacketMixin;
import com.jowhjy.hidecoords.util.OffsetableTrackedWaypoint;
import com.mojang.datafixers.util.Pair;
import eu.pb4.polymer.core.impl.interfaces.EntityAttachedPacket;
import eu.pb4.polymer.core.impl.interfaces.PossiblyInitialPacket;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.SectionPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.PacketListener;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundChunksBiomesPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ClientboundDebugBlockValuePacket;
import net.minecraft.network.protocol.game.ClientboundDebugChunkValuePacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundForgetLevelChunkPacket;
import net.minecraft.network.protocol.game.ClientboundGameTestHighlightPosPacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerLookAtPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetCursorItemPacket;
import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ClientboundTrackedWaypointPacket;
import net.minecraft.network.protocol.game.CommonPlayerSpawnInfo;
import net.minecraft.network.protocol.game.GamePacketTypes;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.math.*;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.waypoints.TrackedWaypoint;
import java.util.*;

public class S2CPacketOffsetter {



    public static <T extends PacketListener> Packet<?> offsetPacket(Packet<T> packet, Offset offset, Level world) {

        var newPacket = offsetPacketByType(packet, offset, world);
        if (FabricLoader.getInstance().isModLoaded("polymer-core")) {
            if (packet instanceof EntityAttachedPacket eap) {
                ((EntityAttachedPacket) newPacket).polymer$setEntity(eap.polymer$getEntity());
            }
            if (packet instanceof PossiblyInitialPacket pip && pip.polymer$getInitial())
            {
                ((PossiblyInitialPacket)newPacket).polymer$setInitial();
            }
        }
        return newPacket;
    }

    public static <T extends PacketListener> Packet<?> offsetPacketByType(Packet<T> packet, Offset offset, Level world) {

        PacketType<? extends Packet<T>> packetType = packet.type();
        //System.out.println(packetType);

        if (packetType.equals(GamePacketTypes.CLIENTBOUND_BUNDLE)) {
            ClientboundBundlePacket bundleS2CPacket = (ClientboundBundlePacket) packet;
            ArrayList<Packet<? super ClientGamePacketListener>> newSubpackets = new ArrayList<>();
            for (Packet<? super ClientGamePacketListener> subpacket : bundleS2CPacket.subPackets()) {
                newSubpackets.add((Packet<? super ClientGamePacketListener>) offsetPacket(subpacket, offset, world));
            }
            return new ClientboundBundlePacket(newSubpackets);
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_BLOCK_UPDATE)) {
            ClientboundBlockUpdatePacket typedPacket = (ClientboundBlockUpdatePacket) packet;
            return new ClientboundBlockUpdatePacket(offset(typedPacket.getPos(), offset), typedPacket.getBlockState());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_BLOCK_EVENT)) {
            ClientboundBlockEventPacket typedPacket = (ClientboundBlockEventPacket) packet;
            return new ClientboundBlockEventPacket(offset(typedPacket.getPos(), offset), typedPacket.getBlock(), typedPacket.getB0(), typedPacket.getB1());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_BLOCK_ENTITY_DATA)) {
            ClientboundBlockEntityDataPacket typedPacket = (ClientboundBlockEntityDataPacket) packet;
            ((HasAccessibleBlockPos)typedPacket).hidecoords$setBlockPos(offset(typedPacket.getPos(),offset));
            return typedPacket;
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_BLOCK_DESTRUCTION)) {
            ClientboundBlockDestructionPacket typedPacket = (ClientboundBlockDestructionPacket) packet;
            return new ClientboundBlockDestructionPacket(typedPacket.getId(), offset(typedPacket.getPos(), offset), typedPacket.getProgress());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_ADD_ENTITY)) {
            ClientboundAddEntityPacket typedPacket = (ClientboundAddEntityPacket) packet;
            double x = offsetX(typedPacket.getX(), offset);
            double y = typedPacket.getY();
            double z = offsetZ(typedPacket.getZ(), offset);
            return new ClientboundAddEntityPacket(typedPacket.getId(), typedPacket.getUUID(), x, y, z, typedPacket.getXRot(), typedPacket.getYRot(), typedPacket.getType(), typedPacket.getData(), typedPacket.getMovement(), typedPacket.getYHeadRot());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_PLAYER_POSITION)) {
            ClientboundPlayerPositionPacket typedPacket = (ClientboundPlayerPositionPacket) packet;
            PositionMoveRotation oldPlayerPosition = typedPacket.change();
            Vec3 oldPos = oldPlayerPosition.position();

            double x = typedPacket.relatives().contains(Relative.X) ? oldPos.x : offsetX(oldPos.x, offset);
            double z = typedPacket.relatives().contains(Relative.Z) ? oldPos.z : offsetZ(oldPos.z, offset);

            Vec3 newPos = new Vec3(x, oldPos.y, z);

            PositionMoveRotation newPlayerPosition = new PositionMoveRotation(newPos, oldPlayerPosition.deltaMovement(), oldPlayerPosition.yRot(), oldPlayerPosition.xRot());
            return new ClientboundPlayerPositionPacket(typedPacket.id(), newPlayerPosition, typedPacket.relatives());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_TELEPORT_ENTITY)) {
            ClientboundTeleportEntityPacket typedPacket = (ClientboundTeleportEntityPacket) packet;
            PositionMoveRotation oldPlayerPosition = typedPacket.change();
            Vec3 oldPos = oldPlayerPosition.position();

            double x = typedPacket.relatives().contains(Relative.X) ? oldPos.x : offsetX(oldPos.x, offset);
            double z = typedPacket.relatives().contains(Relative.Z) ? oldPos.z : offsetZ(oldPos.z, offset);

            Vec3 newPos = new Vec3(x, oldPos.y, z);

            PositionMoveRotation newPlayerPosition = new PositionMoveRotation(newPos, oldPlayerPosition.deltaMovement(), oldPlayerPosition.yRot(), oldPlayerPosition.xRot());
            return new ClientboundTeleportEntityPacket(typedPacket.id(), newPlayerPosition, typedPacket.relatives(), typedPacket.onGround());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_PLAYER_LOOK_AT)) {
            ClientboundPlayerLookAtPacket typedPacket = (ClientboundPlayerLookAtPacket) packet;
            Vec3 targetPos = typedPacket.getPosition(world);
            if (targetPos == null) return packet;
            double x = offsetX(targetPos.x(), offset);
            double y = targetPos.y();
            double z = offsetZ(targetPos.z(), offset);
            return new ClientboundPlayerLookAtPacket(typedPacket.getFromAnchor(), x, y, z);
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_CHUNKS_BIOMES)) {
            ClientboundChunksBiomesPacket typedPacket = (ClientboundChunksBiomesPacket) packet;
            List<ClientboundChunksBiomesPacket.ChunkBiomeData> data = typedPacket.chunkBiomeData();
            ArrayList<ClientboundChunksBiomesPacket.ChunkBiomeData> newData = new ArrayList<>();
            for (ClientboundChunksBiomesPacket.ChunkBiomeData serialized : data) {
                newData.add(new ClientboundChunksBiomesPacket.ChunkBiomeData(offset(serialized.pos(), offset), serialized.buffer()));
            }
            return new ClientboundChunksBiomesPacket(newData);
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_EXPLODE)) {
            ClientboundExplodePacket typedPacket = (ClientboundExplodePacket) packet;
            return new ClientboundExplodePacket(offset(typedPacket.center(),offset), typedPacket.radius(), typedPacket.blockCount(), typedPacket.playerKnockback(), typedPacket.explosionParticle(), typedPacket.explosionSound(), typedPacket.blockParticles());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_LEVEL_CHUNK_WITH_LIGHT)) {
            ClientboundLevelChunkWithLightPacket typedPacket = (ClientboundLevelChunkWithLightPacket) packet;

            ChunkPos chunkPos = new ChunkPos(typedPacket.getX(), typedPacket.getZ());
            ChunkPos newChunkPos = offset(chunkPos, offset);

            ((HasAccessibleChunkPos)typedPacket).hidecoords$setChunkX(newChunkPos.x);
            ((HasAccessibleChunkPos)typedPacket).hidecoords$setChunkZ(newChunkPos.z);

            return typedPacket;
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_OPEN_SIGN_EDITOR)) {
            ClientboundOpenSignEditorPacket typedPacket = (ClientboundOpenSignEditorPacket) packet;
            return new ClientboundOpenSignEditorPacket(offset(typedPacket.getPos(), offset), typedPacket.isFrontText());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_SECTION_BLOCKS_UPDATE)) {
            ClientboundSectionBlocksUpdatePacket typedPacket = (ClientboundSectionBlocksUpdatePacket) packet;
            SectionPos oldSectionPos = ((ChunkDeltaUpdateS2CPacketAccessor)typedPacket).getSectionPos();
            SectionPos newSectionPos = SectionPos.of(offset(oldSectionPos.chunk(), offset), oldSectionPos.y());
            return new ClientboundSectionBlocksUpdatePacket(newSectionPos, ((IChunkDeltaUpdateS2CPacketMixin)typedPacket).hideCoordinates$getRememberedPositions(), ((IChunkDeltaUpdateS2CPacketMixin)typedPacket).hideCoordinates$getRememberedChunkSection());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_LEVEL_PARTICLES)) {
            ClientboundLevelParticlesPacket typedPacket = (ClientboundLevelParticlesPacket) packet;
            return new ClientboundLevelParticlesPacket(typedPacket.getParticle(),typedPacket.isOverrideLimiter(), typedPacket.alwaysShow(), offsetX(typedPacket.getX(), offset), typedPacket.getY(), offsetZ(typedPacket.getZ(), offset), typedPacket.getXDist(), typedPacket.getYDist(), typedPacket.getZDist(), typedPacket.getMaxSpeed(), typedPacket.getCount());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_SOUND)) {
            ClientboundSoundPacket typedPacket = (ClientboundSoundPacket) packet;

            return new ClientboundSoundPacket(typedPacket.getSound(),typedPacket.getSource(), offsetX(typedPacket.getX(),offset),typedPacket.getY(),offsetZ(typedPacket.getZ(),offset), typedPacket.getVolume(),typedPacket.getPitch(),typedPacket.getSeed());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_MOVE_VEHICLE)) {
            ClientboundMoveVehiclePacket typedPacket = (ClientboundMoveVehiclePacket) packet;

            return new ClientboundMoveVehiclePacket(offset(typedPacket.position(),offset), typedPacket.yRot(), typedPacket.xRot());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_SET_DEFAULT_SPAWN_POSITION)) {

            ClientboundSetDefaultSpawnPositionPacket typedPacket = (ClientboundSetDefaultSpawnPositionPacket) packet;
            return new ClientboundSetDefaultSpawnPositionPacket(new LevelData.RespawnData(offset(typedPacket.respawnData().globalPos(), offset), typedPacket.respawnData().yaw(), typedPacket.respawnData().pitch()));
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_FORGET_LEVEL_CHUNK)) {
            ClientboundForgetLevelChunkPacket typedPacket = (ClientboundForgetLevelChunkPacket) packet;
            return new ClientboundForgetLevelChunkPacket(offset(typedPacket.pos(),offset));
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_LIGHT_UPDATE)) {
            ClientboundLightUpdatePacket typedPacket = (ClientboundLightUpdatePacket) packet;
            ChunkPos newChunkPos = offset(new ChunkPos(typedPacket.getX(), typedPacket.getZ()),offset);
            ((HasAccessibleChunkPos)typedPacket).hidecoords$setChunkX(newChunkPos.x);
            ((HasAccessibleChunkPos)typedPacket).hidecoords$setChunkZ(newChunkPos.z);
            return typedPacket;
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_SET_CHUNK_CACHE_CENTER)) {
            ClientboundSetChunkCacheCenterPacket typedPacket = (ClientboundSetChunkCacheCenterPacket) packet;
            ChunkPos oldPos = new ChunkPos(typedPacket.getX(), typedPacket.getZ());
            ChunkPos newPos = offset(oldPos,offset);
            return new ClientboundSetChunkCacheCenterPacket(newPos.x,newPos.z);
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_ENTITY_POSITION_SYNC)) {
            ClientboundEntityPositionSyncPacket typedPacket = (ClientboundEntityPositionSyncPacket) packet;
            PositionMoveRotation oldPlayerPosition = typedPacket.values();
            Vec3 newPosition = offset(oldPlayerPosition.position(), offset);
            PositionMoveRotation newPlayerPosition = new PositionMoveRotation(newPosition, oldPlayerPosition.deltaMovement(), oldPlayerPosition.yRot(), oldPlayerPosition.xRot());
            return new ClientboundEntityPositionSyncPacket(typedPacket.id(), newPlayerPosition, typedPacket.onGround());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_LEVEL_EVENT)) {
            ClientboundLevelEventPacket typedPacket = (ClientboundLevelEventPacket) packet;

            return new ClientboundLevelEventPacket(typedPacket.getType(),offset(typedPacket.getPos(),offset),typedPacket.getData(),typedPacket.isGlobalEvent());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_SET_EQUIPMENT)) {
            ClientboundSetEquipmentPacket typedPacket = (ClientboundSetEquipmentPacket) packet;
            List<Pair<EquipmentSlot, ItemStack>> equipmentList = typedPacket.getSlots();
            ArrayList<Pair<EquipmentSlot, ItemStack>> newEquipmentList = new ArrayList<>();
            for (Pair<EquipmentSlot, ItemStack> pair : equipmentList)
                newEquipmentList.add(Pair.of(pair.getFirst(), offset(pair.getSecond(),offset)));

            return new ClientboundSetEquipmentPacket(typedPacket.getEntity(),newEquipmentList);
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_CONTAINER_SET_SLOT)) {
            ClientboundContainerSetSlotPacket typedPacket = (ClientboundContainerSetSlotPacket) packet;
            return new ClientboundContainerSetSlotPacket(typedPacket.getContainerId(), typedPacket.getStateId(), typedPacket.getSlot(), offset(typedPacket.getItem(),offset));
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_CONTAINER_SET_CONTENT)) {
            ClientboundContainerSetContentPacket typedPacket = (ClientboundContainerSetContentPacket) packet;
            List<ItemStack> contents = typedPacket.items();
            NonNullList<ItemStack> newContents = NonNullList.withSize(contents.size(), ItemStack.EMPTY);
            for (int i = 0; i < contents.size(); i++)
                newContents.set(i, offset(contents.get(i),offset));

            return new ClientboundContainerSetContentPacket(typedPacket.containerId(), typedPacket.stateId(), newContents, offset(typedPacket.carriedItem(),offset));
        }
        if (packetType.equals((GamePacketTypes.CLIENTBOUND_SET_ENTITY_DATA))) {
            ClientboundSetEntityDataPacket typedPacket = (ClientboundSetEntityDataPacket) packet;
            ArrayList<SynchedEntityData.DataValue<?>> newTrackedValues = new ArrayList<>(typedPacket.packedItems().size());
            if (typedPacket.packedItems().isEmpty()) return packet;
            for (int i = 0; i < typedPacket.packedItems().size(); i++)
            {
                SynchedEntityData.DataValue<?> entry = typedPacket.packedItems().get(i);
                SynchedEntityData.DataValue<?> newEntry = new SynchedEntityData.DataValue<>(entry.id(), (EntityDataSerializer<Object>) entry.serializer(), offsetData(entry.value(), offset));
                newTrackedValues.add(newEntry);
            }

            return new ClientboundSetEntityDataPacket(typedPacket.id(), newTrackedValues);
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_RESPAWN)) {
            ClientboundRespawnPacket typedPacket = (ClientboundRespawnPacket) packet;
            CommonPlayerSpawnInfo oldCommonPlayerSpawnInfo = typedPacket.commonPlayerSpawnInfo();
            Optional<GlobalPos> oldLastDeathLocation = oldCommonPlayerSpawnInfo.lastDeathLocation();
            Optional<GlobalPos> newLastDeathLocation = oldLastDeathLocation.map(pos -> offset(pos,offset));
            CommonPlayerSpawnInfo newCommonPlayerSpawnInfo = new CommonPlayerSpawnInfo(oldCommonPlayerSpawnInfo.dimensionType(), oldCommonPlayerSpawnInfo.dimension(), oldCommonPlayerSpawnInfo.seed(), oldCommonPlayerSpawnInfo.gameType(), oldCommonPlayerSpawnInfo.previousGameType(), oldCommonPlayerSpawnInfo.isDebug(), oldCommonPlayerSpawnInfo.isFlat(), newLastDeathLocation, oldCommonPlayerSpawnInfo.portalCooldown(), oldCommonPlayerSpawnInfo.seaLevel());
            return new ClientboundRespawnPacket(newCommonPlayerSpawnInfo, typedPacket.dataToKeep());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_LOGIN)) {
            ClientboundLoginPacket typedPacket = (ClientboundLoginPacket) packet;
            CommonPlayerSpawnInfo oldCommonPlayerSpawnInfo = typedPacket.commonPlayerSpawnInfo();
            Optional<GlobalPos> oldLastDeathLocation = oldCommonPlayerSpawnInfo.lastDeathLocation();
            Optional<GlobalPos> newLastDeathLocation = oldLastDeathLocation.map(pos -> offset(pos,offset));
            CommonPlayerSpawnInfo newCommonPlayerSpawnInfo = new CommonPlayerSpawnInfo(oldCommonPlayerSpawnInfo.dimensionType(), oldCommonPlayerSpawnInfo.dimension(), oldCommonPlayerSpawnInfo.seed(), oldCommonPlayerSpawnInfo.gameType(), oldCommonPlayerSpawnInfo.previousGameType(), oldCommonPlayerSpawnInfo.isDebug(), oldCommonPlayerSpawnInfo.isFlat(), newLastDeathLocation, oldCommonPlayerSpawnInfo.portalCooldown(), oldCommonPlayerSpawnInfo.seaLevel());
            return new ClientboundLoginPacket(typedPacket.playerId(), typedPacket.hardcore(), typedPacket.levels(), typedPacket.maxPlayers(), typedPacket.chunkRadius(), typedPacket.simulationDistance(), typedPacket.reducedDebugInfo(), typedPacket.showDeathScreen(), typedPacket.doLimitedCrafting(), newCommonPlayerSpawnInfo, typedPacket.enforcesSecureChat());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_SET_PLAYER_INVENTORY)) {
            ClientboundSetPlayerInventoryPacket typedPacket = (ClientboundSetPlayerInventoryPacket) packet;
            return new ClientboundSetPlayerInventoryPacket(typedPacket.slot(), offset(typedPacket.contents(), offset));
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_SET_CURSOR_ITEM)) {
            ClientboundSetCursorItemPacket typedPacket = (ClientboundSetCursorItemPacket) packet;
            return new ClientboundSetCursorItemPacket(offset(typedPacket.contents(), offset));
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_WAYPOINT)) {
            ClientboundTrackedWaypointPacket typedPacket = (ClientboundTrackedWaypointPacket) packet;
            TrackedWaypoint waypoint = typedPacket.waypoint();
            return new ClientboundTrackedWaypointPacket(typedPacket.operation(), offset(waypoint, offset));
        }

        if (packetType.equals(GamePacketTypes.CLIENTBOUND_DEBUG_BLOCK_VALUE)) {
            ClientboundDebugBlockValuePacket typedPacket = (ClientboundDebugBlockValuePacket) packet;
            return new ClientboundDebugBlockValuePacket(offset(typedPacket.blockPos(),offset), typedPacket.update());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_DEBUG_CHUNK_VALUE)) {
            ClientboundDebugChunkValuePacket typedPacket = (ClientboundDebugChunkValuePacket) packet;
            return new ClientboundDebugChunkValuePacket(offset(typedPacket.chunkPos(),offset), typedPacket.update());
        }
        if (packetType.equals(GamePacketTypes.CLIENTBOUND_GAME_TEST_HIGHLIGHT_POS)) {
            ClientboundGameTestHighlightPosPacket typedPacket = (ClientboundGameTestHighlightPosPacket) packet;
            return new ClientboundGameTestHighlightPosPacket(offset(typedPacket.absolutePos(),offset), typedPacket.relativePos());
        }

        //if (!(packetType.equals(PlayPackets.SET_BORDER_CENTER) || packetType.equals(PlayPackets.SET_BORDER_SIZE) || packetType.equals(PlayPackets.MOVE_ENTITY_POS)|| packetType.equals(PlayPackets.ENTITY_POSITION_SYNC)|| packetType.equals(PlayPackets.ROTATE_HEAD)|| packetType.equals(PlayPackets.SET_ENTITY_MOTION)|| packetType.equals(PlayPackets.MOVE_ENTITY_POS_ROT)))
            //System.out.println(packet);



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

    private static Vec3 offset(Vec3 vec3d, Offset offset) {
        return new Vec3(vec3d.x() + offset.getX(), vec3d.y, vec3d.z() + offset.getZ());
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
    public static ChunkPos offset(ChunkPos chunkPos, Offset offset)
    {
        return new ChunkPos(chunkPos.x + offset.getChunkX(), chunkPos.z + offset.getChunkZ());
    }

    public static ItemStack offset(ItemStack itemStack, Offset offset) {
        if (itemStack == null) return null;

        ItemStack result = itemStack.copy();

        BundleContents comp;
        if ((comp = itemStack.get(DataComponents.BUNDLE_CONTENTS)) != null)
        {
            BundleContents.Mutable newBundleComp = new BundleContents.Mutable(BundleContents.EMPTY);
            comp.itemCopyStream().forEach(innerStack -> newBundleComp.tryInsert(offset(innerStack, offset)));
            result.set(DataComponents.BUNDLE_CONTENTS, newBundleComp.toImmutable());
        }

        if (itemStack.is(Items.COMPASS)) {
            itemStack.getComponents().forEach(componentMapEntry -> {
                if (!(componentMapEntry.value() instanceof LodestoneTracker lodestoneComponent)) return;

                DataComponentType<LodestoneTracker> test = (DataComponentType<LodestoneTracker>) componentMapEntry.type();

                if (lodestoneComponent.target().isEmpty()) return;
                LodestoneTracker newLodestoneComponent = new LodestoneTracker(Optional.of(offset(lodestoneComponent.target().get(), offset)), lodestoneComponent.tracked());
                result.set(test, newLodestoneComponent);
            });
        }
        return result;
    }


    private static GlobalPos offset(GlobalPos globalPos, Offset offset) {
        return new GlobalPos(globalPos.dimension(), offset(globalPos.pos(),offset));
    }

    private static TrackedWaypoint offset(TrackedWaypoint waypoint, Offset offset) {
        return (TrackedWaypoint) ((OffsetableTrackedWaypoint)waypoint).hidecoords$offset(offset);
    }
}
