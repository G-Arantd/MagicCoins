package net.sirgrantd.magic_coins.common.utils;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.items.IItemHandler;

public class Utils {

    public static int countItems(IItemHandler itemHandler, Item item) {
        int count = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i).getItem() == item) {
                count += itemHandler.getStackInSlot(i).getCount();
            }
        }
        return count;
    }

    public static void removeItemsFromInventory(Player player, Item item, int quantity) {
        player.getInventory().clearOrCountMatchingItems(
            stack -> stack.getItem() == item, quantity, player.inventoryMenu.getCraftSlots()
        );
    }
        
}
