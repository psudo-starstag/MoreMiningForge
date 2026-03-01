package com.starstag.skyforgemining.mining;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;
import java.util.Map;

public enum MiningXpType {

    COAL(Blocks.COAL_ORE, 5),
    IRON(Blocks.IRON_ORE, 8),
    GOLD(Blocks.GOLD_ORE, 12),
    DIAMOND(Blocks.DIAMOND_ORE, 20),
    EMERALD(Blocks.EMERALD_ORE, 25),
    REDSTONE(Blocks.REDSTONE_ORE, 6),
    LAPIS(Blocks.LAPIS_ORE, 6),
    DEEPSLATE_DIAMOND(Blocks.DEEPSLATE_DIAMOND_ORE, 25);

    private final Block block;
    private final int xp;

    private static final Map<Block, Integer> XP_MAP = new HashMap<>();

    static {
        for (MiningXpType type : values()) {
            XP_MAP.put(type.block, type.xp);
        }
    }

    MiningXpType(Block block, int xp) {
        this.block = block;
        this.xp = xp;
    }

    public static int getXpForBlock(Block block) {
        return XP_MAP.getOrDefault(block, 2); // default 2 XP
    }
}