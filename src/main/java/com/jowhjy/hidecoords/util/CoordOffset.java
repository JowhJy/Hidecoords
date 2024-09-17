package com.jowhjy.hidecoords.util;

import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Unique;

public interface CoordOffset {
    @Unique
    public default BlockPos juhc$getCoordOffset() {
        return null;
    }
}
