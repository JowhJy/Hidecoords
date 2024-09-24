package com.jowhjy.hidecoords.util;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.mixin.VehicleMoveC2SPacketAccessor;
import com.jowhjy.hidecoords.mixin.VehicleMoveS2CPacketAccessor;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class C2SPacketOffsetter {
    public static <T extends PacketListener> Packet<?> offsetPacket(Packet<T> packet, Offset offset) {

        PacketType<? extends Packet<T>> packetType = packet.getPacketId();

        if (packet.equals(PlayPackets.MOVE_VEHICLE_C2S))
        {
            VehicleMoveC2SPacket typedPacket = (VehicleMoveC2SPacket) packet;
            ((VehicleMoveC2SPacketAccessor)typedPacket).setX(unoffsetX(typedPacket.getX(),offset));
            ((VehicleMoveC2SPacketAccessor)typedPacket).setZ(unoffsetZ(typedPacket.getZ(),offset));
            return typedPacket;

        }
        if (packet.equals(PlayPackets.PLAYER_ACTION))
        {
            PlayerActionC2SPacket typedPacket = (PlayerActionC2SPacket) packet;
            return new PlayerActionC2SPacket(typedPacket.getAction(),unoffset(typedPacket.getPos(),offset),typedPacket.getDirection(),typedPacket.getSequence());
        }
        if (packet.equals(PlayPackets.MOVE_PLAYER_POS_ROT))
        {
            PlayerMoveC2SPacket.Full typedPacket = (PlayerMoveC2SPacket.Full) packet;
            return new PlayerMoveC2SPacket.Full(unoffsetX(typedPacket.getX(0),offset),typedPacket.getY(0),unoffsetZ(typedPacket.getZ(0),offset),typedPacket.getYaw(0),typedPacket.getPitch(0),typedPacket.isOnGround());
        }
        if (packet.equals(PlayPackets.MOVE_PLAYER_POS))
        {
            PlayerMoveC2SPacket.PositionAndOnGround typedPacket = (PlayerMoveC2SPacket.PositionAndOnGround) packet;
            return new PlayerMoveC2SPacket.PositionAndOnGround(unoffsetX(typedPacket.getX(0),offset),typedPacket.getY(0),unoffsetZ(typedPacket.getZ(0),offset),typedPacket.isOnGround());
        }
        if (packet.equals(PlayPackets.JIGSAW_GENERATE))
        {
            JigsawGeneratingC2SPacket typedPacket = (JigsawGeneratingC2SPacket) packet;
            return new JigsawGeneratingC2SPacket(unoffset(typedPacket.getPos(),offset),typedPacket.getMaxDepth(),typedPacket.shouldKeepJigsaws());
        }
        if (packet.equals(PlayPackets.SET_JIGSAW_BLOCK))
        {
            UpdateJigsawC2SPacket typedPacket = (UpdateJigsawC2SPacket) packet;
            return new UpdateJigsawC2SPacket(unoffset(typedPacket.getPos(),offset),typedPacket.getName(),typedPacket.getTarget(),typedPacket.getPool(),typedPacket.getFinalState(),typedPacket.getJointType(),typedPacket.getSelectionPriority(),typedPacket.getPlacementPriority());
        }
        if (packet.equals(PlayPackets.SET_COMMAND_BLOCK))
        {
            UpdateCommandBlockC2SPacket typedPacket = (UpdateCommandBlockC2SPacket) packet;
            return new UpdateCommandBlockC2SPacket(unoffset(typedPacket.getPos(),offset),typedPacket.getCommand(),typedPacket.getType(),typedPacket.shouldTrackOutput(),typedPacket.isConditional(),typedPacket.isAlwaysActive());
        }


        return packet;
    }

    private static BlockPos unoffset(BlockPos pos, Offset offset) {
        return pos.subtract(offset.getBlockPos());
    }

    private static double unoffsetX(double x, Offset offset) {
        return x - offset.getX();
    }

    private static double unoffsetZ(double z, Offset offset) {
        return z - offset.getZ();
    }
}
