package com.jowhjy.hidecoords.mixin;

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
    private static <T extends PacketListener> void juhc$offsetIncomingPacket(Packet<T> packet, PacketListener listener, CallbackInfo ci)
    {
        if (listener instanceof ServerPlayNetworkHandler serverPlayNetworkHandler) {
            Offset offset = ((HasCoordOffset)serverPlayNetworkHandler).juhc$getCoordOffset();
            Packet<ServerPlayPacketListener> newPacket = C2SPacketOffsetter.offsetPacket(packet, offset);
            //if (newPacket instanceof PlayerMoveC2SPacket.PositionAndOnGround blub) System.out.println(blub.getX(0));
            newPacket.apply((ServerPlayNetworkHandler) listener);
            ci.cancel();
        }
    }

}
