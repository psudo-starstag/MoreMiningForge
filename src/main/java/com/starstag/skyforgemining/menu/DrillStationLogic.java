package com.starstag.skyforgemining.menu;

import com.starstag.skyforgemining.item.DrillItem;
import com.starstag.skyforgemining.item.drillpart.DrillPartItem;
import com.starstag.skyforgemining.item.drillpart.DrillPartType;
import com.starstag.skyforgemining.item.gemstone.GemstoneItem;
import com.starstag.skyforgemining.registry.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DrillStationLogic {

    // =========================
    // UPGRADE LISTS
    // =========================

    public static List<ItemStack> getMasteryOptions(Player player) {
        List<ItemStack> options = new ArrayList<>();
        // Capsule
        options.add(new ItemStack(ModItems.MASTERY_CAPSULE.get()));
        // Other drills in inventory as mastery sources
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof DrillItem
                    && stack != player.getMainHandItem()) {
                options.add(stack.copy());
            }
        }
        return options;
    }

    public static List<ItemStack> getGemOptions() {
        List<ItemStack> options = new ArrayList<>();
        options.add(new ItemStack(ModItems.ROUGH_RUBY.get()));
        options.add(new ItemStack(ModItems.ROUGH_SAPPHIRE.get()));
        options.add(new ItemStack(ModItems.ROUGH_EMERALD.get()));
        options.add(new ItemStack(ModItems.ROUGH_AMETHYST.get()));
        return options;
    }

    public static List<ItemStack> getPartOptions() {
        List<ItemStack> options = new ArrayList<>();
        options.add(new ItemStack(ModItems.DRILL_ENGINE.get()));
        options.add(new ItemStack(ModItems.DRILL_PLATE.get()));
        options.add(new ItemStack(ModItems.DRILL_MOTOR.get()));
        options.add(new ItemStack(ModItems.FUEL_TANK.get()));
        return options;
    }

    // =========================
    // CHECK IF PLAYER HAS MATERIAL
    // =========================

    public static boolean hasItem(Player player, ItemStack required) {
        return player.getInventory().countItem(required.getItem()) >= required.getCount();
    }

    // =========================
    // APPLY UPGRADE
    // =========================

    public static void applyUpgrade(Player player, int section, int itemIndex) {
        ItemStack drill = player.getMainHandItem();
        if (!(drill.getItem() instanceof DrillItem drillItem)) return;

        switch (section) {

            // Mastery
            case 0 -> {
                List<ItemStack> options = getMasteryOptions(player);
                if (itemIndex >= options.size()) return;
                ItemStack material = options.get(itemIndex);
                if (!hasItem(player, material)) return;

                if (material.getItem() == ModItems.MASTERY_CAPSULE.get()) {
                    removeFromInventory(player, ModItems.MASTERY_CAPSULE.get(), 1);
                    drillItem.addXp(drill, 1000);
                } else if (material.getItem() instanceof DrillItem sourceDrill) {
                    // Find the actual stack in inventory
                    ItemStack sourceStack = findInInventory(player, material.getItem());
                    if (sourceStack.isEmpty()) return;
                    int totalXp = sourceDrill.getTotalXp(sourceStack);
                    drillItem.addXp(drill, totalXp);
                    sourceDrill.resetDrill(sourceStack);
                }
            }

            // Gemstones
            case 1 -> {
                List<ItemStack> options = getGemOptions();
                if (itemIndex >= options.size()) return;
                ItemStack material = options.get(itemIndex);
                if (!hasItem(player, material)) return;
                if (!(material.getItem() instanceof GemstoneItem gem)) return;

                int maxSockets = drillItem.getMaxSockets(drill);
                int usedSockets = drillItem.getUsedSockets(drill);
                if (usedSockets >= maxSockets) return;

                removeFromInventory(player, gem, 1);
                drillItem.addGemstone(drill, gem);
            }

            // Parts
            case 2 -> {
                List<ItemStack> options = getPartOptions();
                if (itemIndex >= options.size()) return;
                ItemStack material = options.get(itemIndex);
                if (!hasItem(player, material)) return;
                if (!(material.getItem() instanceof DrillPartItem partItem)) return;

                DrillPartType partType = partItem.getPartType();

                // Return previous part of same type if installed
                returnPreviousPart(player, drill, drillItem, partType);

                removeFromInventory(player, partItem, 1);
                drillItem.applyPart(drill, partType);
            }
        }

        playSound(player);
    }

    // =========================
    // HELPERS
    // =========================

    private static void removeFromInventory(Player player, net.minecraft.world.item.Item item, int count) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == item && count > 0) {
                int remove = Math.min(stack.getCount(), count);
                stack.shrink(remove);
                count -= remove;
            }
        }
    }

    private static void removeFromInventory(Player player, GemstoneItem gem, int count) {
        removeFromInventory(player, (net.minecraft.world.item.Item) gem, count);
    }

    private static void removeFromInventory(Player player, DrillPartItem part, int count) {
        removeFromInventory(player, (net.minecraft.world.item.Item) part, count);
    }

    private static ItemStack findInInventory(Player player, net.minecraft.world.item.Item item) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == item
                    && stack != player.getMainHandItem()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private static void returnPreviousPart(Player player, ItemStack drill,
                                           DrillItem drillItem, DrillPartType partType) {
        String key = "Part_" + partType.name();
        if (!drill.getOrCreateTag().contains(key)) return;

        // Find the matching part item and give it back
        for (ItemStack option : getPartOptions()) {
            if (option.getItem() instanceof DrillPartItem p
                    && p.getPartType() == partType) {
                if (!player.getInventory().add(option.copy())) {
                    player.drop(option.copy(), false);
                }
                // Remove stat from drill
                drill.getOrCreateTag().remove(key);
                drill.getOrCreateTag().putInt(
                        partType.getStat1Key(),
                        Math.max(0, drill.getOrCreateTag().getInt(partType.getStat1Key())
                                - partType.getStat1Value())
                );
                if (partType.hasStat2()) {
                    drill.getOrCreateTag().putInt(
                            partType.getStat2Key(),
                            Math.max(0, drill.getOrCreateTag().getInt(partType.getStat2Key())
                                    - partType.getStat2Value())
                    );
                }
                return;
            }
        }
    }

    private static void playSound(Player player) {
        player.level().playSound(null,
                player.blockPosition(),
                SoundEvents.ANVIL_USE,
                SoundSource.BLOCKS,
                1.0f, 1.0f);
    }
}