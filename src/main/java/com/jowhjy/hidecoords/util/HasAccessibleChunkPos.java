package com.jowhjy.hidecoords.util;

import org.spongepowered.asm.mixin.Unique;

public interface HasAccessibleChunkPos {

    @Unique
    void hidecoords$setChunkX(int chunkX);
    @Unique
    void hidecoords$setChunkZ(int chunkZ);

}
