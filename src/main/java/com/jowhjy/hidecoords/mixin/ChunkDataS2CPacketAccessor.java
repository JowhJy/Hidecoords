package com.jowhjy.hidecoords.mixin;

import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkDataS2CPacket.class)
public interface ChunkDataS2CPacketAccessor{

    @Accessor("chunkX")
    void setChunkX(int chunkX);
    @Accessor("chunkZ")
    void setChunkZ(int chunkZ);

}
