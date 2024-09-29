package com.jowhjy.hidecoords;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class C2SPacketOffsetter {
    public static <T extends PacketListener> Packet<ServerPlayPacketListener> offsetPacket(Packet<T> packet, Offset offset) {

        PacketType<? extends Packet<T>> packetType = packet.getPacketId();

        if (packetType.equals(PlayPackets.MOVE_VEHICLE_C2S))
        {
            VehicleMoveC2SPacket typedPacket = (VehicleMoveC2SPacket) packet;

            Entity dummyEntity = new ArmorStandEntity(null, unoffsetX(typedPacket.getX(),offset), typedPacket.getY(),unoffsetZ(typedPacket.getZ(),offset));
            dummyEntity.setAngles(typedPacket.getYaw(),typedPacket.getPitch());

            return new VehicleMoveC2SPacket(dummyEntity);

        }
        if (packetType.equals(PlayPackets.PLAYER_ACTION))
        {
            PlayerActionC2SPacket typedPacket = (PlayerActionC2SPacket) packet;
            return new PlayerActionC2SPacket(typedPacket.getAction(),unoffset(typedPacket.getPos(),offset),typedPacket.getDirection(),typedPacket.getSequence());
        }
        if (packetType.equals(PlayPackets.MOVE_PLAYER_POS_ROT))
        {
            PlayerMoveC2SPacket.Full typedPacket = (PlayerMoveC2SPacket.Full) packet;
            return new PlayerMoveC2SPacket.Full(unoffsetX(typedPacket.getX(0),offset),typedPacket.getY(0),unoffsetZ(typedPacket.getZ(0),offset),typedPacket.getYaw(0),typedPacket.getPitch(0),typedPacket.isOnGround());
        }
        if (packetType.equals(PlayPackets.MOVE_PLAYER_POS))
        {
            PlayerMoveC2SPacket.PositionAndOnGround typedPacket = (PlayerMoveC2SPacket.PositionAndOnGround) packet;
            return new PlayerMoveC2SPacket.PositionAndOnGround(unoffsetX(typedPacket.getX(0),offset),typedPacket.getY(0),unoffsetZ(typedPacket.getZ(0),offset),typedPacket.isOnGround());
        }
        if (packetType.equals(PlayPackets.JIGSAW_GENERATE))
        {
            JigsawGeneratingC2SPacket typedPacket = (JigsawGeneratingC2SPacket) packet;
            return new JigsawGeneratingC2SPacket(unoffset(typedPacket.getPos(),offset),typedPacket.getMaxDepth(),typedPacket.shouldKeepJigsaws());
        }
        if (packetType.equals(PlayPackets.SET_JIGSAW_BLOCK))
        {
            UpdateJigsawC2SPacket typedPacket = (UpdateJigsawC2SPacket) packet;
            return new UpdateJigsawC2SPacket(unoffset(typedPacket.getPos(),offset),typedPacket.getName(),typedPacket.getTarget(),typedPacket.getPool(),typedPacket.getFinalState(),typedPacket.getJointType(),typedPacket.getSelectionPriority(),typedPacket.getPlacementPriority());
        }
        if (packetType.equals(PlayPackets.SET_COMMAND_BLOCK))
        {
            UpdateCommandBlockC2SPacket typedPacket = (UpdateCommandBlockC2SPacket) packet;
            return new UpdateCommandBlockC2SPacket(unoffset(typedPacket.getPos(),offset),typedPacket.getCommand(),typedPacket.getType(),typedPacket.shouldTrackOutput(),typedPacket.isConditional(),typedPacket.isAlwaysActive());
        }
        if (packetType.equals(PlayPackets.USE_ITEM_ON))
        {
            PlayerInteractBlockC2SPacket typedPacket = (PlayerInteractBlockC2SPacket) packet;
            BlockHitResult blockHitResult = typedPacket.getBlockHitResult();
            BlockHitResult newBlockHitResult = new BlockHitResult(unoffset(blockHitResult.getPos(),offset), blockHitResult.getSide(), unoffset(blockHitResult.getBlockPos(),offset), blockHitResult.isInsideBlock());

            return new PlayerInteractBlockC2SPacket(typedPacket.getHand(), newBlockHitResult, typedPacket.getSequence());
        }
        if (packetType.equals(PlayPackets.SET_CREATIVE_MODE_SLOT))
        {
            CreativeInventoryActionC2SPacket typedPacket = (CreativeInventoryActionC2SPacket) packet;

            return new CreativeInventoryActionC2SPacket(typedPacket.slot(), unoffset(typedPacket.stack(),offset));
        }
        if (packetType.equals((PlayPackets.CONTAINER_CLICK)))
        {
            ClickSlotC2SPacket typedPacket = (ClickSlotC2SPacket) packet;
            ItemStack newStack = unoffset(typedPacket.getStack(),offset);

            Int2ObjectMap<ItemStack> int2ObjectMap = typedPacket.getModifiedStacks();

            for (int j = 0; j < int2ObjectMap.size(); j++) {
                int2ObjectMap.put(j, unoffset(int2ObjectMap.get(j),offset));
            }

            return new ClickSlotC2SPacket(typedPacket.getSyncId(), typedPacket.getRevision(), typedPacket.getSlot(), typedPacket.getButton(),typedPacket.getActionType(),newStack,int2ObjectMap);
        }


        return (Packet<ServerPlayPacketListener>) packet;
    }

    private static Vec3d unoffset(Vec3d pos, Offset offset) {
        return new Vec3d(pos.getX() - offset.getX(), pos.y, pos.getZ() - offset.getZ());
    }

    private static BlockPos unoffset(BlockPos pos, Offset offset) {
        return new BlockPos(pos.getX() - offset.getX(), pos.getY(), pos.getZ() - offset.getZ());
    }

    private static double unoffsetX(double x, Offset offset) {
        return x - offset.getX();
    }

    private static double unoffsetZ(double z, Offset offset) {
        return z - offset.getZ();
    }

    public static ItemStack unoffset(ItemStack itemStack, Offset offset) {
        if (itemStack == null) return null;

        if (itemStack.isOf(Items.COMPASS)) {
            LodestoneTrackerComponent lodestoneComponent = itemStack.getComponents().get(DataComponentTypes.LODESTONE_TRACKER);
            if (lodestoneComponent == null || lodestoneComponent.target().isEmpty()) return itemStack;
            LodestoneTrackerComponent newLodestoneComponent = new LodestoneTrackerComponent(Optional.of(unoffset(lodestoneComponent.target().get(), offset)), lodestoneComponent.tracked());
            itemStack.set(DataComponentTypes.LODESTONE_TRACKER, newLodestoneComponent);
        }
        return itemStack;
    }

    private static GlobalPos unoffset(GlobalPos globalPos, Offset offset) {
        return new GlobalPos(globalPos.dimension(), unoffset(globalPos.pos(),offset));
    }
}
