package com.jowhjy.hidecoords.mixin.collision_fix;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BambooStalkBlock.class)
public class BambooBlockMixin {
    @Unique
    private static final VoxelShape TOP_COLLISION_SHAPE = Block.column(16.0, 15.9, 16.0);

    @Inject(method = "getCollisionShape", at = @At(value = "HEAD"), cancellable = true)
    public void hidecoords$changeBambooCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir)
    {
        /*if (context.isAbove(TOP_COLLISION_SHAPE, pos, true)) cir.setReturnValue(TOP_COLLISION_SHAPE);
        else */cir.setReturnValue(Shapes.empty());
    }


}
