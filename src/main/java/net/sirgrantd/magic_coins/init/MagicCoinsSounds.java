package net.sirgrantd.magic_coins.init;

import net.sirgrantd.magic_coins.MagicCoinsMod;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

public class MagicCoinsSounds {
    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, MagicCoinsMod.MODID);
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC_BAG_COLLECT_COINS = REGISTRY.register("magic_bag.collect_coins",
			() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "magic_bag.collect_coins")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MAGIC_BAG_COINS_INTO_BAG = REGISTRY.register("magic_bag.coins_into_bag",
			() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "magic_bag.coins_into_bag")));
}
