package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.OffsetableTrackedWaypoint;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.waypoints.TrackedWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.waypoints.TrackedWaypoint$ChunkWaypoint")
public abstract class ChunkBasedTrackedWaypointMixin extends TrackedWaypoint implements OffsetableTrackedWaypoint {
    @Shadow private ChunkPos chunkPos;

    ChunkBasedTrackedWaypointMixin() {
        super(null, null, null);

    }

    @Override
    public OffsetableTrackedWaypoint hidecoords$offset(Offset offset) {
        return (OffsetableTrackedWaypoint)TrackedWaypoint.setChunk(identifier.left().get(), this.icon(), new ChunkPos(this.chunkPos.x + offset.getChunkX(), this.chunkPos.z + offset.getChunkZ()));
    }
}
