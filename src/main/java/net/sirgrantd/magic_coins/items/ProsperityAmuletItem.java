package net.sirgrantd.magic_coins.items;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import net.sirgrantd.magic_coins.api.MagicCoinsApi;

public class ProsperityAmuletItem extends Item implements ICurioItem {
    public ProsperityAmuletItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE));
    }
    
    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return true;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        MagicCoinsApi.setIsCoinsLostOnDeath(slotContext.entity(), true);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        MagicCoinsApi.setIsCoinsLostOnDeath(slotContext.entity(), false);
    }
}
