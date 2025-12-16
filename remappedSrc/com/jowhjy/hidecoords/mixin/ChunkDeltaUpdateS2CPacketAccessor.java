package com.jowhjy.hidecoords.mixin;

import net.minecraft.core.SectionPos;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundSectionBlocksUpdatePacket.class)
public interface ChunkDeltaUpdateS2CPacketAccessor {

    @Accessor("sectionPos")
    @Mutable
    SectionPos getSectionPos();
}
