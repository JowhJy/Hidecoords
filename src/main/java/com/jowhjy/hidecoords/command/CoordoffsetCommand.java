package com.jowhjy.hidecoords.command;

import com.jowhjy.hidecoords.Hidecoords;
import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.mixin.ServerChunkLoadingManagerAccessor;
import com.jowhjy.hidecoords.mixin.ServerWorldAccessor;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import com.jowhjy.hidecoords.util.IServerPlayerEntityMixin;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import eu.pb4.polymer.core.impl.PolymerImpl;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.network.packet.s2c.play.ChunkRenderDistanceCenterS2CPacket;
import net.minecraft.network.packet.s2c.play.CommonPlayerSpawnInfo;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CoordoffsetCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess)
    {
        dispatcher.register(CommandManager.literal("coordoffset").requires(source -> source.hasPermissionLevel(3) && source.isExecutedByPlayer())
                .then(CommandManager.literal("get")
                        .executes(context -> executeGet(context.getSource())))
                .then(CommandManager.literal("set")
                        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                            .executes(context -> executeSet(context.getSource(), BlockPosArgumentType.getBlockPos(context, "pos"))))
                        .then(CommandManager.literal("none")
                            .executes(context -> executeSetNone(context.getSource())))));
    }

    private static int executeSetNone(ServerCommandSource source) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayerOrThrow();
        ((IServerPlayerEntityMixin)player).juhc$setShouldOffset(false);
        resendChunks(player);
        source.sendFeedback(() -> Text.literal("Now sending you true coordinates. Entities will take a while to update."), false);
        return 1;
    }

    private static int executeGet(ServerCommandSource source) throws CommandSyntaxException {
        Offset result = ((HasCoordOffset)source.getPlayerOrThrow().networkHandler).hidecoords$getCoordOffset();
        source.sendFeedback(() -> Text.literal("Your offset is " + result.getBlockPos().toShortString()), false);
        return 1;
    }

    private static int executeSet(ServerCommandSource source, BlockPos pos) throws CommandSyntaxException {
        Offset newOffset = Offset.zeroAtLocation(pos);
        ServerPlayerEntity player = source.getPlayerOrThrow();

        source.sendFeedback(() -> Text.literal(pos.toShortString() + " is now in the 0,0 chunk for you. Entities will take a while to update."), false);

        player.juhc$setShouldOffset(true);

        if (newOffset.equals(((HasCoordOffset) player.networkHandler).hidecoords$getCoordOffset())) return 0;

        ((HasCoordOffset)player.networkHandler).hidecoords$setCoordOffset(Offset.zeroAtLocation(pos));

        resendChunks(player);

        return 1;
    }

    //credit to Patbox (Polymer) for parts of this method!
    private static void resendChunks(ServerPlayerEntity player) {

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
