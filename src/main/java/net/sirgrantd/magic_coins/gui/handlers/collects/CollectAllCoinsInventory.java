package net.sirgrantd.magic_coins.gui.handlers.collects;

import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.sirgrantd.magic_coins.MagicCoinsMod;
import net.sirgrantd.magic_coins.api.MagicCoinsApi;
import net.sirgrantd.magic_coins.init.ItemsInit;
import net.sirgrantd.magic_coins.utils.Utils;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record CollectAllCoinsInventory(int x, int y, int z) implements CustomPacketPayload {

    public static final Type<CollectAllCoinsInventory> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "collect_coins_inventory"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CollectAllCoinsInventory> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, CollectAllCoinsInventory message) -> {
        buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
    }, (RegistryFriendlyByteBuf buffer) -> new CollectAllCoinsInventory(buffer.readInt(), buffer.readInt(), buffer.readInt()));

    @Override
    public Type<CollectAllCoinsInventory> type() {
        return TYPE;
    }

    public static void handleData(final CollectAllCoinsInventory message, final IPayloadContext context) {
        if (context.flow() == PacketFlow.SERVERBOUND) {
            context.enqueueWork(() -> {
				Player entity = context.player();
                int x = message.x;
				int y = message.y;
				int z = message.z;
				handleButtonAction(entity, x, y, z);
            }).exceptionally(e -> {
				context.connection().disconnect(Component.literal(e.getMessage()));
				return null;
			});
		}
    }

    public static void handleButtonAction(Player player, int x, int y, int z) {
        if (player == null) {
            return;
        }

        if (player.getCapability(Capabilities.ItemHandler.ENTITY, null) instanceof IItemHandlerModifiable itemHandlerModifiable) {
            int silverCoins = Utils.countItems(itemHandlerModifiable, ItemsInit.SILVER_COIN.get());
            int goldCoins = Utils.countItems(itemHandlerModifiable, ItemsInit.GOLD_COIN.get());
            int crystalCoins = Utils.countItems(itemHandlerModifiable, ItemsInit.CRYSTAL_COIN.get());

            Utils.removeItemsFromInventory(player, ItemsInit.SILVER_COIN.get(), silverCoins);
            Utils.removeItemsFromInventory(player, ItemsInit.GOLD_COIN.get(), goldCoins);
            Utils.removeItemsFromInventory(player, ItemsInit.CRYSTAL_COIN.get(), crystalCoins);

            int totalCoins = 
                silverCoins * MagicCoinsApi.getValueSilverCoins() +
                goldCoins * MagicCoinsApi.getValueGoldCoins() +
                crystalCoins * MagicCoinsApi.getValueCrystalCoins();

            MagicCoinsApi.addCoins(player, totalCoins);
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(CollectAllCoinsInventory.TYPE, CollectAllCoinsInventory.STREAM_CODEC, CollectAllCoinsInventory::handleData);
    }

}