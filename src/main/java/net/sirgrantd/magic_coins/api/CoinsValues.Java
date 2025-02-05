package net.sirgrantd.magic_coins.api;

import net.sirgrantd.magic_coins.common.items.CrystalCoinItem;
import net.sirgrantd.magic_coins.common.items.GoldCoinItem;
import net.sirgrantd.magic_coins.common.items.SilverCoinItem;

public class CoinsValues {
    public static int getValueSilverCoins() {
        return SilverCoinItem.getCoinsValue();
    }

    public static int getValueGoldCoins() {
        return GoldCoinItem.getCoinsValue();
    }

    public static int getValueCrystalCoins() {
        return CrystalCoinItem.getCoinsValue();
    }
}
        