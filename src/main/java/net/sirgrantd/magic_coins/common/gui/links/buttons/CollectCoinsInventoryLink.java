package net.sirgrantd.magic_coins.common.gui.links.buttons;

import net.sirgrantd.magic_coins.MagicCoinsMod;
import net.sirgrantd.magic_coins.api.BagCoinsManager;
import net.sirgrantd.magic_coins.common.items.CrystalCoinItem;
import net.sirgrantd.magic_coins.common.items.GoldCoinItem;
import net.sirgrantd.magic_coins.common.items.SilverCoinItem;
import net.sirgrantd.magic_coins.init.MagicCoinsItems;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public record CollectCoinsInventoryLink(int x, int y, int z) implements CustomPacketPayload {

    public static final Type<CollectCoinsInventoryLink> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "collect_coins_inventory"));
    public static final StreamCodec<RegistryFriendlyByteBuf, CollectCoinsInventoryLink> STREAM_CODEC = StreamCodec.of((RegistryFriendlyByteBuf buffer, CollectCoinsInventoryLink message) -> {
        buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
    }, (RegistryFriendlyByteBuf buffer) -> new CollectCoinsInventoryLink(buffer.readInt(), buffer.readInt(), buffer.readInt()));

    @Override
    public Type<CollectCoinsInventoryLink> type() {
        return TYPE;
    }

    public static void handleData(final CollectCoinsInventoryLink message, final IPayloadContext context) {
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
            int silverCoins = countItems(itemHandlerModifiable, MagicCoinsItems.SILVER_COIN.get());
            int goldCoins = countItems(itemHandlerModifiable, MagicCoinsItems.GOLD_COIN.get());
            int crystalCoins = countItems(itemHandlerModifiable, MagicCoinsItems.CRYSTAL_COIN.get());

            removeItemsFromInventory(player, MagicCoinsItems.SILVER_COIN.get(), silverCoins);
            removeItemsFromInventory(player, MagicCoinsItems.GOLD_COIN.get(), goldCoins);
            removeItemsFromInventory(player, MagicCoinsItems.CRYSTAL_COIN.get(), crystalCoins);

            int totalCoins = 
                silverCoins * SilverCoinItem.getCoinsValue() +
                goldCoins * GoldCoinItem.getCoinsValue() +
                crystalCoins * CrystalCoinItem.getCoinsValue();

            BagCoinsManager.addValueTotalInCoins(player, totalCoins);
        }
    }

    private static int countItems(IItemHandler itemHandler, Item item) {
        int count = 0;
        for (int slotIndex = 0; slotIndex < itemHandler.getSlots(); slotIndex++) {
            ItemStack itemStack = itemHandler.getStackInSlot(slotIndex);
            if (itemStack.getItem() == item) {
                count += itemStack.getCount();
            }
        }
        return count;
    }

    private static void removeItemsFromInventory(Player player, Item item, int quantity) {
        player.getInventory().clearOrCountMatchingItems(
            stack -> stack.getItem() == item, quantity, player.inventoryMenu.getCraftSlots()
        );
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(CollectCoinsInventoryLink.TYPE, CollectCoinsInventoryLink.STREAM_CODEC, CollectCoinsInventoryLink::handleData);
    }

}