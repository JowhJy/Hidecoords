package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Hidecoords;
import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.C2SPacketOffsetter;
import com.jowhjy.hidecoords.S2CPacketOffsetter;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Queue;
import java.util.function.Consumer;

@Mixin(ClientConnection.class)
public abstract class ClientConnectionMixin {

    @Shadow private volatile @Nullable PacketListener packetListener;

    @Shadow @Final private Queue<Consumer<ClientConnection>> queuedTasks;

    @Shadow public abstract void send(Packet<?> packet, @Nullable PacketCallbacks callbacks, boolean flush);

    @Inject(method = "handlePacket", at =@At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void hidecoords$offsetIncomingPacket(Packet<T> packet, PacketListener listener, CallbackInfo ci)
    {

        if (listener instanceof ServerPlayNetworkHandler serverPlayNetworkHandler) {
            //dont offset if gamerule off
            if (!serverPlayNetworkHandler.getPlayer().getServerWorld().getGameRules().getBoolean(Hidecoords.HIDECOORDS_GAMERULE)) return;

            Offset offset = ((HasCoordOffset)serverPlayNetworkHandler).hidecoords$getCoordOffset();
            Packet<ServerPlayPacketListener> newPacket = C2SPacketOffsetter.offsetPacket(packet, offset);
            newPacket.apply((ServerPlayNetworkHandler) listener);
            ci.cancel();        }
    }

    @ModifyVariable(method = "sendInternal", at = @At("HEAD"), argsOnly = true)
    public Packet<?> juhc$offsetOutgoingPacket(Packet<?> packet) {
        if (this.packetListener instanceof  ServerPlayNetworkHandler serverPlayNetworkHandler) {
            if (!serverPlayNetworkHandler.getPlayer().getServerWorld().getGameRules().getBoolean(Hidecoords.HIDECOORDS_GAMERULE)) return packet;
            Offset offset = ((HasCoordOffset) serverPlayNetworkHandler).hidecoords$getCoordOffset();
            return S2CPacketOffsetter.offsetPacket(packet, offset, serverPlayNetworkHandler.getPlayer().getServerWorld());
        }
        return packet;
    }

}
