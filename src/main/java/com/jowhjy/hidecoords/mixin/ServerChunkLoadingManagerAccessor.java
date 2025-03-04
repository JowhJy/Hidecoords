package com.jowhjy.hidecoords.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.server.world.ServerChunkLoadingManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerChunkLoadingManager.class)
public interface ServerChunkLoadingManagerAccessor {

    @Accessor("entityTrackers")
    Int2ObjectMap<ServerChunkLoadingManager.EntityTracker> hidecoords$getEntityTrackers();
}
