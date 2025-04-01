package com.jowhjy.hidecoords.util;

import org.spongepowered.asm.mixin.Unique;

public interface HasAccessibleCoordinates {
    @Unique
    void hidecoords$setX(double x);
    @Unique
    void hidecoords$setY(double y);
    @Unique
    void hidecoords$setZ(double z);
}
