package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerList.class)
public class PlayerManagerMixin {

    /**
     * Reroll the coordinate offset on respawn
     * @param instance
     * @param oldPlayer
     * @param alive
     */
    @Redirect(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;restoreFrom(Lnet/minecraft/server/level/ServerPlayer;Z)V"))
    public void hidecoords$newOffsetOnRespawn(ServerPlayer instance, ServerPlayer oldPlayer, boolean alive, @Local TeleportTransition teleportTarget)
    {
        ((HasCoordOffset)(instance.connection)).hidecoords$setCoordOffset(Offset.zeroAtLocation(BlockPos.containing(teleportTarget.position())), true);
        instance.restoreFrom(oldPlayer, alive);
    }
}
