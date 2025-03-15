package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Hidecoords;
import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import net.minecraft.network.ClientConnection;
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

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandler implements HasCoordOffset {

    @Shadow public abstract ServerPlayerEntity getPlayer();

    @Shadow private double lastTickX;
    @Unique
    Offset hidecoords$coordOffset;

    public ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData, double lastTickX) {
        super(server, connection, clientData);
        this.lastTickX = lastTickX;
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
        Hidecoords.resendChunks(this.getPlayer());
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