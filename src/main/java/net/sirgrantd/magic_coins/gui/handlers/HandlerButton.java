package net.sirgrantd.magic_coins.gui.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import net.sirgrantd.magic_coins.MagicCoinsMod;
import net.sirgrantd.magic_coins.gui.handlers.collects.CollectAllCoinsInventory;
import net.sirgrantd.magic_coins.gui.handlers.collects.CollectCrystalCoins;
import net.sirgrantd.magic_coins.gui.handlers.collects.CollectGoldCoins;
import net.sirgrantd.magic_coins.gui.handlers.collects.CollectSilverCoins;
import net.sirgrantd.magic_coins.gui.handlers.conversor.ConvertCrystalForGold;
import net.sirgrantd.magic_coins.gui.handlers.conversor.ConvertGoldForCrystal;
import net.sirgrantd.magic_coins.gui.handlers.conversor.ConvertGoldForSilver;
import net.sirgrantd.magic_coins.gui.handlers.conversor.ConvertSilverForGold;

public class HandlerButton {

    public static final int COLLECT_COINS_FUNC = 0;
    public static final int SILVER_COIN_FUNC = 1;
    public static final int GOLD_COIN_FUNC = 2;
    public static final int CRYSTAL_COIN_FUNC = 3;

    public static final int SILVER_FOR_GOLD_FUNC = 4;
    public static final int GOLD_FOR_CRYSTAL_FUNC = 5;

    public static final int GOLD_FOR_SILVER_FUNC = 6;
    public static final int CRYSTAL_FOR_GOLD_FUNC = 7;
    
    private int funcButton;

    public HandlerButton(int funcButton) {
        this.funcButton = funcButton;
    }

    public void ExecuteHandlerButton() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            try {
                if (this.funcButton == COLLECT_COINS_FUNC) {
                    PacketDistributor.sendToServer(new CollectAllCoinsInventory(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    CollectAllCoinsInventory.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
                else if (this.funcButton == SILVER_COIN_FUNC) {
                    PacketDistributor.sendToServer(new CollectSilverCoins(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    CollectSilverCoins.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
                else if (this.funcButton == GOLD_COIN_FUNC) {
                    PacketDistributor.sendToServer(new CollectGoldCoins(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    CollectGoldCoins.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
                else if (this.funcButton == CRYSTAL_COIN_FUNC) {
                    PacketDistributor.sendToServer(new CollectCrystalCoins(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    CollectCrystalCoins.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
                else if (this.funcButton == SILVER_FOR_GOLD_FUNC) {
                    PacketDistributor.sendToServer(new ConvertSilverForGold(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    ConvertSilverForGold.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
                else if (this.funcButton == GOLD_FOR_CRYSTAL_FUNC) {
                    PacketDistributor.sendToServer(new ConvertGoldForCrystal(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    ConvertGoldForCrystal.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
                else if (this.funcButton == GOLD_FOR_SILVER_FUNC) {
                    PacketDistributor.sendToServer(new ConvertGoldForSilver(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    ConvertGoldForSilver.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
                else if (this.funcButton == CRYSTAL_FOR_GOLD_FUNC) {
                    PacketDistributor.sendToServer(new ConvertCrystalForGold(player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ()));
                    ConvertCrystalForGold.handleButtonAction(player, player.blockPosition().getX(), player.blockPosition().getY(), player.blockPosition().getZ());
                }
            } catch (Exception e) {
                MagicCoinsMod.LOGGER.info(e.getMessage());
            }
        }
    }


}