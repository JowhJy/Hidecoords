package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Hidecoords;
import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.C2SPacketOffsetter;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

    @Inject(method = "handlePacket", at =@At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void hidecoords$offsetIncomingPacket(Packet<T> packet, PacketListener listener, CallbackInfo ci)
    {

        if (listener instanceof ServerPlayNetworkHandler serverPlayNetworkHandler) {
            //dont offset if gamerule off
            if (!serverPlayNetworkHandler.getPlayer().getServerWorld().getGameRules().getBoolean(Hidecoords.HIDECOORDS_GAMERULE)) return;

            Offset offset = ((HasCoordOffset)serverPlayNetworkHandler).hidecoords$getCoordOffset();
            Packet<ServerPlayPacketListener> newPacket = C2SPacketOffsetter.offsetPacket(packet, offset);
            newPacket.apply((ServerPlayNetworkHandler) listener);
            ci.cancel();
        }
    }

}
