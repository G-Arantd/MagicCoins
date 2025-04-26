package net.sirgrantd.magic_coins.config;

import java.util.function.Supplier;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import net.sirgrantd.magic_coins.MagicCoinsMod;

@EventBusSubscriber(modid = MagicCoinsMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientConfig {

    public static int xDisplayCoins;
    public static int yDisplayCoins;

    // Collect buttons

    public static int xSilverButton;
    public static int ySilverButton;

    public static int xGoldButton;
    public static int yGoldButton;

    public static int xCrystalButton;
    public static int yCrystalButton;

    // Convert buttons

    public static boolean enableSilverForGoldButton;
    public static int xSilverForGoldButton;
    public static int ySilverForGoldButton;

    public static boolean enableGoldForSilverButton;
    public static int xGoldForSilverButton;
    public static int yGoldForSilverButton;

    public static boolean enableGoldForCrystalButton;
    public static int xGoldForCrystalButton;
    public static int yGoldForCrystalButton;

    public static boolean enableCrystalForGoldButton;
    public static int xCrystalForGoldButton;
    public static int yCrystalForGoldButton;

    // --------------------------------------------

    public static class Config {
        public static final Supplier<Integer> X_DISPLAY_COINS;
        public static final Supplier<Integer> Y_DISPLAY_COINS;

        public static final Supplier<Integer> X_SILVER_BUTTON;
        public static final Supplier<Integer> Y_SILVER_BUTTON;

        public static final Supplier<Integer> X_GOLD_BUTTON;
        public static final Supplier<Integer> Y_GOLD_BUTTON;

        public static final Supplier<Integer> X_CRYSTAL_BUTTON;
        public static final Supplier<Integer> Y_CRYSTAL_BUTTON;

        public static final Supplier<Boolean> ENABLE_SILVER_FOR_GOLD_BUTTON;

        public static final Supplier<Integer> X_SILVER_FOR_GOLD_BUTTON;
        public static final Supplier<Integer> Y_SILVER_FOR_GOLD_BUTTON;

        public static final Supplier<Boolean> ENABLE_GOLD_FOR_SILVER_BUTTON;

        public static final Supplier<Integer> X_GOLD_FOR_SILVER_BUTTON;
        public static final Supplier<Integer> Y_GOLD_FOR_SILVER_BUTTON;

        public static final Supplier<Boolean> ENABLE_GOLD_FOR_CRYSTAL_BUTTON;

        public static final Supplier<Integer> X_GOLD_FOR_CRYSTAL_BUTTON;
        public static final Supplier<Integer> Y_GOLD_FOR_CRYSTAL_BUTTON;

        public static final Supplier<Boolean> ENABLE_CRYSTAL_FOR_GOLD_BUTTON;

        public static final Supplier<Integer> X_CRYSTAL_FOR_GOLD_BUTTON;
        public static final Supplier<Integer> Y_CRYSTAL_FOR_GOLD_BUTTON;

        static {
            ModConfigSpec.Builder CONFIG_BUILDER = new ModConfigSpec.Builder();

            // Displays

            CONFIG_BUILDER.push("DISPLAYS");

            X_DISPLAY_COINS = CONFIG_BUILDER
                .comment("The x position of the display for coins")
                .defineInRange("xDisplayCoins", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            Y_DISPLAY_COINS = CONFIG_BUILDER
                .comment("The y position of the display for coins")
                .defineInRange("yDisplayCoins", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            CONFIG_BUILDER.pop();

            // Buttons

            CONFIG_BUILDER.push("BUTTONS");

            // Collect buttons

            X_SILVER_BUTTON = CONFIG_BUILDER
                .comment("The x position of the button for silver coins")
                .defineInRange("xSilverButton", 0, -750, 750);

            Y_SILVER_BUTTON = CONFIG_BUILDER
                .comment("The y position of the button for silver coins")
                .defineInRange("ySilverButton", 0, -750, 750);

            X_GOLD_BUTTON = CONFIG_BUILDER
                .comment("The x position of the button for gold coins")
                .defineInRange("xGoldButton", 0, -750, 750);

            Y_GOLD_BUTTON = CONFIG_BUILDER
                .comment("The y position of the button for gold coins")
                .defineInRange("yGoldButton", 0, -750, 750);

            X_CRYSTAL_BUTTON = CONFIG_BUILDER
                .comment("The x position of the button for crystal coins")
                .defineInRange("xCrystalButton", 0, -750, 750);

            Y_CRYSTAL_BUTTON = CONFIG_BUILDER
                .comment("The y position of the button for crystal coins")
                .defineInRange("yCrystalButton", 0, -750, 750);

            // Convert buttons

            ENABLE_SILVER_FOR_GOLD_BUTTON = CONFIG_BUILDER
                .comment("Enable the button to convert silver coins to gold coins")
                .define("enableSilverForGoldButton", false);
            
            X_SILVER_FOR_GOLD_BUTTON = CONFIG_BUILDER
                .comment("The x position of the button to convert silver coins to gold coins")
                .defineInRange("xSilverForGoldButton", 0, -750, 750);

            Y_SILVER_FOR_GOLD_BUTTON = CONFIG_BUILDER
                .comment("The y position of the button to convert silver coins to gold coins")
                .defineInRange("ySilverForGoldButton", 0, -750, 750);

            // --------------------------------------------

            ENABLE_GOLD_FOR_SILVER_BUTTON = CONFIG_BUILDER
                .comment("Enable the button to convert gold coins to silver coins")
                .define("enableGoldForSilverButton", false);
            
            X_GOLD_FOR_SILVER_BUTTON = CONFIG_BUILDER
                .comment("The x position of the button to convert gold coins to silver coins")
                .defineInRange("xGoldForSilverButton", 0, -750, 750);
            
            Y_GOLD_FOR_SILVER_BUTTON = CONFIG_BUILDER
                .comment("The y position of the button to convert gold coins to silver coins")
                .defineInRange("yGoldForSilverButton", 0, -750, 750);

            // --------------------------------------------
            
            ENABLE_GOLD_FOR_CRYSTAL_BUTTON = CONFIG_BUILDER
                .comment("Enable the button to convert gold coins to crystal coins")
                .define("enableGoldForCrystalButton", false);
            
            X_GOLD_FOR_CRYSTAL_BUTTON = CONFIG_BUILDER
                .comment("The x position of the button to convert gold coins to crystal coins")
                .defineInRange("xGoldForCrystalButton", 0, -750, 750);
            
            Y_GOLD_FOR_CRYSTAL_BUTTON = CONFIG_BUILDER
                .comment("The y position of the button to convert gold coins to crystal coins")
                .defineInRange("yGoldForCrystalButton", 0, -750, 750);

            // --------------------------------------------
            
            ENABLE_CRYSTAL_FOR_GOLD_BUTTON = CONFIG_BUILDER
                .comment("Enable the button to convert crystal coins to gold coins")
                .define("enableCrystalForGoldButton", false);
            
            X_CRYSTAL_FOR_GOLD_BUTTON = CONFIG_BUILDER
                .comment("The x position of the button to convert crystal coins to gold coins")
                .defineInRange("xCrystalForGoldButton", 0, -750, 750);
            
            Y_CRYSTAL_FOR_GOLD_BUTTON = CONFIG_BUILDER
                .comment("The y position of the button to convert crystal coins to gold coins")
                .defineInRange("yCrystalForGoldButton", 0, -750, 750);

            CONFIG_BUILDER.pop();

            SPEC = CONFIG_BUILDER.build();
        }

        public static final ModConfigSpec SPEC;
    }

    private static void bakeConfig() {
        xDisplayCoins = Config.X_DISPLAY_COINS.get();
        yDisplayCoins = Config.Y_DISPLAY_COINS.get();

        // Collect buttons

        xSilverButton = Config.X_SILVER_BUTTON.get();
        ySilverButton = Config.Y_SILVER_BUTTON.get();

        xGoldButton = Config.X_GOLD_BUTTON.get();
        yGoldButton = Config.Y_GOLD_BUTTON.get();

        xCrystalButton = Config.X_CRYSTAL_BUTTON.get();
        yCrystalButton = Config.Y_CRYSTAL_BUTTON.get();

        // Convert buttons

        enableSilverForGoldButton = Config.ENABLE_SILVER_FOR_GOLD_BUTTON.get();
        xSilverForGoldButton = Config.X_SILVER_FOR_GOLD_BUTTON.get();
        ySilverForGoldButton = Config.Y_SILVER_FOR_GOLD_BUTTON.get();

        // --------------------------------------------

        enableGoldForSilverButton = Config.ENABLE_GOLD_FOR_SILVER_BUTTON.get();
        xGoldForSilverButton = Config.X_GOLD_FOR_SILVER_BUTTON.get();
        yGoldForSilverButton = Config.Y_GOLD_FOR_SILVER_BUTTON.get();

        // --------------------------------------------

        enableGoldForCrystalButton = Config.ENABLE_GOLD_FOR_CRYSTAL_BUTTON.get();
        xGoldForCrystalButton = Config.X_GOLD_FOR_CRYSTAL_BUTTON.get();
        yGoldForCrystalButton = Config.Y_GOLD_FOR_CRYSTAL_BUTTON.get();

        // --------------------------------------------

        enableCrystalForGoldButton = Config.ENABLE_CRYSTAL_FOR_GOLD_BUTTON.get();
        xCrystalForGoldButton = Config.X_CRYSTAL_FOR_GOLD_BUTTON.get();
        yCrystalForGoldButton = Config.Y_CRYSTAL_FOR_GOLD_BUTTON.get();

    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getType() == ModConfig.Type.CLIENT) {
            bakeConfig();
        }
    }
    
}