package com.starstag.skyforgemining.mining;

import net.minecraft.network.chat.Component;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.starstag.skyforgemining.SkyforgeMining;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import com.starstag.skyforgemining.player.MiningProvider;
import net.minecraftforge.event.entity.player.PlayerEvent;

@Mod.EventBusSubscriber(modid = SkyforgeMining.MODID)
public class MiningEvents {

    private static int miningXp = 0;
    private static int miningLevel = 1;

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {

        // Only copy if the player died
        if (!event.isWasDeath()) return;

        event.getOriginal().getCapability(MiningProvider.MINING_CAP).ifPresent(oldData -> {

            event.getEntity().getCapability(MiningProvider.MINING_CAP).ifPresent(newData -> {

                newData.setMiningLevel(oldData.getMiningLevel());
                newData.setMiningXp(oldData.getMiningXp());

            });

        });
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(
                    new ResourceLocation("skyforgemining", "mining_data"),
                    new MiningProvider()
            );
        }
    }
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {

        if (event.getPlayer().level().isClientSide) return;

        var player = event.getPlayer();
        var heldItem = player.getMainHandItem();

        // XP SYSTEM
        player.getCapability(MiningProvider.MINING_CAP).ifPresent(data -> {

            data.addXp(10); // 10 XP per block

            int xpRequired = (int)(100 * Math.pow(data.getMiningLevel(), 1.4));

            if (data.getMiningXp() >= xpRequired) {
                data.levelUp();
                player.sendSystemMessage(
                        Component.literal("§6Mining Level Up! §eLevel " + data.getMiningLevel())
                );
            }
        });

        // FORTUNE SYSTEM
        if (heldItem.getItem() instanceof com.starstag.skyforgemining.item.DrillItem drill) {

            int baseFortune = drill.getBaseMiningFortune();
            int levelFortune = getMiningFortuneBonus();
            int totalFortune = baseFortune + levelFortune;

            int extraDrops = totalFortune / 10; // 10 fortune = 1 extra drop

            if (extraDrops > 0) {

                var blockState = event.getState();
                var drops = net.minecraft.world.level.block.Block.getDrops(
                        blockState,
                        (net.minecraft.server.level.ServerLevel) player.level(),
                        event.getPos(),
                        null,
                        player,
                        heldItem
                );

                for (var stack : drops) {
                    stack.grow(extraDrops);
                    net.minecraft.world.level.block.Block.popResource(
                            player.level(),
                            event.getPos(),
                            stack
                    );
                }
            }
        }
    }

    public static int getMiningLevel() {
        return miningLevel;
    }

    public static int getMiningSpeedBonus() {
        return miningLevel * 2; // 2 speed per level
    }

    public static int getMiningFortuneBonus() {
        return miningLevel; // 1 fortune per level
    }
}