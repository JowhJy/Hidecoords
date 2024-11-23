package com.jowhjy.hidecoords.command;

import com.jowhjy.hidecoords.Offset;
import com.jowhjy.hidecoords.util.HasCoordOffset;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class CoordoffsetCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess)
    {
        dispatcher.register(CommandManager.literal("coordoffset").requires(source -> source.hasPermissionLevel(3) && source.isExecutedByPlayer())
                .then(CommandManager.literal("get")
                        .executes(context -> executeGet(context.getSource())))
                //to change offset on the fly we need to figure out a way to resend all chunks first :)
                /*.then(CommandManager.literal("set")
                        .then(CommandManager.argument("pos", BlockPosArgumentType.blockPos())
                            .executes(context -> executeSet(context.getSource(), BlockPosArgumentType.getBlockPos(context, "pos")))))*/);
    }

    private static int executeGet(ServerCommandSource source) {
        Offset result = ((HasCoordOffset)source.getPlayer().networkHandler).hidecoords$getCoordOffset();
        source.sendFeedback(() -> Text.literal("Your offset is " + result.getBlockPos().toShortString()), false);
        return 1;
    }

    private static int executeSet(ServerCommandSource source, BlockPos pos) {
        ((HasCoordOffset)source.getPlayer().networkHandler).hidecoords$setCoordOffset(Offset.zeroAtLocation(pos));
        source.sendFeedback(() -> Text.literal(pos.toShortString() + " is now 0,0 for you"), false);
        return 1;
    }
}
