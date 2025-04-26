package net.sirgrantd.magic_coins.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.sirgrantd.magic_coins.MagicCoinsMod;

public class TabsInit {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MagicCoinsMod.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGIC_COINS = REGISTRY.register("magic_coins", 
    () -> CreativeModeTab.builder().title(Component.translatable("item_group.magic_coins.magic_group")).icon(() -> new ItemStack(ItemsInit.SILVER_COIN.get())).displayItems((parameters, tabData) -> {
        tabData.accept(ItemsInit.SILVER_COIN.get());
        tabData.accept(ItemsInit.GOLD_COIN.get());
        tabData.accept(ItemsInit.CRYSTAL_COIN.get());
        tabData.accept(ItemsInit.PROSPERITY_AMULET.get());
    }).build());
}
