package net.sirgrantd.magic_coins.client.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.sirgrantd.magic_coins.client.gui.buttons.CustomButton;
import net.sirgrantd.magic_coins.common.gui.links.buttons.HandlerButton;
import top.theillusivec4.curios.api.client.ICuriosScreen;

public class MagicCoinsInventory {
    @SubscribeEvent
    public void onInventoryGuiInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();

        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen || screen instanceof ICuriosScreen) {
            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
            boolean isCreative = screen instanceof CreativeModeInventoryScreen;
            
            int xOffsetCollectCoins = isCreative ? 3 : 3;
            int yOffsetCollectCoins = isCreative ? -72 : -25;

            CustomButton collectCoinsButton = new CustomButton(gui, xOffsetCollectCoins, yOffsetCollectCoins, 0, 0, 17, 17, CustomButton.COLLECT_COINS_ICON, HandlerButton.COLLECT_COINS_FUNC);
            event.addListener(collectCoinsButton);

            int xOffsetSilver = isCreative ? 127 : 77;
            int yOffsetSilver = isCreative ? 5 : 7;

            CustomButton silverButton = new CustomButton(gui, xOffsetSilver, yOffsetSilver, 0, 0, 13, 13, CustomButton.SILVER_ICON, HandlerButton.SILVER_COIN_FUNC);
            event.addListener(silverButton);

            int xOffsetGold = isCreative ? 127 : 77;
            int yOffsetGold = isCreative ? 21 : 23;

            CustomButton goldButton = new CustomButton(gui, xOffsetGold, yOffsetGold, 0, 0, 13, 13, CustomButton.GOLD_ICON, HandlerButton.GOLD_COIN_FUNC);
            event.addListener(goldButton);

            int xOffsetCrystal = isCreative ? 127 : 77;
            int yOffsetCrystal = isCreative ? 37 : 39;

            CustomButton crystalButton = new CustomButton(gui, xOffsetCrystal, yOffsetCrystal, 0, 0, 13, 13, CustomButton.CRYSTAL_ICON, HandlerButton.CRYSTAL_COIN_FUNC);
            event.addListener(crystalButton);
        
        }
    }
}
