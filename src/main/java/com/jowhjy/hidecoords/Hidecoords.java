package com.jowhjy.hidecoords;

import com.jowhjy.hidecoords.command.CoordoffsetCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Hidecoords implements ModInitializer {


    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CoordoffsetCommand.register(dispatcher, registryAccess));
    }
}
