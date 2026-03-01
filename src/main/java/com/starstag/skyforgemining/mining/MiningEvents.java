package com.starstag.skyforgemining.mining;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.collection.CollectionProvider;
import com.starstag.skyforgemining.collection.CollectionType;
import com.starstag.skyforgemining.item.DrillItem;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = SkyforgeMining.MODID)
public class MiningEvents {

    // Track player-placed blocks
    private static final Set<BlockPos> placedBlocks = new HashSet<>();



    /*
     * =========================
     * Track Player Placed Blocks
     * =========================
     */
    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel().isClientSide()) return;

        if (event.getEntity() instanceof Player) {
            placedBlocks.add(event.getPos().immutable());
        }
    }

    /*
     * =========================
     * Collection Logic
     * =========================
     */
    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(
                    new ResourceLocation("skyforgemining", "collections"),
                    new CollectionProvider()
            );
        }
    }
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {

        if (!event.isWasDeath()) return;

        event.getOriginal().reviveCaps();

        event.getOriginal().getCapability(CollectionProvider.COLLECTION_CAP).ifPresent(oldData -> {
            event.getEntity().getCapability(CollectionProvider.COLLECTION_CAP).ifPresent(newData -> {
                newData.loadNBT(oldData.saveNBT());
            });
        });

        event.getOriginal().invalidateCaps();
    }

    /*
     * =========================
     * Block Break Logic
     * =========================
     */
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {

        if (event.getPlayer().level().isClientSide) return;

        var player = event.getPlayer();
        var state = event.getState();
        var heldItem = player.getMainHandItem();

        // Ignore placed blocks
        if (placedBlocks.contains(event.getPos())) {
            placedBlocks.remove(event.getPos());
            return;
        }

        // Drill XP
        if (heldItem.getItem() instanceof DrillItem drill) {
            int xp = MiningXpType.getXpForBlock(state.getBlock());
            drill.addXp(heldItem, xp);
        }
    }

    @SubscribeEvent
    public static void onItemPickup(net.minecraftforge.event.entity.player.PlayerEvent.ItemPickupEvent event) {

        var player = event.getEntity();
        var stack = event.getStack();

        CollectionType type = CollectionType.fromItem(stack.getItem());

        if (type != null) {
            player.getCapability(CollectionProvider.COLLECTION_CAP).ifPresent(data -> {
                data.add(type, stack.getCount());
            });
        }
    }
}