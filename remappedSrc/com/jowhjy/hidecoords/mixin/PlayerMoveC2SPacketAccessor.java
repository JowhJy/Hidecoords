package com.jowhjy.hidecoords.mixin;

import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundMovePlayerPacket.class)
public interface PlayerMoveC2SPacketAccessor {

    @Accessor("x")
    @Mutable
    double getX();

    @Accessor("y")
    @Mutable
    double getY();

    @Accessor("z")
    @Mutable
    double getZ();

    @Accessor("yRot")
    @Mutable
    float getYaw();
    
    @Accessor("xRot")
    @Mutable
    float getPitch();
}
