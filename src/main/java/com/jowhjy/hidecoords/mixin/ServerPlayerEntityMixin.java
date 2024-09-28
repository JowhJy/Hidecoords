package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.WorldBorderCenterChangedS2CPacket;
import net.minecraft.network.packet.s2c.play.WorldBorderSizeChangedS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Shadow public ServerPlayNetworkHandler networkHandler;

    @Shadow public abstract ServerWorld getServerWorld();

    @Inject(method = "teleportTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setServerWorld(Lnet/minecraft/server/world/ServerWorld;)V"))
    public void juhc$changeOffsetOnDimensionChange(TeleportTarget teleportTarget, CallbackInfoReturnable<Entity> cir)
    {
        ((HasCoordOffset)(this.networkHandler)).juhc$setCoordOffset(Offset.zeroAtLocation(BlockPos.ofFloored(teleportTarget.pos())));
    }

    //todo this is very inefficient!
    @Inject(method = "teleportTo", at = @At("TAIL"))
    public void juhc$changeBorderOnTeleport(TeleportTarget teleportTarget, CallbackInfoReturnable<Entity> cir)
    {
        this.networkHandler.sendPacket(new WorldBorderSizeChangedS2CPacket(this.getServerWorld().getWorldBorder()));
        this.networkHandler.sendPacket(new WorldBorderCenterChangedS2CPacket(this.getServerWorld().getWorldBorder()));
    }
    @Inject(method = "travel", at = @At("TAIL"))
    public void juhc$changeBorderOnMovement(Vec3d movementInput, CallbackInfo ci)
    {
        this.networkHandler.sendPacket(new WorldBorderSizeChangedS2CPacket(this.getServerWorld().getWorldBorder()));
        this.networkHandler.sendPacket(new WorldBorderCenterChangedS2CPacket(this.getServerWorld().getWorldBorder()));
    }

}
