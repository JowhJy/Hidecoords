package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.CoordOffset;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerCommonNetworkHandler;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin extends ServerCommonNetworkHandler implements CoordOffset {
    @Unique
    BlockPos coordOffset;

    public ServerPlayNetworkHandlerMixin(MinecraftServer server, ClientConnection connection, ConnectedClientData clientData) {
        super(server, connection, clientData);
    }

    @Unique
    @Override
    public BlockPos juhc$getCoordOffset()
    {
        return coordOffset;
    }

    /** Inject into the constructor to make the offset
     */
    @Inject(method = "<init>", at = @At("TAIL"))
    public void juhc$createOffset(MinecraftServer server, ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci){

        coordOffset = player.getBlockPos().multiply(-1);

    }

    @Override
    public void send(Packet<?> packet, @Nullable PacketCallbacks callbacks)
    {
        System.out.println(packet);

        super.send(packet, callbacks);
    }

}