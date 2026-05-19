package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Hidecoords;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import com.jowhjy.hidecoords.util.IServerPlayerEntityMixin;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetBorderCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetBorderSizePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player implements IServerPlayerEntityMixin {

    @Shadow public ServerGamePacketListenerImpl connection;

    @Shadow public abstract ServerLevel level();

    @Shadow
    public abstract PermissionSet permissions();

    @Unique boolean offsetActive = true;

    public ServerPlayerEntityMixin(Level world, GameProfile gameProfile) {
        super(world, gameProfile);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void hidecoords$dontOffsetForOps(MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation, CallbackInfo ci)
    {
        if (this.permissions().hasPermission(Permissions.COMMANDS_ADMIN)) offsetActive = false; //admin level permission doesn't get offset by default
    }

    @Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setServerLevel(Lnet/minecraft/server/level/ServerLevel;)V"))
    public void hidecoords$changeOffsetOnDimensionChange(TeleportTransition transition, CallbackInfoReturnable<Entity> cir)
    {
        ((HasCoordOffset)(this.connection)).hidecoords$pickNewOffset(false, BlockPos.containing(transition.position()));
    }

    //todo this is very inefficient!
    @Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At("TAIL"))
    public void hidecoords$changeBorderOnTeleport(TeleportTransition transition, CallbackInfoReturnable<Entity> cir)
    {
        if (hidecoords$shouldOffset()) return;
        this.connection.send(new ClientboundSetBorderSizePacket(this.level().getWorldBorder()));
        this.connection.send(new ClientboundSetBorderCenterPacket(this.level().getWorldBorder()));
    }
    @Override
    public void travel(@NonNull Vec3 movementInput)
    {
        super.travel(movementInput);
        if (hidecoords$shouldOffset()) return;
        this.connection.send(new ClientboundSetBorderSizePacket(this.level().getWorldBorder()));
        this.connection.send(new ClientboundSetBorderCenterPacket(this.level().getWorldBorder()));
    }

    @Unique @Override
    public boolean hidecoords$shouldOffset()
    {
        return !offsetActive || !this.level().getGameRules().get(Hidecoords.HIDECOORDS_GAMERULE);
    }
    @Unique @Override
    public void hidecoords$setShouldOffset(boolean value)
    {
        offsetActive = value;
    }

}
