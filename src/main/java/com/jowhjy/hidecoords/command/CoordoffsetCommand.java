package com.jowhjy.hidecoords.command;

import com.jowhjy.hidecoords.Hidecoords;
import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CoordoffsetCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess)
    {
        dispatcher.register(CommandManager.literal("coordoffset").requires(CommandManager.requirePermissionLevel(CommandManager.ADMINS_CHECK)).requires(ServerCommandSource::isExecutedByPlayer)
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
        player.juhc$setShouldOffset(false);
        Hidecoords.resendDataAfterOffsetChange(player);
        source.sendFeedback(() -> Text.literal("You should now be receiving true coordinates. Entities may take a while to show their correct position."), false);
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

        source.sendFeedback(() -> Text.literal(pos.toShortString() + " is now in the 0,0 chunk for you. Entities may take a while to show their correct position."), false);

        player.juhc$setShouldOffset(true);

        if (newOffset.equals(((HasCoordOffset) player.networkHandler).hidecoords$getCoordOffset())) return 0;

        ((HasCoordOffset)player.networkHandler).hidecoords$setCoordOffset(Offset.zeroAtLocation(pos), true);

        return 1;
    }
}
