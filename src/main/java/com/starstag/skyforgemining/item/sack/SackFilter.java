package com.starstag.skyforgemining.item.sack;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.HashSet;
import java.util.Set;

public class SackFilter {

    // Add any item here to make it collectable by the sack
    private static final Set<Item> ACCEPTED_ITEMS = new HashSet<>();

    static {
        // Vanilla ores and drops
        ACCEPTED_ITEMS.add(Items.COAL);
        ACCEPTED_ITEMS.add(Items.RAW_IRON);
        ACCEPTED_ITEMS.add(Items.RAW_GOLD);
        ACCEPTED_ITEMS.add(Items.RAW_COPPER);
        ACCEPTED_ITEMS.add(Items.IRON_INGOT);
        ACCEPTED_ITEMS.add(Items.GOLD_INGOT);
        ACCEPTED_ITEMS.add(Items.COPPER_INGOT);
        ACCEPTED_ITEMS.add(Items.DIAMOND);
        ACCEPTED_ITEMS.add(Items.EMERALD);
        ACCEPTED_ITEMS.add(Items.LAPIS_LAZULI);
        ACCEPTED_ITEMS.add(Items.REDSTONE);
        ACCEPTED_ITEMS.add(Items.QUARTZ);
        ACCEPTED_ITEMS.add(Items.AMETHYST_SHARD);
        ACCEPTED_ITEMS.add(Items.GLOWSTONE_DUST);
        ACCEPTED_ITEMS.add(Items.ANCIENT_DEBRIS);
        ACCEPTED_ITEMS.add(Items.NETHERITE_SCRAP);
        ACCEPTED_ITEMS.add(Items.COBBLESTONE);
        ACCEPTED_ITEMS.add(Items.COBBLED_DEEPSLATE);
        ACCEPTED_ITEMS.add(Items.GRAVEL);
        ACCEPTED_ITEMS.add(Items.FLINT);
        ACCEPTED_ITEMS.add(Items.SAND);
        ACCEPTED_ITEMS.add(Items.OBSIDIAN);
        ACCEPTED_ITEMS.add(Items.NETHERRACK);
        ACCEPTED_ITEMS.add(Items.SOUL_SAND);
        ACCEPTED_ITEMS.add(Items.SOUL_SOIL);
        ACCEPTED_ITEMS.add(Items.BASALT);
        ACCEPTED_ITEMS.add(Items.BLACKSTONE);
        ACCEPTED_ITEMS.add(Items.NETHER_QUARTZ_ORE);
        ACCEPTED_ITEMS.add(Items.NETHER_GOLD_ORE);
    }

    public static boolean accepts(Item item) {
        return ACCEPTED_ITEMS.contains(item);
    }

    // Call this to add mod items at startup
    public static void register(Item item) {
        ACCEPTED_ITEMS.add(item);
    }
}