package com.jowhjy.hidecoords;

import com.jowhjy.hidecoords.mixin.PlayerMoveC2SPacketAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.LodestoneTrackerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class C2SPacketOffsetter {
    public static <T extends PacketListener> Packet<ServerPlayPacketListener> offsetPacket(Packet<T> packet, Offset offset) {

        PacketType<? extends Packet<T>> packetType = packet.getPacketType();

        if (packetType.equals(PlayPackets.MOVE_VEHICLE_C2S))
        {
            VehicleMoveC2SPacket typedPacket = (VehicleMoveC2SPacket) packet;

            return new VehicleMoveC2SPacket(unoffset(typedPacket.position(),offset), typedPacket.yaw(), typedPacket.pitch(), typedPacket.onGround());

        }
        if (packetType.equals(PlayPackets.PLAYER_ACTION))
        {
            PlayerActionC2SPacket typedPacket = (PlayerActionC2SPacket) packet;
            return new PlayerActionC2SPacket(typedPacket.getAction(),unoffset(typedPacket.getPos(),offset),typedPacket.getDirection(),typedPacket.getSequence());
        }
        if (packetType.equals(PlayPackets.MOVE_PLAYER_POS_ROT))
        {
            PlayerMoveC2SPacket.Full typedPacket = (PlayerMoveC2SPacket.Full) packet;
            PlayerMoveC2SPacketAccessor packetAccessor = (PlayerMoveC2SPacketAccessor) packet;
            return new PlayerMoveC2SPacket.Full(unoffsetX(packetAccessor.getX(),offset),packetAccessor.getY(),unoffsetZ(packetAccessor.getZ(),offset),packetAccessor.getYaw(),packetAccessor.getPitch(),typedPacket.isOnGround(), typedPacket.horizontalCollision());
        }
        if (packetType.equals(PlayPackets.MOVE_PLAYER_POS))
        {
            PlayerMoveC2SPacket.PositionAndOnGround typedPacket = (PlayerMoveC2SPacket.PositionAndOnGround) packet;
            PlayerMoveC2SPacketAccessor packetAccessor = (PlayerMoveC2SPacketAccessor) packet;
            return new PlayerMoveC2SPacket.PositionAndOnGround(unoffsetX(packetAccessor.getX(),offset),packetAccessor.getY(),unoffsetZ(packetAccessor.getZ(),offset),typedPacket.isOnGround(), typedPacket.horizontalCollision());
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
        if (packetType.equals(PlayPackets.SIGN_UPDATE))
        {
            UpdateSignC2SPacket typedPacket = (UpdateSignC2SPacket) packet;

            return new UpdateSignC2SPacket(unoffset(typedPacket.getPos(),offset), typedPacket.isFront(), typedPacket.getText()[0], typedPacket.getText()[1], typedPacket.getText()[2], typedPacket.getText()[3]);
        }
        if (packetType.equals(PlayPackets.PICK_ITEM_FROM_BLOCK))
        {
            PickItemFromBlockC2SPacket typedPacket = (PickItemFromBlockC2SPacket) packet;

            return new PickItemFromBlockC2SPacket(unoffset(typedPacket.pos(),offset), typedPacket.includeData());
        }

        if (packetType.equals(PlayPackets.SET_STRUCTURE_BLOCK))
        {
            UpdateStructureBlockC2SPacket typedPacket = (UpdateStructureBlockC2SPacket) packet;

            return new UpdateStructureBlockC2SPacket(unoffset(typedPacket.getPos(),offset), typedPacket.getAction(), typedPacket.getMode(), typedPacket.getTemplateName(),typedPacket.getOffset(),typedPacket.getSize(),typedPacket.getMirror(),typedPacket.getRotation(),typedPacket.getMetadata(),typedPacket.shouldIgnoreEntities(),typedPacket.isStrict(), typedPacket.shouldShowAir(),typedPacket.shouldShowBoundingBox(),typedPacket.getIntegrity(),typedPacket.getSeed());
        }
        if (packetType.equals(PlayPackets.SET_TEST_BLOCK))
        {
            SetTestBlockC2SPacket typedPacket = (SetTestBlockC2SPacket) packet;

            return new SetTestBlockC2SPacket(unoffset(typedPacket.position(),offset), typedPacket.mode(), typedPacket.message());
        }
        if (packetType.equals(PlayPackets.TEST_INSTANCE_BLOCK_ACTION))
        {
            TestInstanceBlockActionC2SPacket typedPacket = (TestInstanceBlockActionC2SPacket) packet;

            return new TestInstanceBlockActionC2SPacket(unoffset(typedPacket.pos(),offset), typedPacket.action(), typedPacket.data());
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

        ItemStack result = itemStack.copy();

        BundleContentsComponent comp;
        if ((comp = itemStack.get(DataComponentTypes.BUNDLE_CONTENTS)) != null)
        {
            BundleContentsComponent.Builder newBundleComp = new BundleContentsComponent.Builder(BundleContentsComponent.DEFAULT);
            comp.stream().forEach(innerStack -> newBundleComp.add(unoffset(innerStack, offset)));
            result.set(DataComponentTypes.BUNDLE_CONTENTS, newBundleComp.build());
        }

        if (itemStack.isOf(Items.COMPASS)) {
            itemStack.getComponents().forEach(componentMapEntry -> {
                if (!(componentMapEntry.value() instanceof LodestoneTrackerComponent lodestoneComponent)) return;

                ComponentType<LodestoneTrackerComponent> test = (ComponentType<LodestoneTrackerComponent>) componentMapEntry.type();

                if (lodestoneComponent.target().isEmpty()) return;
                LodestoneTrackerComponent newLodestoneComponent = new LodestoneTrackerComponent(Optional.of(unoffset(lodestoneComponent.target().get(), offset)), lodestoneComponent.tracked());
                result.set(test, newLodestoneComponent);
            });
        }
        return result;
    }

    private static GlobalPos unoffset(GlobalPos globalPos, Offset offset) {
        return new GlobalPos(globalPos.dimension(), unoffset(globalPos.pos(),offset));
    }
}
