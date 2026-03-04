package com.starstag.skyforgemining.mining;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.collection.CollectionProvider;
import com.starstag.skyforgemining.collection.CollectionType;
import com.starstag.skyforgemining.item.DrillItem;
import com.starstag.skyforgemining.item.sack.SackFilter;
import com.starstag.skyforgemining.item.sack.SackItem;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Mod.EventBusSubscriber(modid = SkyforgeMining.MODID)
public class MiningEvents {

    private static final Set<BlockPos> placedBlocks = new HashSet<>();
    private static final Random random = new Random();

    // =========================
    // TRACK PLACED BLOCKS
    // =========================

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel().isClientSide()) return;
        if (event.getEntity() instanceof Player) {
            placedBlocks.add(event.getPos().immutable());
        }
    }

    // =========================
    // CAPABILITIES
    // =========================

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

    // =========================
    // BLOCK BREAK — XP + FORTUNE + PRISTINE
    // =========================

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.getPlayer().level().isClientSide()) return;

        Player player = event.getPlayer();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        ItemStack heldItem = player.getMainHandItem();

        // Skip player-placed blocks
        if (placedBlocks.contains(pos)) {
            placedBlocks.remove(pos);
            return;
        }

        if (!(heldItem.getItem() instanceof DrillItem drill)) return;

        // Award drill XP
        int xp = MiningXpType.getXpForBlock(state.getBlock());
        drill.addXp(heldItem, xp);

        // Fortune and Pristine drop handling
        int fortune = drill.getTotalFortune(heldItem);
        int pristine = heldItem.getOrCreateTag().getInt("pristine");

        // Only modify drops if we have fortune or pristine
        if (fortune <= 0 && pristine <= 0) return;

        ServerLevel serverLevel = (ServerLevel) player.level();

        // Cancel default drops — we'll spawn them ourselves
        event.setCanceled(true);

        // Remove the block
        serverLevel.removeBlock(pos, false);

        // Get base drops
        LootParams.Builder builder = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.TOOL, heldItem)
                .withParameter(LootContextParams.THIS_ENTITY, player)
                .withOptionalParameter(LootContextParams.BLOCK_ENTITY,
                        serverLevel.getBlockEntity(pos));

        List<ItemStack> drops = state.getDrops(builder);

        for (ItemStack drop : drops) {
            if (drop.isEmpty()) continue;

            int count = drop.getCount();

            // Fortune: every 10 fortune = +1 extra drop
            int extraFromFortune = fortune / 10;
            count += extraFromFortune;

            // Pristine: each point = 1% chance to double the drop
            if (pristine > 0) {
                float pristineChance = pristine / 100f;
                if (random.nextFloat() < pristineChance) {
                    count *= 2;
                }
            }

            drop.setCount(count);

            // Spawn the drop
            ItemEntity itemEntity = new ItemEntity(
                    serverLevel,
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    drop
            );
            serverLevel.addFreshEntity(itemEntity);
        }
    }

    private static void moveInventoryItemsIntoSacks(Player player) {

        for (int i = 0; i < player.getInventory().items.size(); i++) {

            ItemStack invStack = player.getInventory().items.get(i);

            if (invStack.isEmpty()) continue;

            // Only accept allowed mining items
            if (!SackFilter.accepts(invStack.getItem())) continue;

            for (ItemStack sackStack : player.getInventory().items) {

                if (!(sackStack.getItem() instanceof SackItem sack)) continue;

                // Skip disabled sacks
                if (!sack.isEnabled(sackStack)) continue;

                int remaining = sack.addItem(sackStack, invStack);

                if (remaining <= 0) {
                    player.getInventory().items.set(i, ItemStack.EMPTY);
                    break;
                } else {
                    invStack.setCount(remaining);
                }
            }
        }
    }

    // =========================
    // ITEM PICKUP — COLLECTIONS + SACK
    // =========================

    @SubscribeEvent
    public static void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        Player player = event.getEntity();
        ItemStack pickedStack = event.getStack();

        // Collections first (full amount)
        CollectionType type = CollectionType.fromItem(pickedStack.getItem());
        if (type != null) {
            player.getCapability(CollectionProvider.COLLECTION_CAP).ifPresent(data -> {
                data.add(type, pickedStack.getCount());
            });
        }

        moveInventoryItemsIntoSacks(player);
    }
}