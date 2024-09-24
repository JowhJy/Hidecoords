package com.jowhjy.hidecoords.util;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.mixin.ChunkDataS2CPacketAccessor;
import com.jowhjy.hidecoords.mixin.ChunkDeltaUpdateS2CPacketAccessor;
import com.jowhjy.hidecoords.mixin.LightUpdateS2CPacketAccessor;
import com.jowhjy.hidecoords.mixin.VehicleMoveS2CPacketAccessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            ((ChunkDataS2CPacketAccessor) typedPacket).setChunkX(offset.getChunkX() + typedPacket.getChunkX());
            ((ChunkDataS2CPacketAccessor) typedPacket).setChunkZ(offset.getChunkZ() + typedPacket.getChunkZ());
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
            ((ChunkDeltaUpdateS2CPacketAccessor)typedPacket).setSectionPos(newSectionPos);
            return typedPacket;
        }
        if (packetType.equals(PlayPackets.LEVEL_PARTICLES)) {
            ParticleS2CPacket typedPacket = (ParticleS2CPacket) packet;
            return new ParticleS2CPacket(typedPacket.getParameters(), typedPacket.isLongDistance(), offsetX(typedPacket.getX(), offset), typedPacket.getY(), offsetZ(typedPacket.getZ(), offset), typedPacket.getOffsetX(), typedPacket.getOffsetY(), typedPacket.getOffsetZ(), typedPacket.getSpeed(), typedPacket.getCount());
        }
        if (packetType.equals(PlayPackets.SOUND)) {
            PlaySoundS2CPacket typedPacket = (PlaySoundS2CPacket) packet;

            return new PlaySoundS2CPacket(typedPacket.getSound(),typedPacket.getCategory(), offsetX8(typedPacket.getX(),offset),typedPacket.getY(),offsetZ8(typedPacket.getZ(),offset), typedPacket.getVolume(),typedPacket.getPitch(),typedPacket.getSeed());
        }
        if (packetType.equals(PlayPackets.SOUND_ENTITY)) {
            PlaySoundFromEntityS2CPacket typedPacket = (PlaySoundFromEntityS2CPacket) packet;

            return new PlaySoundFromEntityS2CPacket(typedPacket.getSound(),typedPacket.getCategory(), Objects.requireNonNull(world.getEntityById(typedPacket.getEntityId())), typedPacket.getVolume(),typedPacket.getPitch(),typedPacket.getSeed());
        }
        if (packetType.equals(PlayPackets.MOVE_VEHICLE_S2C)) {
            VehicleMoveS2CPacket typedPacket = (VehicleMoveS2CPacket) packet;
            ((VehicleMoveS2CPacketAccessor)typedPacket).setX(offsetX(typedPacket.getX(),offset));
            ((VehicleMoveS2CPacketAccessor)typedPacket).setZ(offsetZ(typedPacket.getZ(),offset));
            return typedPacket;
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
            ((LightUpdateS2CPacketAccessor)typedPacket).setChunkX(newChunkPos.x);
            ((LightUpdateS2CPacketAccessor)typedPacket).setChunkZ(newChunkPos.z);
            return typedPacket;
        }
        //todo item and mob nbt stuff





        return packet;
    }

    private static Vec3d offset(Vec3d vec3d, Offset offset) {
        return new Vec3d(vec3d.getX() + offset.getX(), vec3d.y, vec3d.getZ() + offset.getZ());
    }

    public static BlockPos offset(BlockPos blockPos, Offset offset)
    {
        return blockPos.add(offset.getBlockPos());
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

}
