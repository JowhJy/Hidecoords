package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.IChunkDeltaUpdateS2CPacketMixin;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkDeltaUpdateS2CPacket.class)
public abstract class ChunkDeltaUpdateS2CPacketMixin implements IChunkDeltaUpdateS2CPacketMixin  {

    @Shadow @Final private ChunkSectionPos sectionPos;
    @Unique
    private ShortSet hidecoords$rememberPositions;
    @Unique
    private ChunkSection hidecoords$rememberSection;

    @Inject(method = "<init>(Lnet/minecraft/util/math/ChunkSectionPos;Lit/unimi/dsi/fastutil/shorts/ShortSet;Lnet/minecraft/world/chunk/ChunkSection;)V", at = @At("TAIL"))
    public void hidecoords$modifyConstructor(ChunkSectionPos sectionPos, ShortSet positions, ChunkSection section, CallbackInfo ci)
    {
        hidecoords$rememberPositions = positions;
        hidecoords$rememberSection = section;
    }

    @Unique @Override
    public ShortSet hideCoordinates$getRememberedPositions() {
        return hidecoords$rememberPositions;
    }
    @Unique @Override
    public ChunkSection hideCoordinates$getRememberedChunkSection() {
        return hidecoords$rememberSection;
    }
}
