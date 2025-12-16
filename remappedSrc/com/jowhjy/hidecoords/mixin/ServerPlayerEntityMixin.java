package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Hidecoords;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import com.jowhjy.hidecoords.util.IServerPlayerEntityMixin;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements IServerPlayerEntityMixin {

    @Shadow public ServerGamePacketListenerImpl connection;

    @Shadow public abstract ServerLevel level();

    @Unique boolean offsetActive = true;

    public ServerPlayerEntityMixin(Level world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setServerLevel(Lnet/minecraft/server/level/ServerLevel;)V"))
    public void hidecoords$changeOffsetOnDimensionChange(TeleportTransition teleportTarget, CallbackInfoReturnable<Entity> cir)
    {
        ((HasCoordOffset)(this.connection)).hidecoords$setCoordOffset(com.jowhjy.hidecoords.Offset.zeroAtLocation(BlockPos.containing(teleportTarget.position())), false);
    }

    //todo this is very inefficient!
    @Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At("TAIL"))
    public void hidecoords$changeBorderOnTeleport(TeleportTransition teleportTarget, CallbackInfoReturnable<Entity> cir)
    {
        if (!juhc$shouldOffset()) return;
        this.connection.send(new ClientboundSetBorderSizePacket(this.level().getWorldBorder()));
        this.connection.send(new ClientboundSetBorderCenterPacket(this.level().getWorldBorder()));
    }
    @Override
    public void travel(Vec3 movementInput)
    {
        super.travel(movementInput);
        if (!juhc$shouldOffset()) return;
        this.connection.send(new ClientboundSetBorderSizePacket(this.level().getWorldBorder()));
        this.connection.send(new ClientboundSetBorderCenterPacket(this.level().getWorldBorder()));
    }

    @Unique @Override
    public boolean juhc$shouldOffset()
    {
        return offsetActive && this.level().getGameRules().get(Hidecoords.HIDECOORDS_GAMERULE);
    }
    @Unique @Override
    public void juhc$setShouldOffset(boolean value)
    {
        offsetActive = value;
    }

}
