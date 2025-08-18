package net.sirgrantd.magic_coins.items;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
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
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        boolean isEffect = MagicCoinsApi.getPercentageCoinsSaveOnDeath() != 100;
        super.appendHoverText(itemstack, context, list, flag);

        if (isEffect) {
            list.add(Component.translatable("item.prosperity_amulet.description_0.effect_0").withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY)));
            list.add(Component.translatable("item.prosperity_amulet.description_1.effect_0").withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY)));
        } else {
            list.add(Component.translatable("item.prosperity_amulet.description_0.effect_1").withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY)));
            list.add(Component.translatable("item.prosperity_amulet.description_1.effect_1").withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY)));
        }
        
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
