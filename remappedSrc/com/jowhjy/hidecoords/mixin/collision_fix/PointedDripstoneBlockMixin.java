package com.jowhjy.hidecoords.mixin.collision_fix;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PointedDripstoneBlock.class)
public abstract class PointedDripstoneBlockMixin extends BlockBehaviour {
    @Unique
    private static final VoxelShape TOP_COLLISION_SHAPE = Block.column(16.0, 15.9, 16.0);
    @Unique
    private static final VoxelShape TOP_COLLISION_SHAPE_TIP_UP = Block.column(16.0, 10.9, 11.0);

    public PointedDripstoneBlockMixin(Properties settings) {
        super(settings);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context)
    {
        /*VoxelShape collisionShape;

        if (state.get(PointedDripstoneBlock.THICKNESS) == Thickness.TIP
                && state.get(PointedDripstoneBlock.VERTICAL_DIRECTION) != Direction.DOWN) {
            collisionShape = TOP_COLLISION_SHAPE_TIP_UP;
        }
        else collisionShape = TOP_COLLISION_SHAPE;

        if (!context.isAbove(collisionShape, pos, true))*/ return Shapes.empty();
        //else return collisionShape;
    }


}
