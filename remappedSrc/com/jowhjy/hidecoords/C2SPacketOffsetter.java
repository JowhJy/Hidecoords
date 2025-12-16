package com.jowhjy.hidecoords;

import com.jowhjy.hidecoords.mixin.PlayerMoveC2SPacketAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.PacketListener;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.GamePacketTypes;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundJigsawGeneratePacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPickItemFromBlockPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundSetCommandBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.network.protocol.game.ServerboundSetJigsawBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetStructureBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSetTestBlockPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.network.protocol.game.ServerboundTestInstanceBlockActionPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import java.util.Optional;

public class C2SPacketOffsetter {
    public static <T extends PacketListener> Packet<ServerGamePacketListener> offsetPacket(Packet<T> packet, Offset offset) {

        PacketType<? extends Packet<T>> packetType = packet.type();

        if (packetType.equals(GamePacketTypes.SERVERBOUND_MOVE_VEHICLE))
        {
            ServerboundMoveVehiclePacket typedPacket = (ServerboundMoveVehiclePacket) packet;

            return new ServerboundMoveVehiclePacket(unoffset(typedPacket.position(),offset), typedPacket.yRot(), typedPacket.xRot(), typedPacket.onGround());

        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_PLAYER_ACTION))
        {
            ServerboundPlayerActionPacket typedPacket = (ServerboundPlayerActionPacket) packet;
            return new ServerboundPlayerActionPacket(typedPacket.getAction(),unoffset(typedPacket.getPos(),offset),typedPacket.getDirection(),typedPacket.getSequence());
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS_ROT))
        {
            ServerboundMovePlayerPacket.PosRot typedPacket = (ServerboundMovePlayerPacket.PosRot) packet;
            PlayerMoveC2SPacketAccessor packetAccessor = (PlayerMoveC2SPacketAccessor) packet;
            return new ServerboundMovePlayerPacket.PosRot(unoffsetX(packetAccessor.getX(),offset),packetAccessor.getY(),unoffsetZ(packetAccessor.getZ(),offset),packetAccessor.getYaw(),packetAccessor.getPitch(),typedPacket.isOnGround(), typedPacket.horizontalCollision());
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_MOVE_PLAYER_POS))
        {
            ServerboundMovePlayerPacket.Pos typedPacket = (ServerboundMovePlayerPacket.Pos) packet;
            PlayerMoveC2SPacketAccessor packetAccessor = (PlayerMoveC2SPacketAccessor) packet;
            return new ServerboundMovePlayerPacket.Pos(unoffsetX(packetAccessor.getX(),offset),packetAccessor.getY(),unoffsetZ(packetAccessor.getZ(),offset),typedPacket.isOnGround(), typedPacket.horizontalCollision());
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_JIGSAW_GENERATE))
        {
            ServerboundJigsawGeneratePacket typedPacket = (ServerboundJigsawGeneratePacket) packet;
            return new ServerboundJigsawGeneratePacket(unoffset(typedPacket.getPos(),offset),typedPacket.levels(),typedPacket.keepJigsaws());
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_SET_JIGSAW_BLOCK))
        {
            ServerboundSetJigsawBlockPacket typedPacket = (ServerboundSetJigsawBlockPacket) packet;
            return new ServerboundSetJigsawBlockPacket(unoffset(typedPacket.getPos(),offset),typedPacket.getName(),typedPacket.getTarget(),typedPacket.getPool(),typedPacket.getFinalState(),typedPacket.getJoint(),typedPacket.getSelectionPriority(),typedPacket.getPlacementPriority());
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_SET_COMMAND_BLOCK))
        {
            ServerboundSetCommandBlockPacket typedPacket = (ServerboundSetCommandBlockPacket) packet;
            return new ServerboundSetCommandBlockPacket(unoffset(typedPacket.getPos(),offset),typedPacket.getCommand(),typedPacket.getMode(),typedPacket.isTrackOutput(),typedPacket.isConditional(),typedPacket.isAutomatic());
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_USE_ITEM_ON))
        {
            ServerboundUseItemOnPacket typedPacket = (ServerboundUseItemOnPacket) packet;
            BlockHitResult blockHitResult = typedPacket.getHitResult();
            BlockHitResult newBlockHitResult = new BlockHitResult(unoffset(blockHitResult.getLocation(),offset), blockHitResult.getDirection(), unoffset(blockHitResult.getBlockPos(),offset), blockHitResult.isInside());

            return new ServerboundUseItemOnPacket(typedPacket.getHand(), newBlockHitResult, typedPacket.getSequence());
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_SET_CREATIVE_MODE_SLOT))
        {
            ServerboundSetCreativeModeSlotPacket typedPacket = (ServerboundSetCreativeModeSlotPacket) packet;

            return new ServerboundSetCreativeModeSlotPacket(typedPacket.slotNum(), unoffset(typedPacket.itemStack(),offset));
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_SIGN_UPDATE))
        {
            ServerboundSignUpdatePacket typedPacket = (ServerboundSignUpdatePacket) packet;

            return new ServerboundSignUpdatePacket(unoffset(typedPacket.getPos(),offset), typedPacket.isFrontText(), typedPacket.getLines()[0], typedPacket.getLines()[1], typedPacket.getLines()[2], typedPacket.getLines()[3]);
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_PICK_ITEM_FROM_BLOCK))
        {
            ServerboundPickItemFromBlockPacket typedPacket = (ServerboundPickItemFromBlockPacket) packet;

            return new ServerboundPickItemFromBlockPacket(unoffset(typedPacket.pos(),offset), typedPacket.includeData());
        }

        if (packetType.equals(GamePacketTypes.SERVERBOUND_SET_STRUCTURE_BLOCK))
        {
            ServerboundSetStructureBlockPacket typedPacket = (ServerboundSetStructureBlockPacket) packet;

            return new ServerboundSetStructureBlockPacket(unoffset(typedPacket.getPos(),offset), typedPacket.getUpdateType(), typedPacket.getMode(), typedPacket.getName(),typedPacket.getOffset(),typedPacket.getSize(),typedPacket.getMirror(),typedPacket.getRotation(),typedPacket.getData(),typedPacket.isIgnoreEntities(),typedPacket.isStrict(), typedPacket.isShowAir(),typedPacket.isShowBoundingBox(),typedPacket.getIntegrity(),typedPacket.getSeed());
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_SET_TEST_BLOCK))
        {
            ServerboundSetTestBlockPacket typedPacket = (ServerboundSetTestBlockPacket) packet;

            return new ServerboundSetTestBlockPacket(unoffset(typedPacket.position(),offset), typedPacket.mode(), typedPacket.message());
        }
        if (packetType.equals(GamePacketTypes.SERVERBOUND_TEST_INSTANCE_BLOCK_ACTION))
        {
            ServerboundTestInstanceBlockActionPacket typedPacket = (ServerboundTestInstanceBlockActionPacket) packet;

            return new ServerboundTestInstanceBlockActionPacket(unoffset(typedPacket.pos(),offset), typedPacket.action(), typedPacket.data());
        }
        //CONTAINER_CLICK_C2S cannot be unoffset because the item is sent as a hash, causing the ghost item desyncs for lodestone compasses. (see also https://github.com/joshuaprince/CoordinateOffset/blob/master/src/main/java/com/jtprince/coordinateoffset/offsetter/client/OffsetterClientClickWindow.java)
        //there is nothing I can do about that for now


        return (Packet<ServerGamePacketListener>) packet;
    }

    private static Vec3 unoffset(Vec3 pos, Offset offset) {
        return new Vec3(pos.x() - offset.getX(), pos.y, pos.z() - offset.getZ());
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

        BundleContents comp;
        if ((comp = itemStack.get(DataComponents.BUNDLE_CONTENTS)) != null)
        {
            BundleContents.Mutable newBundleComp = new BundleContents.Mutable(BundleContents.EMPTY);
            comp.itemCopyStream().forEach(innerStack -> newBundleComp.tryInsert(unoffset(innerStack, offset)));
            result.set(DataComponents.BUNDLE_CONTENTS, newBundleComp.toImmutable());
        }

        if (itemStack.is(Items.COMPASS)) {
            itemStack.getComponents().forEach(componentMapEntry -> {
                if (!(componentMapEntry.value() instanceof LodestoneTracker lodestoneComponent)) return;

                DataComponentType<LodestoneTracker> test = (DataComponentType<LodestoneTracker>) componentMapEntry.type();

                if (lodestoneComponent.target().isEmpty()) return;
                LodestoneTracker newLodestoneComponent = new LodestoneTracker(Optional.of(unoffset(lodestoneComponent.target().get(), offset)), lodestoneComponent.tracked());
                result.set(test, newLodestoneComponent);
            });
        }
        return result;
    }

    private static GlobalPos unoffset(GlobalPos globalPos, Offset offset) {
        return new GlobalPos(globalPos.dimension(), unoffset(globalPos.pos(),offset));
    }
}
