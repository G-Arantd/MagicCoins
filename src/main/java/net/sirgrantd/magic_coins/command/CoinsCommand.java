package net.sirgrantd.magic_coins.command;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.sirgrantd.magic_coins.MagicCoinsMod;
import net.sirgrantd.magic_coins.api.MagicCoinsApi;

@EventBusSubscriber
public class CoinsCommand {

    public record PlayerOnlineInfo(ServerPlayer player, String uuid) {}
    public record PlayerCoinsInfo(String name, int coins) {}

    private static int addCoins(CommandContext<CommandSourceStack> arguments) {
        Level world = arguments.getSource().getUnsidedLevel();
        Entity players = arguments.getSource().getEntity();
        
        if (players == null && world instanceof ServerLevel serverLevel) {
            players = FakePlayerFactory.getMinecraft(serverLevel);
        }
        if (players == null) {
            return 0;
        }

        int amount = IntegerArgumentType.getInteger(arguments, "amount");
        Entity player = new Object() {
            public Entity getEntity() {
                try {
                    return EntityArgument.getEntity(arguments, "players");
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.getEntity();

        MagicCoinsApi.addCoins(player, amount);

        String message = Component.translatable(
            amount == 1 ? "command.coins.add.success.singular" : "command.coins.add.success.plural",
            amount, player.getName().getString()
        ).getString();
        arguments.getSource().sendSystemMessage(
            Component.literal("§a" + message)
        );
        return 1;
    }

    private static int setCoins(CommandContext<CommandSourceStack> arguments) {
        Level world = arguments.getSource().getUnsidedLevel();
        Entity players = arguments.getSource().getEntity();
        
        if (players == null && world instanceof ServerLevel serverLevel) {
            players = FakePlayerFactory.getMinecraft(serverLevel);
        }
        if (players != null) {
        }

        int amount = IntegerArgumentType.getInteger(arguments, "amount");
        Entity player = new Object() {
            public Entity getEntity() {
                try {
                    return EntityArgument.getEntity(arguments, "players");
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }.getEntity();

        MagicCoinsApi.setTotalCoins(player, amount);
        String message = Component.translatable(
            amount == 1 ? "command.coins.set.success.singular" : "command.coins.set.success.plural",
            amount, player.getName().getString()
        ).getString();
        arguments.getSource().sendSystemMessage(
            Component.literal("§a" + message)
        );
        return 1;
    }

    private static int getCoins(CommandContext<CommandSourceStack> arguments, Entity player) {
        Level world = arguments.getSource().getUnsidedLevel();
        Entity players = arguments.getSource().getEntity();

        if (players == null && world instanceof ServerLevel serverLevel) {
            players = FakePlayerFactory.getMinecraft(serverLevel);
        }

        if (players == null) {
            return 0;
        }

        if (player == null) {
            player = arguments.getSource().getEntity();

            if (player == null && arguments.getSource().getUnsidedLevel() instanceof ServerLevel serverLevel) {
                player = FakePlayerFactory.getMinecraft(serverLevel);
            }
        }

        if (player != null) {
            int coins = MagicCoinsApi.getTotalCoins(player);
            
            arguments.getSource().sendSystemMessage(Component.literal(
                String.format("%s: §a$%d", player.getName().getString(), coins)
            ));
        }
        
        return 1;
    }

    private static int rankCoins(CommandContext<CommandSourceStack> arguments, int page) {
        MinecraftServer server = arguments.getSource().getServer();

        Map<String, PlayerOnlineInfo> playersOnlineMap = server.getPlayerList().getPlayers().stream()
            .map(p -> new PlayerOnlineInfo(p, p.getUUID().toString()))
            .collect(Collectors.toMap(PlayerOnlineInfo::uuid, p -> p));

        File playerDataFolder = server.getWorldPath(LevelResource.PLAYER_DATA_DIR).toFile();
        List<PlayerCoinsInfo> ranking = new ArrayList<>();

        for (PlayerOnlineInfo onlineInfo : playersOnlineMap.values()) {
            int coins = MagicCoinsApi.getTotalCoins(onlineInfo.player());
            String name = onlineInfo.player().getName().getString();
            ranking.add(new PlayerCoinsInfo(name, coins));
        }

        File[] files = playerDataFolder.listFiles((dir, name) -> name.endsWith(".dat"));

        if (files != null) {
            for (File file : files) {
                try {
                    String fileUUID = file.getName().replace(".dat", "");

                    int coins = 0;
                    String name = fileUUID;

                    if (!playersOnlineMap.containsKey(fileUUID)) {
                        CompoundTag nbt = NbtIo.readCompressed(file.toPath(), NbtAccounter.unlimitedHeap());
                        UUID uuid = UUID.fromString(fileUUID);
                        name = server.getProfileCache().get(uuid).map(GameProfile::getName).orElse(uuid.toString());

                        coins = 0;
                        if (nbt.contains("neoforge:attachments")) {
                            CompoundTag attachments = nbt.getCompound("neoforge:attachments");
                            String key = MagicCoinsMod.MODID + ":coins_in_bag";
                            if (attachments.contains(key)) {
                                coins = attachments.getCompound(key).getInt("ValueTotalInCoins");
                            }
                        }
                    }
                    ranking.add(new PlayerCoinsInfo(name, coins));
                    

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ranking.sort((a, b) -> Integer.compare(b.coins(), a.coins()));

        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) ranking.size() / pageSize);

        String errorPage = Component.translatable("command.coins.rank.error.page").getString(); 

        if (page < 1 || page > totalPages) {
            arguments.getSource().sendSystemMessage(Component.literal(
                String.format("§c%s %d.", errorPage, totalPages)
            ));
            return 0;
        }

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, ranking.size());

        String title = Component.translatable("command.coins.rank.title").getString();
        String pageText = Component.translatable("command.coins.rank.page").getString();
        arguments.getSource().sendSystemMessage(Component.literal(
            String.format("§6-> %s (%s %d/%d) <-", title, pageText, page, totalPages)
        ));

        for (int i = start; i < end; i++) {
            PlayerCoinsInfo info = ranking.get(i);
            arguments.getSource().sendSystemMessage(Component.literal(
                String.format("§6%d. §f%s: §a$%d", i + 1, info.name(), info.coins())
            ));
        }

        return 1;
    }

    @SubscribeEvent
    public static void registerCoinsCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("coins")
            .then(Commands.literal("add")
                .requires(s -> s.hasPermission(4))
                .then(Commands.argument("players", EntityArgument.players())
                    .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                        .executes(arguments -> {
                            return addCoins(arguments);
                        }))))
            .then(Commands.literal("set")
                .requires(s -> s.hasPermission(4))
                .then(Commands.argument("players", EntityArgument.players())
                    .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                        .executes(arguments -> {
                            return setCoins(arguments);
                        }))))
            .then(Commands.literal("get")
                .requires(s -> s.hasPermission(0))
                    .executes(arguments -> {
                        return getCoins(arguments, null);
                    })
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(arguments -> {
                        Entity player = new Object() {
                            public Entity getEntity() {
                                try {
                                    return EntityArgument.getEntity(arguments, "player");
                                } catch (CommandSyntaxException e) {
                                    arguments.getSource().sendSystemMessage(Component.literal("§cPlayer not found"));
                                    return null;
                                }
                            }
                        }.getEntity();

                        return getCoins(arguments, player);
                    })))
                .then(Commands.literal("rank")
                    .executes(arguments -> {
                        return rankCoins(arguments, 1);
                    })
                    .then(Commands.argument("page", IntegerArgumentType.integer(0))
                        .requires(s -> s.hasPermission(0))
                            .executes(arguments -> {
                                int page = IntegerArgumentType.getInteger(arguments, "page");
                                return rankCoins(arguments, page);
                            })))
        );
    }
}
