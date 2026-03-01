package com.starstag.skyforgemining.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.starstag.skyforgemining.menu.MiningMenu;

@Mod.EventBusSubscriber
public class MiningCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("mining")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayer();
                            player.openMenu(new MiningMenu());
                            return 1;
                        })
        );
    }
}