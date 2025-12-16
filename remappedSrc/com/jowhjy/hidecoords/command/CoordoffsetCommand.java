package com.jowhjy.hidecoords.command;

import com.jowhjy.hidecoords.Hidecoords;
import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class CoordoffsetCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess)
    {
        dispatcher.register(Commands.literal("coordoffset").requires(Commands.hasPermission(Commands.LEVEL_ADMINS)).requires(CommandSourceStack::isPlayer)
                .then(Commands.literal("get")
                        .executes(context -> executeGet(context.getSource())))
                .then(Commands.literal("set")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                            .executes(context -> executeSet(context.getSource(), BlockPosArgument.getBlockPos(context, "pos"))))
                        .then(Commands.literal("none")
                            .executes(context -> executeSetNone(context.getSource())))));
    }

    private static int executeSetNone(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        player.juhc$setShouldOffset(false);
        Hidecoords.resendDataAfterOffsetChange(player);
        source.sendSuccess(() -> Component.literal("You should now be receiving true coordinates. Entities may take a while to show their correct position."), false);
        return 1;
    }

    private static int executeGet(CommandSourceStack source) throws CommandSyntaxException {
        Offset result = ((HasCoordOffset)source.getPlayerOrException().connection).hidecoords$getCoordOffset();
        source.sendSuccess(() -> Component.literal("Your offset is " + result.getBlockPos().toShortString()), false);
        return 1;
    }

    private static int executeSet(CommandSourceStack source, BlockPos pos) throws CommandSyntaxException {
        Offset newOffset = Offset.zeroAtLocation(pos);
        ServerPlayer player = source.getPlayerOrException();

        source.sendSuccess(() -> Component.literal(pos.toShortString() + " is now in the 0,0 chunk for you. Entities may take a while to show their correct position."), false);

        player.juhc$setShouldOffset(true);

        if (newOffset.equals(((HasCoordOffset) player.connection).hidecoords$getCoordOffset())) return 0;

        ((HasCoordOffset)player.connection).hidecoords$setCoordOffset(Offset.zeroAtLocation(pos), true);

        return 1;
    }
}
