package com.jowhjy.hidecoords.util;

import org.spongepowered.asm.mixin.Unique;

public interface IServerPlayerEntityMixin {

    @Unique
    default boolean hidecoords$shouldOffset()
    {
        return true;
    }

    @Unique
    default void hidecoords$setShouldOffset(boolean b) {

    }
}
