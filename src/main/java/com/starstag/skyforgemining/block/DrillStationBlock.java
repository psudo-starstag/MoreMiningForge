package com.starstag.skyforgemining.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkHooks;
import com.starstag.skyforgemining.menu.DrillStationMenu;

public class DrillStationBlock extends Block {

    public DrillStationBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new SimpleMenuProvider(
                            (id, inv, p) -> new DrillStationMenu(id, inv),
                            Component.literal("§6Drill Station")
                    )
            );
        }
        return InteractionResult.SUCCESS;
    }
}