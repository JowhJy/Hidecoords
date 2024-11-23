package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.HasAccessiblePos;
import net.minecraft.network.packet.s2c.play.LightUpdateS2CPacket;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;

@Mixin(LightUpdateS2CPacket.class)
public class LightUpdateS2CPacketMixin implements HasAccessiblePos {

    @Unique
    int hidecoords$accessibleChunkX;

    @Unique
    int hidecoords$accessibleChunkZ;

    @Override
    public void hidecoords$setChunkX(int chunkX) {
        hidecoords$accessibleChunkX = chunkX;
    }

    @Override
    public void hidecoords$setChunkZ(int chunkZ) {
        hidecoords$accessibleChunkZ = chunkZ;
    }

    @Inject(method = "<init>(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/world/chunk/light/LightingProvider;Ljava/util/BitSet;Ljava/util/BitSet;)V", at = @At("TAIL"))
    public void hidecoords$modifyConstructor(ChunkPos chunkPos, LightingProvider lightProvider, BitSet skyBits, BitSet blockBits, CallbackInfo ci)
    {
        hidecoords$accessibleChunkX = chunkPos.x;
        hidecoords$accessibleChunkZ = chunkPos.z;
    }

    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;", ordinal = 0))
    private int modifyWriteX(int par1)
    {
        return hidecoords$accessibleChunkX;
    }
    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/PacketByteBuf;writeVarInt(I)Lnet/minecraft/network/PacketByteBuf;", ordinal = 1))
    private int modifyWriteZ(int par1)
    {
        return hidecoords$accessibleChunkZ;
    }
}
