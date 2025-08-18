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
import net.sirgrantd.magic_coins.api.MagicCoinsApi;
import net.sirgrantd.magic_coins.api.BaseCoinItemApi;

public class GoldCoinItem extends BaseCoinItemApi {
    public GoldCoinItem() {
        super(Rarity.RARE);
    }

    @Override
    protected int getCoinValue() {
        return MagicCoinsApi.getValueGoldCoins();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack itemstack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, context, list, flag);
        Component description = Component.translatable("item.magic_coins.gold_coin.description", getCoinValue())
            .withStyle(ChatFormatting.GRAY)
            .withStyle(ChatFormatting.ITALIC);
        list.add(description);
    }
}
