package net.sirgrantd.magic_coins.gui.handlers.conversor;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import net.sirgrantd.magic_coins.MagicCoinsMod;
import net.sirgrantd.magic_coins.api.MagicCoinsApi;
import net.sirgrantd.magic_coins.init.ItemsInit;
import net.sirgrantd.magic_coins.utils.Utils;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record ConvertGoldForSilver(int x, int y, int z) implements CustomPacketPayload {

    public static final Type<ConvertGoldForSilver> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "gold_for_silver_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ConvertGoldForSilver> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ConvertGoldForSilver message) -> {
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }, (RegistryFriendlyByteBuf buffer) -> new ConvertGoldForSilver(buffer.readInt(), buffer.readInt(), buffer.readInt()));

    @Override
    public Type<ConvertGoldForSilver> type() {
        return TYPE;
    }

    public static void handleData(final ConvertGoldForSilver message, final IPayloadContext context) {
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
            int goldCoins = Utils.countItems(itemHandlerModifiable, ItemsInit.GOLD_COIN.get()) * MagicCoinsApi.getValueGoldCoins();
            
            if (goldCoins >= MagicCoinsApi.getValueSilverCoins()) {

                int silverCoins = goldCoins / MagicCoinsApi.getValueSilverCoins();
                int remainingGoldCoins = goldCoins % MagicCoinsApi.getValueSilverCoins();

                Utils.removeItemsFromInventory(player, ItemsInit.GOLD_COIN.get(), (goldCoins / MagicCoinsApi.getValueGoldCoins()) - (remainingGoldCoins / MagicCoinsApi.getValueGoldCoins()));

                ItemStack silverCoin = new ItemStack(ItemsInit.SILVER_COIN.get(), silverCoins);
                if (!player.getInventory().add(silverCoin)) {
                    player.drop(silverCoin, false);
                }
            }
        }

    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(ConvertGoldForSilver.TYPE, ConvertGoldForSilver.STREAM_CODEC, ConvertGoldForSilver::handleData);
    }
}
