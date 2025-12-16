package com.jowhjy.hidecoords;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.NotNull;

public class Offset {
    BlockPos blockPos;
    ChunkPos chunkPos;

    public Offset(BlockPos blockPos)
    {
        this.blockPos = blockPos;
        this.chunkPos = new ChunkPos(blockPos);
    }

    public int getX()
    {
        return blockPos.getX();
    }

    public int getZ()
    {
        return blockPos.getZ();
    }

    public int getChunkX()
    {
        return chunkPos.x;
    }

    public int getChunkZ()
    {
        return chunkPos.z;
    }

    public BlockPos getBlockPos()
    {
        return blockPos;
    }

    public ChunkPos getChunkPos()
    {
        return chunkPos;
    }

    public static BlockPos align(BlockPos blockPos) {

        // Add half of the divisor so that the output is rounded instead of just floored
        int x = blockPos.getX() + 8;
        int z = blockPos.getZ() + 8;

        return new BlockPos(x >> 4 << 4, blockPos.getY(), z >> 4 << 4);
    }

    public static Offset zeroAtLocation(BlockPos blockPos)
    {
        return new Offset(align(blockPos.multiply(-1)));
    }


}
