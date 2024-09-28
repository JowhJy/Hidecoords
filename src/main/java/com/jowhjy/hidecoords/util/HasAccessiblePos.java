package com.jowhjy.hidecoords.util;

import org.spongepowered.asm.mixin.Unique;

public interface HasAccessiblePos {

    @Unique
    void juhc$setChunkX(int chunkX);
    @Unique
    void juhc$setChunkZ(int chunkZ);

}
