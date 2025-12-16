package com.jowhjy.hidecoords.util;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Unique;

public interface HasAccessibleBlockPos {
    @Unique
    void hidecoords$setBlockPos(BlockPos pos);
}
