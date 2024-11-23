package com.jowhjy.hidecoords;

import com.jowhjy.hidecoords.command.CoordoffsetCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
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



}
