package com.jowhjy.hidecoords.mixin.collision_fix;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.block.enums.Thickness;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PointedDripstoneBlock.class)
public abstract class PointedDripstoneBlockMixin extends AbstractBlock {
    @Unique
    private static final VoxelShape TOP_COLLISION_SHAPE = Block.createColumnShape(16.0, 15.9, 16.0);
    @Unique
    private static final VoxelShape TOP_COLLISION_SHAPE_TIP_UP = Block.createColumnShape(16.0, 10.9, 11.0);

    public PointedDripstoneBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
    {
        /*VoxelShape collisionShape;

        if (state.get(PointedDripstoneBlock.THICKNESS) == Thickness.TIP
                && state.get(PointedDripstoneBlock.VERTICAL_DIRECTION) != Direction.DOWN) {
            collisionShape = TOP_COLLISION_SHAPE_TIP_UP;
        }
        else collisionShape = TOP_COLLISION_SHAPE;

        if (!context.isAbove(collisionShape, pos, true))*/ return VoxelShapes.empty();
        //else return collisionShape;
    }


}
