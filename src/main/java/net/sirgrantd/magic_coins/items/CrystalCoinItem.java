package net.sirgrantd.magic_coins.items;

import net.minecraft.world.item.Rarity;

import net.sirgrantd.magic_coins.api.MagicCoinsApi;

public class CrystalCoinItem extends BaseCoinItem {
    public CrystalCoinItem() {
        super(Rarity.EPIC);
    }

    @Override
    protected int getCoinValue() {
        return MagicCoinsApi.getValueCrystalCoins();
    }
}

