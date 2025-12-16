package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.OffsetableTrackedWaypoint;
import net.minecraft.world.waypoints.TrackedWaypoint;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TrackedWaypoint.class)
public class TrackedWaypointMixin implements OffsetableTrackedWaypoint {
    @Override
    public OffsetableTrackedWaypoint hidecoords$offset(Offset offset) {
        return this;
    }
}
