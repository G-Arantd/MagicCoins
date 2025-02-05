package net.sirgrantd.magic_coins.init;

import net.sirgrantd.magic_coins.MagicCoinsMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class ItemGroups {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagicCoinsMod.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGIC_COINS = REGISTRY.register("magic_coins", 
    () -> CreativeModeTab.builder().title(Component.translatable("item_group.magic_coins.magic_group")).icon(() -> new ItemStack(MagicCoinsItems.SILVER_COIN.get())).displayItems((parameters, tabData) -> {
        tabData.accept(MagicCoinsItems.SILVER_COIN.get());
        tabData.accept(MagicCoinsItems.GOLD_COIN.get());
        tabData.accept(MagicCoinsItems.CRYSTAL_COIN.get());
        tabData.accept(MagicCoinsItems.PROSPERITY_AMULET.get());
    }).build());
}
