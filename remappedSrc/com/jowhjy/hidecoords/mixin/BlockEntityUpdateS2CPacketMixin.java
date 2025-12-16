package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.HasAccessibleBlockPos;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundBlockEntityDataPacket.class)
public abstract class BlockEntityUpdateS2CPacketMixin implements HasAccessibleBlockPos {

    @Shadow @Final private BlockPos pos;
    @Unique
    private BlockPos hidecoords$accessibleBlockPos;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void hidecoords$modifyConstructor(BlockPos pos, BlockEntityType<?> blockEntityType, CompoundTag nbt, CallbackInfo ci)
    {
        hidecoords$accessibleBlockPos = pos;
    }

    @Unique @Override
    public void hidecoords$setBlockPos(BlockPos pos)
    {
        hidecoords$accessibleBlockPos = pos;
    }
    @ModifyReturnValue(method = "getPos", at = @At("RETURN"))
    private BlockPos modifyWriteZ(BlockPos original)
    {
        return hidecoords$accessibleBlockPos;
    }
}
