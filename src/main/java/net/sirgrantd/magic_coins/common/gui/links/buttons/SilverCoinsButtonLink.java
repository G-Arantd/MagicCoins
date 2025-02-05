package net.sirgrantd.magic_coins.common.gui.links.buttons;

import net.sirgrantd.magic_coins.MagicCoinsMod;
import net.sirgrantd.magic_coins.api.BagCoinsManager;
import net.sirgrantd.magic_coins.common.items.SilverCoinItem;
import net.sirgrantd.magic_coins.init.MagicCoinsItems;

import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record SilverCoinsButtonLink(int x, int y, int z) implements CustomPacketPayload {

    public static final Type<SilverCoinsButtonLink> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "silver_coins_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SilverCoinsButtonLink> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SilverCoinsButtonLink message) -> {
        buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
    }, (RegistryFriendlyByteBuf buffer) -> new SilverCoinsButtonLink(buffer.readInt(), buffer.readInt(), buffer.readInt()));

    @Override
    public Type<SilverCoinsButtonLink> type() {
        return TYPE;
    }

    public static void handleData(final SilverCoinsButtonLink message, final IPayloadContext context) {
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
        int valueCoins = SilverCoinItem.getCoinsValue();

        int totalCoins = BagCoinsManager.getValueTotalInCoins(player);

        if (totalCoins >= valueCoins) {
            BagCoinsManager.removeValueTotalInCoins(player, valueCoins);

            ItemStack silverCoin = new ItemStack(MagicCoinsItems.SILVER_COIN.get());
            if (!player.addItem(silverCoin)) {
                player.drop(silverCoin, false);
            }
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(SilverCoinsButtonLink.TYPE, SilverCoinsButtonLink.STREAM_CODEC, SilverCoinsButtonLink::handleData);
    }

}