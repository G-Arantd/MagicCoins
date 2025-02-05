package net.sirgrantd.magic_coins.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.sirgrantd.magic_coins.api.BagCoinsManager;
import net.sirgrantd.magic_coins.client.gui.image.ImageDisplayRender;
import net.sirgrantd.magic_coins.client.gui.text.TextDisplayRender;
import top.theillusivec4.curios.api.client.ICuriosScreen;

@EventBusSubscriber({Dist.CLIENT})
public class MoneyDisplayInventory {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(ScreenEvent.Render.Post event) {
        Screen screen = event.getScreen();
        if ( screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen || screen instanceof ICuriosScreen) {

            AbstractContainerScreen<?> gui = (AbstractContainerScreen<?>) screen;
            boolean isCreative = screen instanceof CreativeModeInventoryScreen;

            Player player = Minecraft.getInstance().player;
            String valueTotalCoins = String.valueOf(BagCoinsManager.getValueTotalInCoins(player));

            int xOffsetImage = isCreative ? 0 : 0;
            int yOffsetImage = isCreative ? -75 : -28;

            int xOffsetText = 25;
            int yOffsetText = isCreative ? -67 : -20;

            ImageDisplayRender displayRender = new ImageDisplayRender(gui, xOffsetImage, yOffsetImage, ImageDisplayRender.DISPLAY_VIEW);
            displayRender.renderWidget(event.getGuiGraphics(), 80, 23);

            TextDisplayRender textRender = new TextDisplayRender(gui, xOffsetText, yOffsetText, valueTotalCoins);
            textRender.renderText(event.getGuiGraphics());

        }
    }
}