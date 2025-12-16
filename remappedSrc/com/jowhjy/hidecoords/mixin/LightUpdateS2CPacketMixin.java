package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.util.HasAccessibleChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;
import net.minecraft.network.protocol.game.ClientboundLightUpdatePacket;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.lighting.LevelLightEngine;

@Mixin(ClientboundLightUpdatePacket.class)
public class LightUpdateS2CPacketMixin implements HasAccessibleChunkPos {

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

    @Inject(method = "<init>(Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/lighting/LevelLightEngine;Ljava/util/BitSet;Ljava/util/BitSet;)V", at = @At("TAIL"))
    public void hidecoords$modifyConstructor(ChunkPos chunkPos, LevelLightEngine lightProvider, BitSet skyBits, BitSet blockBits, CallbackInfo ci)
    {
        hidecoords$accessibleChunkX = chunkPos.x;
        hidecoords$accessibleChunkZ = chunkPos.z;
    }

    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeVarInt(I)Lnet/minecraft/network/FriendlyByteBuf;", ordinal = 0))
    private int modifyWriteX(int par1)
    {
        return hidecoords$accessibleChunkX;
    }
    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;writeVarInt(I)Lnet/minecraft/network/FriendlyByteBuf;", ordinal = 1))
    private int modifyWriteZ(int par1)
    {
        return hidecoords$accessibleChunkZ;
    }
}
