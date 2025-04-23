package net.sirgrantd.magic_coins.common.config;

import java.util.function.Supplier;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.sirgrantd.magic_coins.MagicCoinsMod;

@EventBusSubscriber(modid = MagicCoinsMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonConfig {

    public static int goldCoinsValue;
    public static int crystalCoinsValue;
    public static Double coinLossFactor;

    public static class Config {
        public static final Supplier<Integer> GOLD_COIN_VALUE;
        public static final Supplier<Integer> CRYSTAL_COIN_VALUE;
        public static final Supplier<Double> COIN_LOSS_FACTOR;

        static {
            ModConfigSpec.Builder CONFIG_BUILDER = new ModConfigSpec.Builder();
            
            CONFIG_BUILDER.push("COINS");

            GOLD_COIN_VALUE = CONFIG_BUILDER
                .comment("The value of a gold coin")
                .defineInRange("goldMagicValue", 50, 0, 500);

            CRYSTAL_COIN_VALUE = CONFIG_BUILDER
                .comment("The value of a crystal coin")
                .defineInRange("crystalMagicValue", 2500, 0, 25000);

            COIN_LOSS_FACTOR = CONFIG_BUILDER
                    .comment("How many % coins should be lost on Death?")
                    .defineInRange("coinLossFactor", 0.5, 0, 1.0);

            CONFIG_BUILDER.pop();

            SPEC = CONFIG_BUILDER.build();
        }

        public static final ModConfigSpec SPEC;
    }

    private static void bakeConfig() {
        goldCoinsValue = Config.GOLD_COIN_VALUE.get();
        crystalCoinsValue = Config.CRYSTAL_COIN_VALUE.get();
        coinLossFactor = Config.COIN_LOSS_FACTOR.get();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.COMMON) {
            bakeConfig();
        }
    }
}