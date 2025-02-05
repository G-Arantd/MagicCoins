package net.sirgrantd.magic_coins.client.gui.buttons;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.sirgrantd.magic_coins.MagicCoinsMod;
import net.sirgrantd.magic_coins.common.gui.links.buttons.HandlerButton;

@OnlyIn(Dist.CLIENT)
public class CustomButton extends ImageButton{

    private final AbstractContainerScreen<?> parentGui;
    private final int xOffset;
    private final int yOffset;

    public static final WidgetSprites COLLECT_COINS_ICON = new WidgetSprites(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "button_collect_coins"),
            ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "button_collect_coins_highlighted"));

    public static final WidgetSprites SILVER_ICON = new WidgetSprites(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "silver_button"),
            ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "silver_button_highlighted"));

    public static final WidgetSprites GOLD_ICON = new WidgetSprites(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "gold_button"),
            ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "gold_button_highlighted"));

    public static final WidgetSprites CRYSTAL_ICON = new WidgetSprites(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "crystal_button"),
            ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "crystal_button_highlighted"));

    public CustomButton(AbstractContainerScreen<?> parentGui, int xOffset, int yOffset, int xIn, int yIn, int widthIn, int heightIn, WidgetSprites sprites, int handlerButton) {
        super(xIn, yIn, widthIn, heightIn, sprites,
            (button) -> {
                HandlerButton handler = new HandlerButton(handlerButton);
                handler.ExecuteHandlerButton();
            }
        );
        this.parentGui = parentGui;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.setX(parentGui.getGuiLeft() + xOffset);
        this.setY(parentGui.getGuiTop() + yOffset);

        if (parentGui instanceof CreativeModeInventoryScreen creativeScreen) {
            boolean isInventoryTab = creativeScreen.isInventoryOpen();
            this.active = isInventoryTab;

            if (!isInventoryTab) {
                return;
            }
        }

        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);
    }
    
}
