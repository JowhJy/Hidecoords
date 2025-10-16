package com.jowhjy.hidecoords;

import com.jowhjy.hidecoords.command.CoordoffsetCommand;
import com.jowhjy.hidecoords.mixin.ServerChunkLoadingManagerAccessor;
import com.jowhjy.hidecoords.mixin.ServerWaypointHandlerInvoker;
import com.jowhjy.hidecoords.mixin.ServerWorldAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerWaypointHandler;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hidecoords implements ModInitializer {


    public static final String MOD_ID = "hidecoords";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final GameRules.Key<GameRules.BooleanRule> HIDECOORDS_GAMERULE =
            GameRuleRegistry.register("hideCoordinates", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CoordoffsetCommand.register(dispatcher, registryAccess));
    }

    //credit to Patbox (Polymer) for parts of this method!
    public static void resendDataAfterOffsetChange(ServerPlayerEntity player) {

        var world = player.getEntityWorld();
        var chunksLoadingManagerAccess = ((ServerChunkLoadingManagerAccessor) player.getEntityWorld().getChunkManager().chunkLoadingManager);

        try {
            for (var e : ((ServerWorldAccessor) player.getEntityWorld()).hidecoords$getEntityManager().getLookup().iterate()) {
                var tracker = chunksLoadingManagerAccess.hidecoords$getEntityTrackers().get(e.getId());
                if (tracker != null) {
                    tracker.updateTrackedStatus(player);
                }
            }
        }
        catch (Throwable throwable) {
            Hidecoords.LOGGER.warn("Failed to reload entities", throwable);
        }

        var playerManager = player.getEntityWorld().getServer().getPlayerManager();
        //some code from respawnPlayer is reused here
        WorldProperties worldProperties = world.getLevelProperties();
        player.networkHandler.sendPacket(new PlayerRespawnS2CPacket(player.createCommonPlayerSpawnInfo(world), PlayerRespawnS2CPacket.KEEP_ALL));
        player.networkHandler
                .requestTeleport(player.getX(), player.getY(), player.getZ(), player.getYaw(), player.getPitch());
        player.networkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
        player.networkHandler
                .sendPacket(new ExperienceBarUpdateS2CPacket(player.experienceProgress, player.totalExperience, player.experienceLevel));
        playerManager.sendStatusEffects(player);
        playerManager.sendWorldInfo(player, world);
        playerManager.sendCommandTree(player);
        player.onSpawn();
        player.setHealth(player.getHealth());

        player.networkHandler.sendPacket(new ChunkRenderDistanceCenterS2CPacket(player.getChunkPos().x, player.getChunkPos().z));

        player.getChunkFilter().forEach((chunkPos) -> {
            var chunk = world.getChunk(chunkPos.x, chunkPos.z);
            player.networkHandler.chunkDataSender.unload(player, chunk.getPos());
            player.networkHandler.chunkDataSender.add(chunk);
        });

        //waypoints?
        ServerWaypointHandler waypointHandler = player.getEntityWorld().getWaypointHandler();
        waypointHandler.getWaypoints().forEach(waypoint -> ((ServerWaypointHandlerInvoker)waypointHandler).invokeRefreshTracking(player, waypoint));
    }



}
