package com.jowhjy.hidecoords.util;

import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Unique;

public interface HasAccessibleBlockPos {
    @Unique
    void hidecoords$setBlockPos(BlockPos pos);
}
