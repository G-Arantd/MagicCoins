package net.sirgrantd.magic_coins.common.gui.links.buttons.collects;

import net.sirgrantd.magic_coins.MagicCoinsMod;
import net.sirgrantd.magic_coins.api.BagCoinsManager;
import net.sirgrantd.magic_coins.common.items.GoldCoinItem;
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
public record GoldCoinsButtonLink(int x, int y, int z) implements CustomPacketPayload {

    public static final Type<GoldCoinsButtonLink> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "gold_coins_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GoldCoinsButtonLink> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, GoldCoinsButtonLink message) -> {
        buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
    }, (RegistryFriendlyByteBuf buffer) -> new GoldCoinsButtonLink(buffer.readInt(), buffer.readInt(), buffer.readInt()));

    @Override
    public Type<GoldCoinsButtonLink> type() {
        return TYPE;
    }

    public static void handleData(final GoldCoinsButtonLink message, final IPayloadContext context) {
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
        int valueCoins = GoldCoinItem.getCoinsValue();

        int totalCoins = BagCoinsManager.getValueTotalInCoins(player);

        if (totalCoins >= valueCoins) {
            BagCoinsManager.removeValueTotalInCoins(player, valueCoins);

            ItemStack goldCoin = new ItemStack(MagicCoinsItems.GOLD_COIN.get());
            if (!player.addItem(goldCoin)) {
                player.drop(goldCoin, false);
            }
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(GoldCoinsButtonLink.TYPE, GoldCoinsButtonLink.STREAM_CODEC, GoldCoinsButtonLink::handleData);
    }

}