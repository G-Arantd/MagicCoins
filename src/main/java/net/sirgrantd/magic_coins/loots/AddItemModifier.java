package net.sirgrantd.magic_coins.loots;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import java.util.List;
import java.util.Random;

import net.sirgrantd.magic_coins.config.ServerConfig;

public class AddItemModifier extends LootModifier {
    public static final MapCodec<AddItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
        LootModifier.codecStart(inst).and(
            Codec.list(ItemEntry.CODEC.codec()).fieldOf("items").forGetter(e -> e.items)
        ).and(
            ResourceLocation.CODEC.listOf().fieldOf("loot_tables").forGetter(e -> e.lootTables)
        ).apply(inst, AddItemModifier::new)
    );

    private final List<ItemEntry> items;
    private final List<ResourceLocation> lootTables;
    private final Random random = new Random();

    public AddItemModifier(LootItemCondition[] conditionsIn, List<ItemEntry> items, List<ResourceLocation> lootTables) {
        super(conditionsIn);
        this.items = items;
        this.lootTables = lootTables;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext lootContext) {
        if (!ServerConfig.coinsLootChests) return generatedLoot;

        ResourceLocation currentLootTable = lootContext.getQueriedLootTableId();
        if (!lootTables.contains(currentLootTable)) return generatedLoot;

        for (ItemEntry entry : items) {
            if (random.nextFloat() <= entry.chance) {
                int count = entry.min + random.nextInt(entry.max - entry.min + 1);
                generatedLoot.add(new ItemStack(entry.item, count));
            }
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    public static class ItemEntry {
        public static final MapCodec<ItemEntry> CODEC = RecordCodecBuilder.mapCodec(inst ->
            inst.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(itemEntry -> itemEntry.item),
                Codec.FLOAT.fieldOf("chance").forGetter(itemEntry -> itemEntry.chance),
                Codec.INT.fieldOf("min").forGetter(itemEntry -> itemEntry.min),
                Codec.INT.fieldOf("max").forGetter(itemEntry -> itemEntry.max)
            ).apply(inst, ItemEntry::new)
        );

        public final Item item;
        public final float chance;
        public final int min;
        public final int max;

        public ItemEntry(Item item, float chance, int min, int max) {
            this.item = item;
            this.chance = chance;
            this.min = min;
            this.max = max;
        }
    }
}
