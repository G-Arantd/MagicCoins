package net.sirgrantd.magic_coins.client.gui.text;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TextDisplayRender {

    private final AbstractContainerScreen<?> parentGui;
    private final int xOffset;
    private final int yOffset;
    private final String text;

    public TextDisplayRender(AbstractContainerScreen<?> parentGui, int xOffset, int yOffset, String text) {
        this.parentGui = parentGui;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.text = text;
    }

    public void renderText(GuiGraphics guiGraphics) {
        
        int x = parentGui.getGuiLeft() + xOffset;
        int y = parentGui.getGuiTop() + yOffset;

        if (parentGui instanceof CreativeModeInventoryScreen creativeScreen) {
            boolean isInventoryTab = creativeScreen.isInventoryOpen();

            if (!isInventoryTab) {
                return;
            }
        }

        guiGraphics.drawString(
            Minecraft.getInstance().font, text, x, y, 0xFFFFFF, false
        );
    }
}
