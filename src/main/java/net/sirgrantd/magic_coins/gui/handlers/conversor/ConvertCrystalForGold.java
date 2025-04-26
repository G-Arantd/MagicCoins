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
public record ConvertCrystalForGold(int x, int y, int z) implements CustomPacketPayload {

    public static final Type<ConvertCrystalForGold> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "crystal_for_gold_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ConvertCrystalForGold> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, ConvertCrystalForGold message) -> {
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }, (RegistryFriendlyByteBuf buffer) -> new ConvertCrystalForGold(buffer.readInt(), buffer.readInt(), buffer.readInt()));

    @Override
    public Type<ConvertCrystalForGold> type() {
        return TYPE;
    }

    public static void handleData(final ConvertCrystalForGold message, final IPayloadContext context) {
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
            int crystalCoins = Utils.countItems(itemHandlerModifiable, ItemsInit.CRYSTAL_COIN.get()) * MagicCoinsApi.getValueCrystalCoins();
            
            if (crystalCoins >= MagicCoinsApi.getValueGoldCoins()) {

                int goldCoins = crystalCoins / MagicCoinsApi.getValueGoldCoins();
                int remainingCrystalCoins = crystalCoins % MagicCoinsApi.getValueGoldCoins();

                Utils.removeItemsFromInventory(player, ItemsInit.CRYSTAL_COIN.get(), (crystalCoins / MagicCoinsApi.getValueCrystalCoins()) - (remainingCrystalCoins / MagicCoinsApi.getValueCrystalCoins()));

                ItemStack goldCoin = new ItemStack(ItemsInit.GOLD_COIN.get(), goldCoins);
                if (!player.getInventory().add(goldCoin)) {
                    player.drop(goldCoin, false);
                }
            }
        }

    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(ConvertCrystalForGold.TYPE, ConvertCrystalForGold.STREAM_CODEC, ConvertCrystalForGold::handleData);
    }
}
