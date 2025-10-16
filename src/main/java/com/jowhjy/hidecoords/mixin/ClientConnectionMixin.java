package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.*;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.server.network.ServerPlayNetworkHandler;
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

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {

    @Unique
    private static final Set<PacketType<?>> PACKETS_WORLD_BORDER = Set.of(
            // These packets are translated in WorldBorderObfuscator, not this file.
            PlayPackets.INITIALIZE_BORDER,
            PlayPackets.SET_BORDER_CENTER,
            PlayPackets.SET_BORDER_LERP_SIZE,
            PlayPackets.SET_BORDER_SIZE,
            PlayPackets.SET_BORDER_WARNING_DELAY,
            PlayPackets.SET_BORDER_WARNING_DISTANCE
    );

    @Shadow private volatile @Nullable PacketListener packetListener;

    @Shadow @Final private Queue<Consumer<ClientConnection>> queuedTasks;

    @Inject(method = "handlePacket", at =@At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void hidecoords$offsetIncomingPacket(Packet<T> packet, PacketListener listener, CallbackInfo ci)
    {
        if (listener instanceof ServerPlayNetworkHandler serverPlayNetworkHandler) {
            //dont offset if gamerule off
            if (!serverPlayNetworkHandler.getPlayer().juhc$shouldOffset()) return;

            Offset offset = ((HasCoordOffset)serverPlayNetworkHandler).hidecoords$getCoordOffset();
            Packet<ServerPlayPacketListener> newPacket = C2SPacketOffsetter.offsetPacket(packet, offset);
            newPacket.apply((ServerPlayNetworkHandler) listener);
            ci.cancel();        }
    }

    @ModifyVariable(method = "sendInternal", at = @At("HEAD"), argsOnly = true)
    public Packet<?> juhc$offsetOutgoingPacket(Packet<?> packet) {
        if (this.packetListener instanceof  ServerPlayNetworkHandler serverPlayNetworkHandler) {
            //dont offset if gamerule off
            if (!serverPlayNetworkHandler.getPlayer().juhc$shouldOffset()) return packet;

            Offset offset = ((HasCoordOffset) serverPlayNetworkHandler).hidecoords$getCoordOffset();
            return PACKETS_WORLD_BORDER.contains(packet.getPacketType())
            ? WorldBorderObfuscator.translate(packet, offset,serverPlayNetworkHandler.getPlayer())
            : S2CPacketOffsetter.offsetPacket(packet, offset, serverPlayNetworkHandler.getPlayer().getEntityWorld());
        }
        return packet;
    }

}
