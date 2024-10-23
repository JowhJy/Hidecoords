package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    @Shadow public ServerPlayNetworkHandler networkHandler;

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Shadow public abstract ServerWorld getServerWorld();

    @Inject(method = "teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/server/network/ServerPlayerEntity;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setServerWorld(Lnet/minecraft/server/world/ServerWorld;)V"))
    public void juhc$changeOffsetOnDimensionChange(TeleportTarget teleportTarget, CallbackInfoReturnable<Entity> cir)
    {
        ((HasCoordOffset)(this.networkHandler)).juhc$setCoordOffset(Offset.zeroAtLocation(BlockPos.ofFloored(teleportTarget.position())));
    }

    //todo this is very inefficient!
    @Inject(method = "teleportTo(Lnet/minecraft/world/TeleportTarget;)Lnet/minecraft/server/network/ServerPlayerEntity;", at = @At("TAIL"))
    public void juhc$changeBorderOnTeleport(TeleportTarget teleportTarget, CallbackInfoReturnable<Entity> cir)
    {
        this.networkHandler.sendPacket(new WorldBorderSizeChangedS2CPacket(this.getServerWorld().getWorldBorder()));
        this.networkHandler.sendPacket(new WorldBorderCenterChangedS2CPacket(this.getServerWorld().getWorldBorder()));
    }
    @Override
    public void travel(Vec3d movementInput)
    {
        super.travel(movementInput);
        this.networkHandler.sendPacket(new WorldBorderSizeChangedS2CPacket(this.getServerWorld().getWorldBorder()));
        this.networkHandler.sendPacket(new WorldBorderCenterChangedS2CPacket(this.getServerWorld().getWorldBorder()));
    }

}
