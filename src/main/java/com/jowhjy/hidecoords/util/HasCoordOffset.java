package com.jowhjy.hidecoords.util;

import com.jowhjy.hidecoords.Offset;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Unique;

public interface HasCoordOffset {
    @Unique
    public default Offset hidecoords$getCoordOffset() {
        return null;
    }

    @Unique
    default void hidecoords$setCoordOffset(Offset offset, boolean resendData) {

    }
    @Unique
    default void hidecoords$pickNewOffset(boolean resendData, BlockPos position) {

    }
}
