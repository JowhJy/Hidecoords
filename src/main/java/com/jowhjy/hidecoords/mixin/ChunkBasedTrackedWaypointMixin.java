package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.OffsetableTrackedWaypoint;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.waypoint.TrackedWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.waypoint.TrackedWaypoint$ChunkBased")
public abstract class ChunkBasedTrackedWaypointMixin extends TrackedWaypoint implements OffsetableTrackedWaypoint {
    @Shadow private ChunkPos chunkPos;

    ChunkBasedTrackedWaypointMixin() {
        super(null, null, null);

    }

    @Override
    public OffsetableTrackedWaypoint hidecoords$offset(Offset offset) {
        return (OffsetableTrackedWaypoint)TrackedWaypoint.ofChunk(source.left().get(), this.getConfig(), new ChunkPos(this.chunkPos.x + offset.getChunkX(), this.chunkPos.z + offset.getChunkZ()));
    }
}
