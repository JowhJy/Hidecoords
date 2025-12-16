package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.OffsetableTrackedWaypoint;
import net.minecraft.core.Vec3i;
import net.minecraft.world.waypoints.TrackedWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.waypoints.TrackedWaypoint$Vec3iWaypoint")
public abstract class PositionalTrackedWaypointMixin extends TrackedWaypoint implements OffsetableTrackedWaypoint {
    @Shadow private Vec3i vector;

    PositionalTrackedWaypointMixin() {
        super(null, null, null);

    }

    @Override
    public OffsetableTrackedWaypoint hidecoords$offset(Offset offset) {
        return (OffsetableTrackedWaypoint)TrackedWaypoint.setPosition(identifier.left().get(), this.icon(), new Vec3i(this.vector.getX() + offset.getX(), this.vector.getY(), this.vector.getZ() + offset.getZ()));
    }
}
