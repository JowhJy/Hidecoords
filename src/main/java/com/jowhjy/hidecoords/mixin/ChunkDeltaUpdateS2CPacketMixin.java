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
    ChunkSectionPos hidecoords$accessibleSectionPos;

    @Inject(method = "<init>(Lnet/minecraft/util/math/ChunkSectionPos;Lit/unimi/dsi/fastutil/shorts/ShortSet;Lnet/minecraft/world/chunk/ChunkSection;)V", at = @At("TAIL"))
    public void hidecoords$modifyConstructor(ChunkSectionPos sectionPos, ShortSet positions, ChunkSection section, CallbackInfo ci)
    {
        hidecoords$accessibleSectionPos = sectionPos;
    }

    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;writeLong(J)Lnet/minecraft/network/PacketByteBuf;"))
    public long hidecoords$changeSectionPos(long l)
    {
        return hidecoords$accessibleSectionPos.asLong();
    }

    @Override
    public void hidecoords$setChunkX(int chunkX) {
        hidecoords$accessibleSectionPos = ChunkSectionPos.from(chunkX, hidecoords$accessibleSectionPos.getSectionY(), hidecoords$accessibleSectionPos.getSectionZ());
    }

    @Override
    public void hidecoords$setChunkZ(int chunkZ) {
        hidecoords$accessibleSectionPos = ChunkSectionPos.from(hidecoords$accessibleSectionPos.getSectionX(), hidecoords$accessibleSectionPos.getSectionY(), chunkZ);

    }
}
