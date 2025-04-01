package com.jowhjy.hidecoords.util;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Unique;

public interface IEntityShapeContextMixin {
    @Unique
    default Entity hidecoords$getEntity() {
        return null;
    }
}
