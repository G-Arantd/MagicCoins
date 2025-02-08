package net.sirgrantd.magic_coins.api;

import net.minecraft.world.entity.Entity;
import net.sirgrantd.magic_coins.common.capabilities.CoinsBagCapabilities;

public class BagCoinsManager {
    public static int getValueTotalInCoins(Entity entity) {
        return entity.getData(CoinsBagCapabilities.COINS_IN_BAG).valueTotalInCoins;
    }

    public static void setValueTotalInCoins(Entity entity, int value) {
        CoinsBagCapabilities.CoinsInBag coinsInBag = entity.getData(CoinsBagCapabilities.COINS_IN_BAG);
        coinsInBag.valueTotalInCoins = Math.max(0, value);
        coinsInBag.syncCoinsInBag(entity);
    }

    public static void addValueTotalInCoins(Entity entity, int value) {
        CoinsBagCapabilities.CoinsInBag coinsInBag = entity.getData(CoinsBagCapabilities.COINS_IN_BAG);
        coinsInBag.valueTotalInCoins = Math.max(0, getValueTotalInCoins(entity) + value);
        coinsInBag.syncCoinsInBag(entity);
    }

    public static void removeValueTotalInCoins(Entity entity, int value) {
        CoinsBagCapabilities.CoinsInBag coinsInBag = entity.getData(CoinsBagCapabilities.COINS_IN_BAG);
        coinsInBag.valueTotalInCoins = Math.max(0, getValueTotalInCoins(entity) - value);
        coinsInBag.syncCoinsInBag(entity);
    }

    public static boolean getIsCoinsLostOnDeath(Entity entity) {
        return entity.getData(CoinsBagCapabilities.COINS_IN_BAG).isCoinsLostOnDeath;
    }
    
    public static void setIsCoinsLostOnDeath(Entity entity, boolean value) {
        CoinsBagCapabilities.CoinsInBag coinsInBag = entity.getData(CoinsBagCapabilities.COINS_IN_BAG);
        coinsInBag.isCoinsLostOnDeath = value;
        coinsInBag.syncCoinsInBag(entity);
    }

    public static boolean hasCapability(Entity entity) {
        return entity.getData(CoinsBagCapabilities.COINS_IN_BAG) != null;
    }
}
