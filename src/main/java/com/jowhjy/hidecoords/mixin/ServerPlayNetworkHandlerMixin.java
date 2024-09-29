package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.WorldBorderObfuscator;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import com.jowhjy.hidecoords.S2CPacketOffsetter;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandler implements HasCoordOffset {

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

    @Shadow public abstract ServerPlayerEntity getPlayer();

    @Unique
    Offset juhc$coordOffset;

    public ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Unique
    @Override
    public Offset juhc$getCoordOffset()
    {
        return juhc$coordOffset;
    }
    @Unique
    @Override
    public void juhc$setCoordOffset(Offset coordOffset)
    {
        juhc$coordOffset = coordOffset;
    }

    /** Inject into the constructor to make the offsetPacket
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void juhc$createOffset(MinecraftServer server, ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci){

        juhc$coordOffset = Offset.zeroAtLocation(player.getBlockPos());

    }

    @Override
    public void send(Packet<?> packet, @Nullable PacketCallbacks callbacks)
    {
        Packet<?> newPacket = S2CPacketOffsetter.offsetPacket(packet, juhc$coordOffset, this.getPlayer().getWorld());
        if (PACKETS_WORLD_BORDER.contains(newPacket.getPacketId()))
        {
            newPacket = WorldBorderObfuscator.translate(newPacket,juhc$coordOffset,this.getPlayer());
        }

        super.send(newPacket, callbacks);
    }

}