package net.sirgrantd.magic_coins.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

import net.sirgrantd.magic_coins.MagicCoinsMod;

public class SoundsInit {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, MagicCoinsMod.MODID);

    public static final Supplier<SoundEvent> MAGIC_BAG_COLLECT_COINS = registerSoundEvent("collect_coins");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, name);
        return SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}
