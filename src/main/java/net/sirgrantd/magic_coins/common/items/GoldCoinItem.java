package net.sirgrantd.magic_coins.common.items;

import net.neoforged.api.distmarker.OnlyIn;
import net.sirgrantd.magic_coins.api.BagCoinsManager;
import net.sirgrantd.magic_coins.config.MagicCoinsConfig;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class GoldCoinItem extends Item {
    public static int getCoinsValue() {
        return MagicCoinsConfig.goldCoinsValue;
    }

    public GoldCoinItem() {
        super(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(ItemStack itemstack) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        int coinsInStack = itemStack.getCount();
        BagCoinsManager.addValueTotalInCoins(player, coinsInStack*getCoinsValue());
        itemStack.shrink(coinsInStack);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}