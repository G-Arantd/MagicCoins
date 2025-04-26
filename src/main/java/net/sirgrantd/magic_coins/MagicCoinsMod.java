package net.sirgrantd.magic_coins;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.util.thread.SidedThreadGroups;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;

import net.minecraft.util.Tuple;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.FriendlyByteBuf;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collection;
import java.util.ArrayList;

import net.sirgrantd.magic_coins.capabilities.CoinsBagCapabilities;
import net.sirgrantd.magic_coins.config.ClientConfig;
import net.sirgrantd.magic_coins.config.ServerConfig;
import net.sirgrantd.magic_coins.gui.MagicCoinsButtonInventory;
import net.sirgrantd.magic_coins.init.ItemsInit;
import net.sirgrantd.magic_coins.init.LootInit;
import net.sirgrantd.magic_coins.init.SoundsInit;
import net.sirgrantd.magic_coins.init.TabsInit;

@Mod("magic_coins")
public class MagicCoinsMod {
	public static final Logger LOGGER = LogManager.getLogger(MagicCoinsMod.class);
	public static final String MODID = "magic_coins";

	public MagicCoinsMod(IEventBus modEventBus, ModContainer modContainer) {
		NeoForge.EVENT_BUS.register(this);
		modEventBus.addListener(this::registerNetworking);

		TabsInit.REGISTRY.register(modEventBus);
		ItemsInit.REGISTRY.register(modEventBus);

		LootInit.register(modEventBus);
		SoundsInit.register(modEventBus);

		CoinsBagCapabilities.ATTACHMENT_TYPES.register(modEventBus);

		modContainer.registerConfig(ModConfig.Type.CLIENT, ClientConfig.Config.SPEC, String.format("%s-client.toml", MODID));
		modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.Config.SPEC, String.format("%s-server.toml", MODID));
	}

	@EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
	public static class ClientProxy {
		
		@SubscribeEvent
		public static void setupClient(FMLClientSetupEvent event) {
			NeoForge.EVENT_BUS.register(new MagicCoinsButtonInventory());
		}
	}

	private static boolean networkingRegistered = false;
	private static final Map<CustomPacketPayload.Type<?>, NetworkMessage<?>> MESSAGES = new HashMap<>();

	private record NetworkMessage<T extends CustomPacketPayload>(StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
	}

	public static <T extends CustomPacketPayload> void addNetworkMessage(CustomPacketPayload.Type<T> id, StreamCodec<? extends FriendlyByteBuf, T> reader, IPayloadHandler<T> handler) {
		if (networkingRegistered)
			throw new IllegalStateException("Cannot register new network messages after networking has been registered");
		MESSAGES.put(id, new NetworkMessage<>(reader, handler));
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	private void registerNetworking(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(MODID);
		MESSAGES.forEach((id, networkMessage) -> registrar.playBidirectional(id, ((NetworkMessage) networkMessage).reader(), ((NetworkMessage) networkMessage).handler()));
		networkingRegistered = true;
	}

	private static final Collection<Tuple<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

	public static void queueServerWork(int tick, Runnable action) {
		if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
			workQueue.add(new Tuple<>(action, tick));
	}

	@SubscribeEvent
	public void tick(ServerTickEvent.Post event) {
		List<Tuple<Runnable, Integer>> actions = new ArrayList<>();
		workQueue.forEach(work -> {
			work.setB(work.getB() - 1);
			if (work.getB() == 0)
				actions.add(work);
		});
		actions.forEach(e -> e.getA().run());
		workQueue.removeAll(actions);
	}
}
