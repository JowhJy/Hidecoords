package com.jowhjy.hidecoords.util;

import org.spongepowered.asm.mixin.Unique;

public interface IServerPlayerEntityMixin {

    @Unique
    default boolean juhc$shouldOffset()
    {
        return false;
    }

    @Unique
    default void juhc$setShouldOffset(boolean b) {

    }
}
