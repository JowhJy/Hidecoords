package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Hidecoords;
import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import com.jowhjy.hidecoords.util.IServerPlayerEntityMixin;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.WorldBorderCenterChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderSizeChangedS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity implements IServerPlayerEntityMixin {

    @Shadow public ServerPlayNetworkHandler networkHandler;

    @Shadow public abstract ServerWorld getWorld();

    @Unique boolean offsetActive = true;

    public ServerPlayerEntityMixin(World world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Inject(method = "teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/server/network/ServerPlayerEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setServerWorld(Lnet/minecraft/server/world/ServerWorld;)V"))
    public void hidecoords$changeOffsetOnDimensionChange(TeleportTarget teleportTarget, CallbackInfoReturnable<Entity> cir)
    {
        ((HasCoordOffset)(this.networkHandler)).hidecoords$setCoordOffset(Offset.zeroAtLocation(BlockPos.ofFloored(teleportTarget.position())), false);
    }

    //todo this is very inefficient!
    @Inject(method = "teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/server/network/ServerPlayerEntity;", at = @At("TAIL"))
    public void hidecoords$changeBorderOnTeleport(TeleportTarget teleportTarget, CallbackInfoReturnable<Entity> cir)
    {
        if (!juhc$shouldOffset()) return;
        this.networkHandler.sendPacket(new WorldBorderSizeChangedS2CPacket(this.getWorld().getWorldBorder()));
        this.networkHandler.sendPacket(new WorldBorderCenterChangedS2CPacket(this.getWorld().getWorldBorder()));
    }
    @Override
    public void travel(Vec3d movementInput)
    {
        super.travel(movementInput);
        if (!juhc$shouldOffset()) return;
        this.networkHandler.sendPacket(new WorldBorderSizeChangedS2CPacket(this.getWorld().getWorldBorder()));
        this.networkHandler.sendPacket(new WorldBorderCenterChangedS2CPacket(this.getWorld().getWorldBorder()));
    }

    @Unique @Override
    public boolean juhc$shouldOffset()
    {
        return offsetActive && this.getWorld().getGameRules().getBoolean(Hidecoords.HIDECOORDS_GAMERULE);
    }
    @Unique @Override
    public void juhc$setShouldOffset(boolean value)
    {
        offsetActive = value;
    }

}
