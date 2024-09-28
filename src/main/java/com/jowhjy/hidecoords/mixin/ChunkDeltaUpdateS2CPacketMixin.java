package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.HasAccessiblePos;
import it.unimi.dsi.fastutil.shorts.ShortSet;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.ChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkDeltaUpdateS2CPacket.class)
public class ChunkDeltaUpdateS2CPacketMixin implements HasAccessiblePos {

    @Unique
    ChunkSectionPos juhc$accessibleSectionPos;

    @Inject(method = "<init>(Lnet/minecraft/util/math/ChunkSectionPos;Lit/unimi/dsi/fastutil/shorts/ShortSet;Lnet/minecraft/world/chunk/ChunkSection;)V", at = @At("TAIL"))
    public void juhc$modifyConstructor(ChunkSectionPos sectionPos, ShortSet positions, ChunkSection section, CallbackInfo ci)
    {
        juhc$accessibleSectionPos = sectionPos;
    }

    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;writeLong(J)Lnet/minecraft/network/PacketByteBuf;"))
    public long juhc$changeSectionPos(long l)
    {
        return juhc$accessibleSectionPos.asLong();
    }

    @Override
    public void juhc$setChunkX(int chunkX) {
        juhc$accessibleSectionPos = ChunkSectionPos.from(chunkX, juhc$accessibleSectionPos.getSectionY(), juhc$accessibleSectionPos.getSectionZ());
    }

    @Override
    public void juhc$setChunkZ(int chunkZ) {
        juhc$accessibleSectionPos = ChunkSectionPos.from(juhc$accessibleSectionPos.getSectionX(), juhc$accessibleSectionPos.getSectionY(), chunkZ);

    }
}
