package com.jowhjy.hidecoords;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

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


}
