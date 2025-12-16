package com.jowhjy.hidecoords.util;

import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Unique;

public interface IChunkDeltaUpdateS2CPacketMixin {
    @Unique
    default ShortSet hideCoordinates$getRememberedPositions() {return null;}
    @Unique
    default LevelChunkSection hideCoordinates$getRememberedChunkSection() {return null;}
}
