package net.sirgrantd.magic_coins.config;

import java.util.function.Supplier;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import net.sirgrantd.magic_coins.MagicCoinsMod;

@EventBusSubscriber(modid = MagicCoinsMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ServerConfig {

    public static int goldCoinsValue;
    public static int crystalCoinsValue;
    public static int silverCoinsValue;

    public static int percentageCoinsLostOnDeath;
    
    public static boolean coinsLootChests;

    public static class Config {
        public static final Supplier<Integer> SILVER_COIN_VALUE;
        public static final Supplier<Integer> GOLD_COIN_VALUE;
        public static final Supplier<Integer> CRYSTAL_COIN_VALUE;
        public static final Supplier<Integer> PERCENTAGE_COINS_LOST_ON_DEATH;
        public static final Supplier<Boolean> COINS_LOOT_CHESTS;

        static {
            ModConfigSpec.Builder CONFIG_BUILDER = new ModConfigSpec.Builder();
            
            CONFIG_BUILDER.push("COINS");

            SILVER_COIN_VALUE = CONFIG_BUILDER
                .comment("The value of a silver coin")
                .defineInRange("silverMagicValue", 1, 1, 100);

            GOLD_COIN_VALUE = CONFIG_BUILDER
                .comment("The value of a gold coin")
                .defineInRange("goldMagicValue", 50, 1, 500);

            CRYSTAL_COIN_VALUE = CONFIG_BUILDER
                .comment("The value of a crystal coin")
                .defineInRange("crystalMagicValue", 2500, 1, 25000);

            CONFIG_BUILDER.pop();

            CONFIG_BUILDER.push("DEATH");

            PERCENTAGE_COINS_LOST_ON_DEATH = CONFIG_BUILDER
                .comment("The percentage of coins lost on death")
                .defineInRange("percentageCoinsLostOnDeath", 50, 0, 100);

            CONFIG_BUILDER.pop();

            CONFIG_BUILDER.push("LOOTS");

            COINS_LOOT_CHESTS = CONFIG_BUILDER
                .comment("Should coins be lootable from chests")
                .define("coinsLootChests", true);
            
            CONFIG_BUILDER.pop();

            SPEC = CONFIG_BUILDER.build();
        }

        public static final ModConfigSpec SPEC;
    }

    private static void bakeConfig() {
        silverCoinsValue = Config.SILVER_COIN_VALUE.get();
        goldCoinsValue = Config.GOLD_COIN_VALUE.get();
        crystalCoinsValue = Config.CRYSTAL_COIN_VALUE.get();
        percentageCoinsLostOnDeath = Config.PERCENTAGE_COINS_LOST_ON_DEATH.get();
        coinsLootChests = Config.COINS_LOOT_CHESTS.get();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.SERVER && event.getConfig().getSpec() == Config.SPEC) {
            bakeConfig();
        }
    }
}