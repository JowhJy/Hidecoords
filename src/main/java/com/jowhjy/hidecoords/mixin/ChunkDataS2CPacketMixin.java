package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.HasAccessibleChunkPos;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.light.LightingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;

@Mixin(ChunkDataS2CPacket.class)
public abstract class ChunkDataS2CPacketMixin implements HasAccessibleChunkPos {

    @Unique int hidecoords$accessibleChunkX;
    @Unique int hidecoords$accessibleChunkZ;

    @Override
    public void hidecoords$setChunkX(int chunkX) {
        hidecoords$accessibleChunkX = chunkX;
    }

    @Override
    public void hidecoords$setChunkZ(int chunkZ) {
        hidecoords$accessibleChunkZ = chunkZ;
    }

    @Inject(method = "<init>(Lnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/chunk/light/LightingProvider;Ljava/util/BitSet;Ljava/util/BitSet;)V", at = @At("TAIL"))
    public void hidecoords$modifyConstructor(WorldChunk chunk, LightingProvider lightProvider, BitSet skyBits, BitSet blockBits, CallbackInfo ci)
    {
        hidecoords$accessibleChunkX = chunk.getPos().x;
        hidecoords$accessibleChunkZ = chunk.getPos().z;
    }

    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/RegistryByteBuf;writeInt(I)Lnet/minecraft/network/PacketByteBuf;", ordinal = 0))
    private int modifyWriteX(int par1)
    {
        return hidecoords$accessibleChunkX;
    }
    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/RegistryByteBuf;writeInt(I)Lnet/minecraft/network/PacketByteBuf;", ordinal = 1))
    private int modifyWriteZ(int par1)
    {
        return hidecoords$accessibleChunkZ;
    }
}
