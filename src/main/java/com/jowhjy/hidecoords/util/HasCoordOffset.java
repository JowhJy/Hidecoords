package com.jowhjy.hidecoords.util;

import com.jowhjy.hidecoords.Offset;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Unique;

public interface HasCoordOffset {
    @Unique
    public default Offset juhc$getCoordOffset() {
        return null;
    }
}
