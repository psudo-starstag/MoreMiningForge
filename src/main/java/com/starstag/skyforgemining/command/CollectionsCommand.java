package com.starstag.skyforgemining.command;

import com.mojang.brigadier.CommandDispatcher;
import com.starstag.skyforgemining.collection.CollectionMenu;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CollectionsCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {

        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("collections")
                        .executes(context -> {

                            ServerPlayer player = context.getSource().getPlayer();
                            player.openMenu(new CollectionMenu());

                            return 1;
                        })
        );
    }
}