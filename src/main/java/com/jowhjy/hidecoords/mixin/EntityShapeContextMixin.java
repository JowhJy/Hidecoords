package com.jowhjy.hidecoords.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EntityShapeContext;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityShapeContext.class)
public class EntityShapeContextMixin {
    @Shadow
    @Final
    private Entity entity;

    @WrapOperation(method = "getCollisionShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"))
    public VoxelShape hidecoords$playerNoCollideWithBambooOrDripstone(BlockState instance, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext, Operation<VoxelShape> original) {
        return
                (this.entity instanceof ServerPlayerEntity && (instance.isOf(Blocks.BAMBOO_BLOCK) || instance.isOf(Blocks.POINTED_DRIPSTONE)))
                        ? VoxelShapes.empty()
                        : original.call(instance, blockView, blockPos, shapeContext);

    }
}
