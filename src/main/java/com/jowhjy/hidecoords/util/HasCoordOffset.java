package com.jowhjy.hidecoords.util;

import com.jowhjy.hidecoords.Offset;
import org.spongepowered.asm.mixin.Unique;

public interface HasCoordOffset {
    @Unique
    public default Offset hidecoords$getCoordOffset() {
        return null;
    }

    @Unique
    default void hidecoords$setCoordOffset(Offset offset, boolean resendData) {

    }
}
