package com.jowhjy.hidecoords.mixin;

import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VehicleMoveS2CPacket.class)
public interface VehicleMoveS2CPacketAccessor {
    @Accessor("x")
    void setX(double x);
    @Accessor("z")
    void setZ(double z);
}
