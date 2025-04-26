package net.sirgrantd.magic_coins.init;

import net.minecraft.world.item.Item;

import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.sirgrantd.magic_coins.MagicCoinsMod;
import net.sirgrantd.magic_coins.items.SilverCoinItem;
import net.sirgrantd.magic_coins.items.GoldCoinItem;
import net.sirgrantd.magic_coins.items.CrystalCoinItem;
import net.sirgrantd.magic_coins.items.ProsperityAmuletItem;

public class ItemsInit {
    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(MagicCoinsMod.MODID);
    public static final DeferredItem<Item> SILVER_COIN = REGISTRY.register("silver_coin", SilverCoinItem::new);
    public static final DeferredItem<Item> GOLD_COIN = REGISTRY.register("gold_coin", GoldCoinItem::new);
    public static final DeferredItem<Item> CRYSTAL_COIN = REGISTRY.register("crystal_coin", CrystalCoinItem::new);
    public static final DeferredItem<Item> PROSPERITY_AMULET = REGISTRY.register("prosperity_amulet", ProsperityAmuletItem::new);
}