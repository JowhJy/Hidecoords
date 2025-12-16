package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Hidecoords;
import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPlayNetworkHandlerMixin extends ServerCommonPacketListenerImpl implements HasCoordOffset {

    @Shadow public abstract ServerPlayer getPlayer();

    @Shadow private double firstGoodX;
    @Unique
    Offset hidecoords$coordOffset;

    public ServerPlayNetworkHandlerMixin(MinecraftServer server, Connection connection, CommonListenerCookie clientData, double lastTickX) {
        super(server, connection, clientData);
        this.firstGoodX = lastTickX;
    }

    @Unique
    @Override
    public Offset hidecoords$getCoordOffset()
    {
        return hidecoords$coordOffset;
    }
    @Unique
    @Override
    public void hidecoords$setCoordOffset(Offset coordOffset, boolean resendData)
    {
        hidecoords$coordOffset = coordOffset;
        if (resendData) Hidecoords.resendDataAfterOffsetChange(this.getPlayer());
    }

    /** Inject into the constructor to make the offsetPacket
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void hidecoords$createOffset(MinecraftServer server, Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci){

        hidecoords$coordOffset = Offset.zeroAtLocation(player.blockPosition());

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