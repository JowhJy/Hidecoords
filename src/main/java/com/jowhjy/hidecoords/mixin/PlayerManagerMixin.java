package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    /**
     * Reroll the coordinate offset on respawn
     * @param instance
     * @param oldPlayer
     * @param alive
     */
    @Redirect(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;copyFrom(Lnet/minecraft/server/network/ServerPlayerEntity;Z)V"))
    public void hidecoords$newOffsetOnRespawn(ServerPlayerEntity instance, ServerPlayerEntity oldPlayer, boolean alive)
    {
        ((HasCoordOffset)(instance.networkHandler)).hidecoords$setCoordOffset(Offset.zeroAtLocation(instance.getBlockPos()));
    }
}
