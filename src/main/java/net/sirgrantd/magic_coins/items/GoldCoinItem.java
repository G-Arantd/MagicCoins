package net.sirgrantd.magic_coins.items;

import net.minecraft.world.item.Rarity;

import net.sirgrantd.magic_coins.api.MagicCoinsApi;

public class GoldCoinItem extends BaseCoinItem {
    public GoldCoinItem() {
        super(Rarity.RARE);
    }

    @Override
    protected int getCoinValue() {
        return MagicCoinsApi.getValueGoldCoins();
    }    
}
