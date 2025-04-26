package net.sirgrantd.magic_coins.command;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;

import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.sirgrantd.magic_coins.api.MagicCoinsApi;

@EventBusSubscriber
public class CoinsCommand {

    @SubscribeEvent
    public static void registerCoinsCommand(RegisterCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("coins")
        .requires(s -> s.hasPermission(4))
            .then(Commands.literal("add")
                .then(Commands.argument("players", EntityArgument.players())
                    .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                        .executes(arguments -> {
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

                            MagicCoinsApi.addCoins(player, amount);
                            return 0;
                        }))))
            .then(Commands.literal("set")
                .then(Commands.argument("players", EntityArgument.players())
                    .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                        .executes(arguments -> {
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
                            return 0;
                        }))))
            .then(Commands.literal("get")
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(arguments -> {
                        Level world = arguments.getSource().getUnsidedLevel();
                        Entity players = arguments.getSource().getEntity();
                        
                        if (players == null && world instanceof ServerLevel serverLevel) {
                            players = FakePlayerFactory.getMinecraft(serverLevel);
                        }
                        if (players != null) {
                        }

                        Entity player = new Object() {
                            public Entity getEntity() {
                                try {
                                    return EntityArgument.getEntity(arguments, "player");
                                } catch (CommandSyntaxException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }
                        }.getEntity();

                        MagicCoinsApi.getTotalCoins(player);
                        return 0;
                    })))
        );
    }
}
