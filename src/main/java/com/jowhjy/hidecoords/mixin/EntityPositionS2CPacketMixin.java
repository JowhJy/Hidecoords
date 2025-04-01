package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.HasAccessibleCoordinates;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPositionS2CPacket.class)
public class EntityPositionS2CPacketMixin implements HasAccessibleCoordinates {

    @Shadow @Final private double z;
    @Shadow @Final private double y;
    @Shadow @Final private double x;
    @Unique
    double hidecoords$accessibleX;
    @Unique
    double hidecoords$accessibleY;
    @Unique
    double hidecoords$accessibleZ;

    @Inject(method = "<init>(Lnet/minecraft/entity/Entity;)V", at = @At("TAIL"))
    public void hidecoords$modifyConstructor(Entity entity, CallbackInfo ci)
    {
        hidecoords$accessibleX = this.x;
        hidecoords$accessibleY = this.y;
        hidecoords$accessibleZ = this.z;
    }

    @Override
    public void hidecoords$setX(double x) {
        hidecoords$accessibleX = x;
    }
    @Override
    public void hidecoords$setY(double y) {
        hidecoords$accessibleY = y;
    }@Override
    public void hidecoords$setZ(double z) {
        hidecoords$accessibleZ = z;
    }

    @ModifyReturnValue(method = "getX", at = @At("RETURN"))
    private double modifyGetX(double original)
    {
        return hidecoords$accessibleX;
    }
    @ModifyReturnValue(method = "getY", at = @At("RETURN"))
    private double modifyGetY(double original)
    {
        return hidecoords$accessibleY;
    }
    @ModifyReturnValue(method = "getZ", at = @At("RETURN"))
    private double modifyGetZ(double original)
    {
        return hidecoords$accessibleZ;
    }
}
