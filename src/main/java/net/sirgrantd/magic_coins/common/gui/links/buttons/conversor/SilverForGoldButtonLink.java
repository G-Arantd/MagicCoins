package net.sirgrantd.magic_coins.common.gui.links.buttons.conversor;

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
import net.sirgrantd.magic_coins.api.CoinsValuesManager;
import net.sirgrantd.magic_coins.common.utils.Utils;
import net.sirgrantd.magic_coins.init.MagicCoinsItems;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record SilverForGoldButtonLink(int x, int y, int z) implements CustomPacketPayload {
    
    public static final Type<SilverForGoldButtonLink> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "silver_for_gold_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SilverForGoldButtonLink> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, SilverForGoldButtonLink message) -> {
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }, (RegistryFriendlyByteBuf buffer) -> new SilverForGoldButtonLink(buffer.readInt(), buffer.readInt(), buffer.readInt()));

    @Override
    public Type<SilverForGoldButtonLink> type() {
        return TYPE;
    }

    public static void handleData(final SilverForGoldButtonLink message, final IPayloadContext context) {
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
            int silverCoins = Utils.countItems(itemHandlerModifiable, MagicCoinsItems.SILVER_COIN.get());
            
            if (silverCoins >= CoinsValuesManager.getValueGoldCoins()) {

                int goldCoins = silverCoins / CoinsValuesManager.getValueGoldCoins();
                int remainingSilverCoins = silverCoins % CoinsValuesManager.getValueGoldCoins();

                Utils.removeItemsFromInventory(player, MagicCoinsItems.SILVER_COIN.get(), silverCoins-remainingSilverCoins);

                ItemStack goldCoin = new ItemStack(MagicCoinsItems.GOLD_COIN.get(), goldCoins);
                if (!player.getInventory().add(goldCoin)) {
                    player.drop(goldCoin, false);
                }
            }
        }
        
    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(SilverForGoldButtonLink.TYPE, SilverForGoldButtonLink.STREAM_CODEC, SilverForGoldButtonLink::handleData);
    }
}
