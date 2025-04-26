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
public record CollectSilverCoins(int x, int y, int z) implements CustomPacketPayload {

    public static final Type<CollectSilverCoins> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "silver_coins_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CollectSilverCoins> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, CollectSilverCoins message) -> {
        buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
    }, (RegistryFriendlyByteBuf buffer) -> new CollectSilverCoins(buffer.readInt(), buffer.readInt(), buffer.readInt()));

    @Override
    public Type<CollectSilverCoins> type() {
        return TYPE;
    }

    public static void handleData(final CollectSilverCoins message, final IPayloadContext context) {
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
        int valueCoins = MagicCoinsApi.getValueSilverCoins();

        int totalCoins = MagicCoinsApi.getTotalCoins(player);

        if (totalCoins >= valueCoins) {
            MagicCoinsApi.removeCoins(player, valueCoins);

            ItemStack silverCoin = new ItemStack(ItemsInit.SILVER_COIN.get());
            if (!player.addItem(silverCoin)) {
                player.drop(silverCoin, false);
            }
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(CollectSilverCoins.TYPE, CollectSilverCoins.STREAM_CODEC, CollectSilverCoins::handleData);
    }

}