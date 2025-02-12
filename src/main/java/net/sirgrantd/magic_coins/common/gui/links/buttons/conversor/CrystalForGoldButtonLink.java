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
public record CrystalForGoldButtonLink(int x, int y, int z) implements CustomPacketPayload {

    public static final Type<CrystalForGoldButtonLink> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "crystal_for_gold_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrystalForGoldButtonLink> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, CrystalForGoldButtonLink message) -> {
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }, (RegistryFriendlyByteBuf buffer) -> new CrystalForGoldButtonLink(buffer.readInt(), buffer.readInt(), buffer.readInt()));

    @Override
    public Type<CrystalForGoldButtonLink> type() {
        return TYPE;
    }

    public static void handleData(final CrystalForGoldButtonLink message, final IPayloadContext context) {
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
            int crystalCoins = Utils.countItems(itemHandlerModifiable, MagicCoinsItems.CRYSTAL_COIN.get()) * CoinsValuesManager.getValueCrystalCoins();
            
            if (crystalCoins >= CoinsValuesManager.getValueGoldCoins()) {

                int goldCoins = crystalCoins / CoinsValuesManager.getValueGoldCoins();
                int remainingCrystalCoins = crystalCoins % CoinsValuesManager.getValueGoldCoins();

                Utils.removeItemsFromInventory(player, MagicCoinsItems.CRYSTAL_COIN.get(), (crystalCoins / CoinsValuesManager.getValueCrystalCoins()) - (remainingCrystalCoins / CoinsValuesManager.getValueCrystalCoins()));

                ItemStack goldCoin = new ItemStack(MagicCoinsItems.GOLD_COIN.get(), goldCoins);
                if (!player.getInventory().add(goldCoin)) {
                    player.drop(goldCoin, false);
                }
            }
        }

    }

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(CrystalForGoldButtonLink.TYPE, CrystalForGoldButtonLink.STREAM_CODEC, CrystalForGoldButtonLink::handleData);
    }
}
