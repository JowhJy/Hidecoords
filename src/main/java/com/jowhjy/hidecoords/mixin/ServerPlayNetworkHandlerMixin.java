package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.PacketType;
import net.minecraft.network.packet.PlayPackets;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
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

    @Shadow private double lastTickX;
    @Unique
    Offset hidecoords$coordOffset;

    public ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Unique
    @Override
    public Offset hidecoords$getCoordOffset()
    {
        return hidecoords$coordOffset;
    }
    @Unique
    @Override
    public void hidecoords$setCoordOffset(Offset coordOffset)
    {
        hidecoords$coordOffset = coordOffset;
    }

    /** Inject into the constructor to make the offsetPacket
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void hidecoords$createOffset(MinecraftServer server, ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci){

        hidecoords$coordOffset = Offset.zeroAtLocation(player.getBlockPos());

    }

    /*@Override
    public void send(Packet<?> packet, @Nullable PacketCallbacks callbacks)
    {
        //no offset if gamerule off
        if (!this.getPlayer().getServerWorld().getGameRules().getBoolean(Hidecoords.HIDECOORDS_GAMERULE)) {
            super.send(packet, callbacks);
            return;
        }

        Packet<?> newPacket = S2CPacketOffsetter.offsetPacket(packet, hidecoords$coordOffset, this.getPlayer().getWorld());
        if (PACKETS_WORLD_BORDER.contains(newPacket.getPacketType()))
        {
            newPacket = WorldBorderObfuscator.translate(newPacket, hidecoords$coordOffset,this.getPlayer());
        }

        super.send(newPacket, callbacks);
    }*/

}