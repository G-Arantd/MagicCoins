package net.sirgrantd.magic_coins.items;

import net.minecraft.world.item.Rarity;

import net.sirgrantd.magic_coins.api.MagicCoinsApi;

public class SilverCoinItem extends BaseCoinItem {
    public SilverCoinItem() {
        super(Rarity.UNCOMMON);
    }

    @Override
    protected int getCoinValue() {
        return MagicCoinsApi.getValueSilverCoins();
    }
}
