package net.sirgrantd.magic_coins.common.gui.links.buttons;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.sirgrantd.magic_coins.MagicCoinsMod;

public class HandlerButton {

    public static final int COLLECT_COINS_FUNC = 0;
    public static final int SILVER_COIN_FUNC = 1;
    public static final int GOLD_COIN_FUNC = 2;
    public static final int CRYSTAL_COIN_FUNC = 3;
    
    private int funcButton;

    public HandlerButton(int funcButton) {
        this.funcButton = funcButton;
    }

    public void ExecuteHandlerButton() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            try {
                if (this.funcButton == COLLECT_COINS_FUNC) {
                    PacketDistributor.sendToServer(new CollectCoinsInventoryLink(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    CollectCoinsInventoryLink.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
                else if (this.funcButton == SILVER_COIN_FUNC) {
                    PacketDistributor.sendToServer(new SilverCoinsButtonLink(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    SilverCoinsButtonLink.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
                else if (this.funcButton == GOLD_COIN_FUNC) {
                    PacketDistributor.sendToServer(new GoldCoinsButtonLink(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    GoldCoinsButtonLink.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
                else if (this.funcButton == CRYSTAL_COIN_FUNC) {
                    PacketDistributor.sendToServer(new CrystalCoinsButtonLink(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    CrystalCoinsButtonLink.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
            } catch (Exception e) {
                MagicCoinsMod.LOGGER.info(e.getMessage());
            }
        }
    }


}
