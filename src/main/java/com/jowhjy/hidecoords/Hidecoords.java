package com.jowhjy.hidecoords;

import com.jowhjy.hidecoords.command.CoordoffsetCommand;
import com.jowhjy.hidecoords.mixin.ServerChunkLoadingManagerAccessor;
import com.jowhjy.hidecoords.mixin.ServerWorldAccessor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.network.packet.s2c.play.ChunkRenderDistanceCenterS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hidecoords implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("hidecoords");

    public static final GameRules.Key<GameRules.BooleanRule> HIDECOORDS_GAMERULE =
            GameRuleRegistry.register("hideCoordinates", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(true));

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CoordoffsetCommand.register(dispatcher, registryAccess));
    }

    //credit to Patbox (Polymer) for parts of this method!
    public static void resendChunks(ServerPlayerEntity player) {

        var world = player.getWorld();
        var chunksLoadingManagerAccess = ((ServerChunkLoadingManagerAccessor) ((ServerChunkManager) player.getWorld().getChunkManager()).chunkLoadingManager);

        try {
            for (var e : ((ServerWorldAccessor) player.getWorld()).hidecoords$getEntityManager().getLookup().iterate()) {
                var tracker = chunksLoadingManagerAccess.hidecoords$getEntityTrackers().get(e.getId());
                if (tracker != null) {
                    tracker.updateTrackedStatus(player);
                }
            }
        }
        catch (Throwable throwable) {
            Hidecoords.LOGGER.warn("Failed to reload entities", throwable);
        }

        player.networkHandler.sendPacket(new ChunkRenderDistanceCenterS2CPacket(player.getChunkPos().x, player.getChunkPos().z));
        player.requestTeleport(player.getX(),player.getY(),player.getZ());

        player.getChunkFilter().forEach((chunkPos) -> {
            var chunk = world.getChunk(chunkPos.x, chunkPos.z);
            player.networkHandler.chunkDataSender.unload(player, chunk.getPos());
            player.networkHandler.chunkDataSender.add(chunk);
        });
    }



}
