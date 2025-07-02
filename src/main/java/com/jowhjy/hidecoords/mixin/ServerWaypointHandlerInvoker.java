package com.jowhjy.hidecoords.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerWaypointHandler;
import net.minecraft.world.waypoint.ServerWaypoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerWaypointHandler.class)
public interface ServerWaypointHandlerInvoker {

    @Invoker
    void invokeRefreshTracking(ServerPlayerEntity player, ServerWaypoint waypoint);
}
