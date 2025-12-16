package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.*;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.GamePacketTypes;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(Connection.class)
public abstract class ClientConnectionMixin {

    @Unique
    private static final Set<PacketType<?>> PACKETS_WORLD_BORDER = Set.of(
            // These packets are translated in WorldBorderObfuscator, not this file.
            GamePacketTypes.CLIENTBOUND_INITIALIZE_BORDER,
            GamePacketTypes.CLIENTBOUND_SET_BORDER_CENTER,
            GamePacketTypes.CLIENTBOUND_SET_BORDER_LERP_SIZE,
            GamePacketTypes.CLIENTBOUND_SET_BORDER_SIZE,
            GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DELAY,
            GamePacketTypes.CLIENTBOUND_SET_BORDER_WARNING_DISTANCE
    );

    @Shadow private volatile @Nullable PacketListener packetListener;

    @Shadow @Final private Queue<Consumer<Connection>> pendingActions;

    @Inject(method = "genericsFtw", at =@At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void hidecoords$offsetIncomingPacket(Packet<T> packet, PacketListener listener, CallbackInfo ci)
    {
        if (listener instanceof ServerGamePacketListenerImpl serverPlayNetworkHandler) {
            //dont offset if gamerule off
            if (!serverPlayNetworkHandler.getPlayer().juhc$shouldOffset()) return;

            Offset offset = ((HasCoordOffset)serverPlayNetworkHandler).hidecoords$getCoordOffset();
            Packet<ServerGamePacketListener> newPacket = C2SPacketOffsetter.offsetPacket(packet, offset);
            newPacket.handle((ServerGamePacketListenerImpl) listener);
            ci.cancel();        }
    }

    @ModifyVariable(method = "doSendPacket", at = @At("HEAD"), argsOnly = true)
    public Packet<?> juhc$offsetOutgoingPacket(Packet<?> packet) {
        if (this.packetListener instanceof  ServerGamePacketListenerImpl serverPlayNetworkHandler) {
            //dont offset if gamerule off
            if (!serverPlayNetworkHandler.getPlayer().juhc$shouldOffset()) return packet;

            Offset offset = ((HasCoordOffset) serverPlayNetworkHandler).hidecoords$getCoordOffset();
            return PACKETS_WORLD_BORDER.contains(packet.type())
            ? WorldBorderObfuscator.translate(packet, offset,serverPlayNetworkHandler.getPlayer())
            : S2CPacketOffsetter.offsetPacket(packet, offset, serverPlayNetworkHandler.getPlayer().level());
        }
        return packet;
    }

}
