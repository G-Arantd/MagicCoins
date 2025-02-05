package net.sirgrantd.magic_coins.common.events;

import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.sirgrantd.magic_coins.api.BagCoinsManager;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;

@EventBusSubscriber
public class LossCoinsForDeath {
    @SubscribeEvent
	public static void onPlayerDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player player) {
            handlePlayerDeath(player);
        }
    }

    private static void handlePlayerDeath(Player player) {
        int totalCoins = BagCoinsManager.getValueTotalInCoins(player);
        boolean isCoinsLostOnDeath = BagCoinsManager.getIsCoinsLostOnDeath(player);

        if (totalCoins > 0) {
            double lossFactor = isCoinsLostOnDeath ? 1.0 : 0.5;

            int newTotalCoins = (int) Math.round(totalCoins * lossFactor);
            BagCoinsManager.setValueTotalInCoins(player, newTotalCoins);
        }
    }
}
