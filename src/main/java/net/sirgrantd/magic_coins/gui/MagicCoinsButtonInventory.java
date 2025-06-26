package net.sirgrantd.magic_coins.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import top.theillusivec4.curios.api.client.ICuriosScreen;

import net.sirgrantd.magic_coins.config.ClientConfig;
import net.sirgrantd.magic_coins.gui.components.CustomButton;
import net.sirgrantd.magic_coins.gui.handlers.HandlerButton;

public class MagicCoinsButtonInventory {
    @SubscribeEvent
    public void onInventoryGuiInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();

        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen || screen instanceof ICuriosScreen) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
            boolean isCreative = screen instanceof CreativeModeInventoryScreen;
            boolean isCurios = screen instanceof ICuriosScreen;
            
            int xOffsetCollectCoins = isCreative ? 3 + ClientConfig.xDisplayCoins : 3 + ClientConfig.xDisplayCoins;
            int yOffsetCollectCoins = isCreative ? -72 + ClientConfig.yDisplayCoins : -25 + ClientConfig.yDisplayCoins;

            CustomButton collectCoinsButton = new CustomButton(gui, xOffsetCollectCoins, yOffsetCollectCoins, 0, 0, 17, 17, CustomButton.COLLECT_COINS_ICON, HandlerButton.COLLECT_COINS_FUNC);
            event.addListener(collectCoinsButton);

            int xOffsetSilver = isCreative ? 127 + ClientConfig.xSilverButton : 77 + ClientConfig.xSilverButton;
            int yOffsetSilver = isCreative ? 5 + ClientConfig.ySilverButton : 7 + ClientConfig.ySilverButton;

            CustomButton silverButton = new CustomButton(gui, xOffsetSilver, yOffsetSilver, 0, 0, 13, 13, CustomButton.SILVER_ICON, HandlerButton.SILVER_COIN_FUNC);
            event.addListener(silverButton);

            int xOffsetGold = isCreative ? 127 + ClientConfig.xGoldButton : 77 + ClientConfig.xGoldButton;
            int yOffsetGold = isCreative ? 21 + ClientConfig.yGoldButton : 23 + ClientConfig.yGoldButton;

            CustomButton goldButton = new CustomButton(gui, xOffsetGold, yOffsetGold, 0, 0, 13, 13, CustomButton.GOLD_ICON, HandlerButton.GOLD_COIN_FUNC);
            event.addListener(goldButton);

            int xOffsetCrystal = isCreative ? 127 + ClientConfig.xCrystalButton : 77 + ClientConfig.xCrystalButton;
            int yOffsetCrystal = isCreative ? 37 + ClientConfig.yCrystalButton : 39 + ClientConfig.yCrystalButton;

            CustomButton crystalButton = new CustomButton(gui, xOffsetCrystal, yOffsetCrystal, 0, 0, 13, 13, CustomButton.CRYSTAL_ICON, HandlerButton.CRYSTAL_COIN_FUNC);
            event.addListener(crystalButton);

            // Buttons Extras

            if (ClientConfig.enableSilverForGoldButton && !isCurios && !isCreative) {
                int xOffsetSilverForGold = -30 + ClientConfig.xSilverForGoldButton;
                int yOffsetSilverForGold = 7 + ClientConfig.ySilverForGoldButton;

                CustomButton silverForGoldButton = new CustomButton(gui, xOffsetSilverForGold, yOffsetSilverForGold, 0, 0, 26, 13, CustomButton.SILVER_FOR_GOLD_ICON, HandlerButton.SILVER_FOR_GOLD_FUNC);
                event.addListener(silverForGoldButton);
            }

            if (ClientConfig.enableGoldForSilverButton && !isCurios && !isCreative) {
                int xOffsetGoldForSilver = -30 + ClientConfig.xGoldForSilverButton;
                int yOffsetGoldForSilver = 23 + ClientConfig.yGoldForSilverButton;

                CustomButton goldForSilverButton = new CustomButton(gui, xOffsetGoldForSilver, yOffsetGoldForSilver, 0, 0, 26, 13, CustomButton.GOLD_FOR_SILVER_ICON, HandlerButton.GOLD_FOR_SILVER_FUNC);
                event.addListener(goldForSilverButton);
            }

            if (ClientConfig.enableGoldForCrystalButton && !isCurios && !isCreative) {
                int xOffsetGoldForCrystal = -30 + ClientConfig.xGoldForCrystalButton;
                int yOffsetGoldForCrystal = 39 + ClientConfig.yGoldForCrystalButton;

                CustomButton goldForCrystalButton = new CustomButton(gui, xOffsetGoldForCrystal, yOffsetGoldForCrystal, 0, 0, 26, 13, CustomButton.GOLD_FOR_CRYSTAL_ICON, HandlerButton.GOLD_FOR_CRYSTAL_FUNC);
                event.addListener(goldForCrystalButton);
            }

            if (ClientConfig.enableCrystalForGoldButton && !isCurios && !isCreative) {
                int xOffsetCrystalForGold = -30 + ClientConfig.xCrystalForGoldButton;
                int yOffsetCrystalForGold = 55 + ClientConfig.yCrystalForGoldButton;

                CustomButton crystalForGoldButton = new CustomButton(gui, xOffsetCrystalForGold, yOffsetCrystalForGold, 0, 0, 26, 13, CustomButton.CRYSTAL_FOR_GOLD_ICON, HandlerButton.CRYSTAL_FOR_GOLD_FUNC);
                event.addListener(crystalForGoldButton);
            }
        
        }
    }
}
