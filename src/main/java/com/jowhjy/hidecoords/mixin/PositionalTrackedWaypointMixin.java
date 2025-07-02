package com.jowhjy.hidecoords.mixin;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.OffsetableTrackedWaypoint;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.waypoint.TrackedWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.waypoint.TrackedWaypoint$Positional")
public abstract class PositionalTrackedWaypointMixin extends TrackedWaypoint implements OffsetableTrackedWaypoint {
    @Shadow private Vec3i pos;

    PositionalTrackedWaypointMixin() {
        super(null, null, null);

    }

    @Override
    public OffsetableTrackedWaypoint hidecoords$offset(Offset offset) {
        return (OffsetableTrackedWaypoint)TrackedWaypoint.ofPos(source.left().get(), this.getConfig(), new Vec3i(this.pos.getX() + offset.getX(), this.pos.getY(), this.pos.getZ() + offset.getZ()));
    }
}
