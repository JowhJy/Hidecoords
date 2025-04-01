package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.IEntityShapeContextMixin;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityShapeContext.class)
public class EntityShapeContextMixin implements IEntityShapeContextMixin {
    @Shadow
    @Final
    private Entity entity;

    @Unique @Override
    public Entity hidecoords$getEntity()
    {
        return entity;
    }
}
