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
                        .executes(context -> executeGet(context.getSource()))));
    }

    private static int executeGet(ServerCommandSource source) {
        Offset result = ((HasCoordOffset)source.getPlayer().networkHandler).juhc$getCoordOffset();
        source.sendFeedback(() -> Text.literal(result.getBlockPos().toString()), false);
        return 1;
    }
}
