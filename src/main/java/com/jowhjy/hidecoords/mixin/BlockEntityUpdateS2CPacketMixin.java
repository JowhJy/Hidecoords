package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.HasAccessibleBlockPos;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityUpdateS2CPacket.class)
public abstract class BlockEntityUpdateS2CPacketMixin implements HasAccessibleBlockPos {

    @Unique
    private BlockPos hidecoords$accessibleBlockPos;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void hidecoords$modifyConstructor(BlockPos pos, BlockEntityType<?> blockEntityType, NbtCompound nbt, CallbackInfo ci)
    {
        hidecoords$accessibleBlockPos = pos;
    }

    @Unique @Override
    public void hidecoords$setBlockPos(BlockPos pos)
    {
        hidecoords$accessibleBlockPos = pos;
    }
    @ModifyReturnValue(method = "getPos", at = @At("RETURN"))
    private BlockPos modifyWrite(BlockPos original)
    {
        return hidecoords$accessibleBlockPos;
    }
}
