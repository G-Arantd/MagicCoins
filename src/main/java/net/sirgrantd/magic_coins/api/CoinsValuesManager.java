package net.sirgrantd.magic_coins.api;

import net.sirgrantd.magic_coins.common.config.CommonConfig;

public class CoinsValuesManager {

    public static int getValueSilverCoins() {
        return 1;
    }

    public static int getValueGoldCoins() {
        return CommonConfig.goldCoinsValue;
    }

    public static int getValueCrystalCoins() {
        return CommonConfig.crystalCoinsValue;
    }
    
}
