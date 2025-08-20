package com.jowhjy.hidecoords.config;

import com.jowhjy.hidecoords.Hidecoords;

public class HidecoordsConfigs {

    private static HidecoordsConfigProvider configs;
    public static SimpleConfig CONFIG;

    public static boolean BAMBOO_DRIPSTONE_HITBOX;

    public static void registerConfigs() {

        configs = new HidecoordsConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(Hidecoords.MOD_ID + "_config").provider(configs).request();

        assignConfigs();

    }

    private static void assignConfigs() {
    }

    private static void createConfigs() {
    }
}
