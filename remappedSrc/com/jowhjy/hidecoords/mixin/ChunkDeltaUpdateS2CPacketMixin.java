package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.IChunkDeltaUpdateS2CPacketMixin;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundSectionBlocksUpdatePacket.class)
public abstract class ChunkDeltaUpdateS2CPacketMixin implements IChunkDeltaUpdateS2CPacketMixin  {

    @Shadow @Final private SectionPos sectionPos;
    @Unique
    private ShortSet hidecoords$rememberPositions;
    @Unique
    private LevelChunkSection hidecoords$rememberSection;

    @Inject(method = "<init>(Lnet/minecraft/core/SectionPos;Lit/unimi/dsi/fastutil/shorts/ShortSet;Lnet/minecraft/world/level/chunk/LevelChunkSection;)V", at = @At("TAIL"))
    public void hidecoords$modifyConstructor(SectionPos sectionPos, ShortSet positions, LevelChunkSection section, CallbackInfo ci)
    {
        hidecoords$rememberPositions = positions;
        hidecoords$rememberSection = section;
    }

    @Unique @Override
    public ShortSet hideCoordinates$getRememberedPositions() {
        return hidecoords$rememberPositions;
    }
    @Unique @Override
    public LevelChunkSection hideCoordinates$getRememberedChunkSection() {
        return hidecoords$rememberSection;
    }
}
