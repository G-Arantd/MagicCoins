package net.sirgrantd.magic_coins.features;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.sirgrantd.magic_coins.api.MagicCoinsApi;
import net.sirgrantd.magic_coins.config.ServerConfig;

@EventBusSubscriber
public class LostCoinsForDeath {
    @SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player player) {
            handlePlayerDeath(player);
        }
    }

    private static void handlePlayerDeath(Player player) {
        int totalCoins = MagicCoinsApi.getTotalCoins(player);
        boolean isCoinsLostOnDeath = MagicCoinsApi.isCoinsLostOnDeath(player);

        if (totalCoins > 0) {
            double lossFactor = isCoinsLostOnDeath ? 1.0 : ServerConfig.percentageCoinsLostOnDeath/100.0;

            int newTotalCoins = (int) Math.round(totalCoins * lossFactor);
            MagicCoinsApi.setTotalCoins(player, newTotalCoins);
        }
    }
}
