package net.sirgrantd.magic_coins.api;

import net.sirgrantd.magic_coins.config.MagicCoinsConfig;

public class CoinsValuesManager {

    public static int getValueSilverCoins() {
        return 1;
    }

    public static int getValueGoldCoins() {
        return MagicCoinsConfig.goldCoinsValue;
    }

    public static int getValueCrystalCoins() {
        return MagicCoinsConfig.crystalCoinsValue;
    }
    
}
