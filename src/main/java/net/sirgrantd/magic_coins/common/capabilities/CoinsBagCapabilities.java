package net.sirgrantd.magic_coins.common.capabilities;

import net.sirgrantd.magic_coins.MagicCoinsMod;

import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.HolderLookup;

import java.util.function.Supplier;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class CoinsBagCapabilities {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MagicCoinsMod.MODID);
    public static final Supplier<AttachmentType<CoinsInBag>> COINS_IN_BAG = ATTACHMENT_TYPES.register("coins_in_bag", () -> AttachmentType.serializable(() -> new CoinsInBag()).build());

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        MagicCoinsMod.addNetworkMessage(CoinsInBagSyncMessage.TYPE, CoinsInBagSyncMessage.STREAM_CODEC, CoinsInBagSyncMessage::handleData);
    }

    @EventBusSubscriber
    public static class EventBusHandlers {
        @SubscribeEvent
        public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getData(COINS_IN_BAG).syncCoinsInBag(event.getEntity());
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getData(COINS_IN_BAG).syncCoinsInBag(event.getEntity());
            }
        }

        @SubscribeEvent
        public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                player.getData(COINS_IN_BAG).syncCoinsInBag(event.getEntity());
            }
        }

        @SubscribeEvent
        public static void clonePlayer(PlayerEvent.Clone event) {
            CoinsInBag original = event.getOriginal().getData(COINS_IN_BAG);
            CoinsInBag clone = new CoinsInBag();
            clone.valueTotalInCoins = original.valueTotalInCoins;
            clone.isCoinsLostOnDeath = original.isCoinsLostOnDeath;
            if (!event.isWasDeath()) {
			}
            event.getEntity().setData(COINS_IN_BAG, clone);
        }
    }

    public static class CoinsInBag implements INBTSerializable<CompoundTag> {
        public int valueTotalInCoins = 0;
        public boolean isCoinsLostOnDeath = false;

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider lookupProvider) {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("ValueTotalInCoins", valueTotalInCoins);
            nbt.putBoolean("IsCoinsLostOnDeath", isCoinsLostOnDeath);
            return nbt;
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider lookupProvider, CompoundTag nbt) {
            valueTotalInCoins = nbt.getInt("ValueTotalInCoins");
            isCoinsLostOnDeath = nbt.getBoolean("IsCoinsLostOnDeath");
        }

        public void syncCoinsInBag(Entity entity) {
            if (entity instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new CoinsInBagSyncMessage(this));
            }
        }
    }

    public record CoinsInBagSyncMessage(CoinsInBag data) implements CustomPacketPayload {
        public static final Type<CoinsInBagSyncMessage> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MagicCoinsMod.MODID, "coins_in_bag_sync"));
        public static final StreamCodec<RegistryFriendlyByteBuf, CoinsInBagSyncMessage> STREAM_CODEC = StreamCodec.of(
            (RegistryFriendlyByteBuf buffer, CoinsInBagSyncMessage message) -> buffer.writeNbt(message.data().serializeNBT(buffer.registryAccess())),
            (RegistryFriendlyByteBuf buffer) -> {
                CoinsInBagSyncMessage message = new CoinsInBagSyncMessage(new CoinsInBag());
                message.data.deserializeNBT(buffer.registryAccess(), buffer.readNbt());
                return message;
            }
        );

        @Override
        public Type<CoinsInBagSyncMessage> type() {
            return TYPE;
        }

        public static void handleData(final CoinsInBagSyncMessage message, final IPayloadContext context) {
            if (context.flow() == PacketFlow.CLIENTBOUND && message.data != null) {
                context.enqueueWork(() -> context.player().getData(COINS_IN_BAG).deserializeNBT(context.player().registryAccess(), message.data.serializeNBT(context.player().registryAccess()))).exceptionally(e -> {
                    context.connection().disconnect(Component.literal(e.getMessage()));
                    return null;
                });
            }
        }
    }
}
