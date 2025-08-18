package net.sirgrantd.magic_coins.api;

import net.minecraft.world.entity.Entity;

import net.sirgrantd.magic_coins.config.ServerConfig;
import net.sirgrantd.magic_coins.capabilities.CoinsBagCapabilities;
import net.sirgrantd.magic_coins.capabilities.CoinsBagCapabilities.CoinsInBag;

public class MagicCoinsApi {

    // Feature: Coins in bag

    public static boolean hasCapability(Entity entity) {
        return entity.getData(CoinsBagCapabilities.COINS_IN_BAG) != null;
    }
    
    public static int getTotalCoins(Entity entity) {
        return entity.getData(CoinsBagCapabilities.COINS_IN_BAG).valueTotalInCoins;
    }

    public static void setTotalCoins(Entity entity, int value) {
        CoinsInBag coinsInBag = entity.getData(CoinsBagCapabilities.COINS_IN_BAG);
        coinsInBag.valueTotalInCoins = Math.max(0, value);
        coinsInBag.syncCoinsInBag(entity);
    }

    public static void addCoins(Entity entity, int value) {
        CoinsInBag coinsInBag = entity.getData(CoinsBagCapabilities.COINS_IN_BAG);
        coinsInBag.valueTotalInCoins = Math.max(0, getTotalCoins(entity) + value);
        coinsInBag.syncCoinsInBag(entity);
    }

    public static void removeCoins(Entity entity, int value) {
        CoinsInBag coinsInBag = entity.getData(CoinsBagCapabilities.COINS_IN_BAG);
        coinsInBag.valueTotalInCoins = Math.max(0, getTotalCoins(entity) - value);
        coinsInBag.syncCoinsInBag(entity);
    }

    // Feature: Coins lost on death

    public static boolean isCoinsLostOnDeath(Entity entity) {
        return entity.getData(CoinsBagCapabilities.COINS_IN_BAG).isCoinsLostOnDeath;
    }

    public static void setIsCoinsLostOnDeath(Entity entity, boolean value) {
        CoinsInBag coinsInBag = entity.getData(CoinsBagCapabilities.COINS_IN_BAG);
        coinsInBag.isCoinsLostOnDeath = value;
        coinsInBag.syncCoinsInBag(entity);
    }

    public static int getPercentageCoinsSaveOnDeath() {
        return ServerConfig.percentageCoinsSaveOnDeath;
    }

    // Feature: Coins values

    public static int getValueSilverCoins() {
        return ServerConfig.silverCoinsValue;
    }

    public static int getValueGoldCoins() {
        return ServerConfig.goldCoinsValue;
    }

    public static int getValueCrystalCoins() {
        return ServerConfig.crystalCoinsValue;
    }
}