package com.jowhjy.hidecoords.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.waypoints.ServerWaypointManager;
import net.minecraft.world.waypoints.WaypointTransmitter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerWaypointManager.class)
public interface ServerWaypointHandlerInvoker {

    @Invoker
    void invokeCreateConnection(ServerPlayer player, WaypointTransmitter waypoint);
}
