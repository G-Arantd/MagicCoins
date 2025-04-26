package net.sirgrantd.magic_coins.gui.handlers.collects;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.sirgrantd.magic_coins.MagicCoinsMod;
import net.sirgrantd.magic_coins.api.MagicCoinsApi;
import net.sirgrantd.magic_coins.init.ItemsInit;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record CollectGoldCoins(int x, int y, int z) implements CustomPacketPayload {

    public static final Type<CollectGoldCoins> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "gold_coins_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CollectGoldCoins> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, CollectGoldCoins message) -> {
        buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
    }, (RegistryFriendlyByteBuf buffer) -> new CollectGoldCoins(buffer.readInt(), buffer.readInt(), buffer.readInt()));

    @Override
    public Type<CollectGoldCoins> type() {
        return TYPE;
    }

    public static void handleData(final CollectGoldCoins message, final IPayloadContext context) {
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
        int valueCoins = MagicCoinsApi.getValueGoldCoins();

        int totalCoins = MagicCoinsApi.getTotalCoins(player);

        if (totalCoins >= valueCoins) {
            MagicCoinsApi.removeCoins(player, valueCoins);

            ItemStack goldCoin = new ItemStack(ItemsInit.GOLD_COIN.get());
            if (!player.addItem(goldCoin)) {
                player.drop(goldCoin, false);
            }
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(CollectGoldCoins.TYPE, CollectGoldCoins.STREAM_CODEC, CollectGoldCoins::handleData);
    }

}