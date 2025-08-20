package com.jowhjy.hidecoords.mixin.collision_fix;

import net.minecraft.block.BambooBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BambooBlock.class)
public class BambooBlockMixin {
    @Unique
    private static final VoxelShape TOP_COLLISION_SHAPE = Block.createColumnShape(16.0, 15.9, 16.0);

    @Inject(method = "getCollisionShape", at = @At(value = "HEAD"), cancellable = true)
    public void hidecoords$changeBambooCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir)
    {
        /*if (context.isAbove(TOP_COLLISION_SHAPE, pos, true)) cir.setReturnValue(TOP_COLLISION_SHAPE);
        else */cir.setReturnValue(VoxelShapes.empty());
    }


}
