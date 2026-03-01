package com.starstag.skyforgemining.item;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class DrillItem extends PickaxeItem {

    private final int baseMiningSpeed;
    private final int baseMiningFortune;

    public DrillItem(Tier tier,
                     int attackDamage,
                     float attackSpeed,
                     Properties properties,
                     int baseMiningSpeed,
                     int baseMiningFortune) {

        super(tier, attackDamage, attackSpeed, properties);
        this.baseMiningSpeed = baseMiningSpeed;
        this.baseMiningFortune = baseMiningFortune;
    }

    /*
     * =========================
     * NBT HELPERS
     * =========================
     */

    public int getDrillLevel(ItemStack stack) {
        return stack.getOrCreateTag().getInt("DrillLevel");
    }

    public int getDrillXp(ItemStack stack) {
        return stack.getOrCreateTag().getInt("DrillXP");
    }

    public void addXp(ItemStack stack, int amount) {

        int currentXp = getDrillXp(stack);
        int level = getDrillLevel(stack);

        currentXp += amount;

        int xpRequired = getXpForNextLevel(level);

        while (currentXp >= xpRequired) {
            currentXp -= xpRequired;
            level++;
            xpRequired = getXpForNextLevel(level);
        }

        stack.getOrCreateTag().putInt("DrillXP", currentXp);
        stack.getOrCreateTag().putInt("DrillLevel", level);
    }

    private int getXpForNextLevel(int level) {
        return (int)(100 * Math.pow(level + 1, 1.4));
    }

    /*
     * =========================
     * MINING SPEED
     * =========================
     */

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {

        float vanilla = super.getDestroySpeed(stack, state);

        int level = getDrillLevel(stack);
        int totalSpeed = baseMiningSpeed + (level * 2);

        return vanilla + totalSpeed;
    }

    public int getTotalFortune(ItemStack stack) {
        return baseMiningFortune + getDrillLevel(stack);
    }

    public void setDrillXp(ItemStack stack, int xp) {
        stack.getOrCreateTag().putInt("DrillXP", xp);
        stack.getOrCreateTag().putInt("DrillLevel", 0);
    }

    // Get total XP accumulated across all levels
    public int getTotalXp(ItemStack stack) {
        int level = getDrillLevel(stack);
        int currentXp = getDrillXp(stack);

        // Sum up all XP spent reaching current level
        int totalSpent = 0;
        for (int i = 0; i < level; i++) {
            totalSpent += getXpForNextLevel(i);
        }

        return totalSpent + currentXp;
    }

    // Set drill back to zero properly
    public void resetDrill(ItemStack stack) {
        stack.getOrCreateTag().putInt("DrillXP", 0);
        stack.getOrCreateTag().putInt("DrillLevel", 0);
    }

    /*
     * =========================
     * TOOLTIP
     * =========================
     */

    @Override
    public void appendHoverText(ItemStack stack,
                                @Nullable Level level,
                                List<Component> tooltip,
                                TooltipFlag flag) {

        int drillLevel = getDrillLevel(stack);
        int drillXp = getDrillXp(stack);
        int xpRequired = getXpForNextLevel(drillLevel);

        tooltip.add(Component.literal("§8Drill"));
        tooltip.add(Component.literal(" "));
        tooltip.add(Component.literal("§6Drill Mastery: §e" + drillLevel));
        tooltip.add(Component.literal("§7XP: §a" + drillXp + "§7/§a" + xpRequired));
        tooltip.add(Component.literal(" "));

        if (Screen.hasShiftDown()) {
            tooltip.add(Component.literal("§6Mining Speed: §e" + (baseMiningSpeed + (drillLevel * 2))));
            tooltip.add(Component.literal("§7Base Mining Speed: §6" + baseMiningSpeed));
            tooltip.add(Component.literal("§6Mining Fortune: §e" + getTotalFortune(stack)));
            tooltip.add(Component.literal("§7Base Fortune: §6" + baseMiningFortune));
            tooltip.add(Component.literal("§7Mastery Bonus: §6" + drillLevel));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.literal("§8(10 Fortune = +1 Extra Drop)"));
            tooltip.add(Component.literal(" "));
        } else {
            tooltip.add(Component.literal("§8[§eShift§8] for more details"));
            tooltip.add(Component.literal(" "));

        }
    }
}