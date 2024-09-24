package com.jowhjy.hidecoords.mixin;

import net.minecraft.network.packet.s2c.play.LightUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LightUpdateS2CPacket.class)
public interface LightUpdateS2CPacketAccessor {
    @Accessor("chunkX")
    void setChunkX(int chunkX);
    @Accessor("chunkZ")
    void setChunkZ(int chunkZ);
}
