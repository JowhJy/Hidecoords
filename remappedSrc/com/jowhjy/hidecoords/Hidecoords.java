package com.jowhjy.hidecoords;

import com.jowhjy.hidecoords.command.CoordoffsetCommand;
import com.jowhjy.hidecoords.mixin.ServerChunkLoadingManagerAccessor;
import com.jowhjy.hidecoords.mixin.ServerWaypointHandlerInvoker;
import com.jowhjy.hidecoords.mixin.ServerWorldAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.ClientboundSetChunkCacheCenterPacket;
import net.minecraft.network.protocol.game.ClientboundSetExperiencePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.waypoints.ServerWaypointManager;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.storage.LevelData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hidecoords implements ModInitializer {


    public static final String MOD_ID = "hidecoords";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final GameRule<Boolean> HIDECOORDS_GAMERULE =
            GameRuleBuilder.forBoolean(true).buildAndRegister(Identifier.fromNamespaceAndPath("hidecoords","hidecoords"));

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CoordoffsetCommand.register(dispatcher, registryAccess));
    }

    //credit to Patbox (Polymer) for parts of this method!
    public static void resendDataAfterOffsetChange(ServerPlayer player) {

        var world = player.level();
        var chunksLoadingManagerAccess = ((ServerChunkLoadingManagerAccessor) player.level().getChunkSource().chunkMap);

        try {
            for (var e : ((ServerWorldAccessor) player.level()).hidecoords$getEntityManager().getEntityGetter().getAll()) {
                var tracker = chunksLoadingManagerAccess.hidecoords$getEntityTrackers().get(e.getId());
                if (tracker != null) {
                    tracker.updatePlayer(player);
                }
            }
        }
        catch (Throwable throwable) {
            Hidecoords.LOGGER.warn("Failed to reload entities", throwable);
        }

        var playerManager = player.level().getServer().getPlayerList();
        //some code from respawnPlayer is reused here
        LevelData worldProperties = world.getLevelData();
        player.connection.send(new ClientboundRespawnPacket(player.createCommonSpawnInfo(world), ClientboundRespawnPacket.KEEP_ALL_DATA));
        player.connection
                .teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
        player.connection.send(new ClientboundChangeDifficultyPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));
        player.connection
                .send(new ClientboundSetExperiencePacket(player.experienceProgress, player.totalExperience, player.experienceLevel));
        playerManager.sendActivePlayerEffects(player);
        playerManager.sendLevelInfo(player, world);
        playerManager.sendPlayerPermissionLevel(player);
        player.initInventoryMenu();
        player.setHealth(player.getHealth());

        player.connection.send(new ClientboundSetChunkCacheCenterPacket(player.chunkPosition().x, player.chunkPosition().z));

        player.getChunkTrackingView().forEach((chunkPos) -> {
            var chunk = world.getChunk(chunkPos.x, chunkPos.z);
            player.connection.chunkSender.dropChunk(player, chunk.getPos());
            player.connection.chunkSender.markChunkPendingToSend(chunk);
        });

        //waypoints?
        ServerWaypointManager waypointHandler = player.level().getWaypointManager();
        waypointHandler.transmitters().forEach(waypoint -> ((ServerWaypointHandlerInvoker)waypointHandler).invokeRefreshTracking(player, waypoint));
    }



}
