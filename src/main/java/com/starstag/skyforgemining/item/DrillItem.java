package com.starstag.skyforgemining.item;

import com.starstag.skyforgemining.item.drillpart.DrillPartType;
import com.starstag.skyforgemining.item.gemstone.GemstoneItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
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
            // Stats
            tooltip.add(Component.literal("§6Mining Speed: §e" + (baseMiningSpeed + (drillLevel * 2))));
            tooltip.add(Component.literal("§7Base Mining Speed: §6" + baseMiningSpeed));
            tooltip.add(Component.literal("§6Mining Fortune: §e" + getTotalFortune(stack)));
            tooltip.add(Component.literal("§7Base Fortune: §6" + baseMiningFortune));
            tooltip.add(Component.literal("§7Mastery Bonus: §6" + drillLevel));
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.literal("§8(10 Fortune = +1 Extra Drop)"));
            tooltip.add(Component.literal(" "));

            // Gemstones
            int usedSockets = getUsedSockets(stack);
            int maxSockets = getMaxSockets(stack);
            tooltip.add(Component.literal("§bGemstones §7(" + usedSockets + "/" + maxSockets + " sockets)"));
            if (usedSockets == 0) {
                tooltip.add(Component.literal("§8No gemstones socketed"));
            } else {
                CompoundTag tag = stack.getOrCreateTag();
                for (int i = 0; i < usedSockets; i++) {
                    String gemStr = tag.getString("Gem_" + i);
                    if (!gemStr.isEmpty()) {
                        String[] parts = gemStr.split("_", 2);
                        if (parts.length == 2) {
                            String typeName = parts[0];
                            String tierName = parts[1];
                            String emoji = getGemEmoji(typeName);
                            String color = getGemColor(typeName);
                            tooltip.add(Component.literal(
                                    emoji + " " + color + tierName + " " + typeName));
                        }
                    }
                }
            }
            tooltip.add(Component.literal(" "));

            // Installed parts
            tooltip.add(Component.literal("§7Installed Parts:"));
            boolean hasParts = false;
            for (com.starstag.skyforgemining.item.drillpart.DrillPartType partType
                    : com.starstag.skyforgemining.item.drillpart.DrillPartType.values()) {
                String key = "Part_" + partType.name();
                if (stack.getOrCreateTag().contains(key)) {
                    hasParts = true;
                    String partLine = "§a✔ §f" + partType.getDisplayName()
                            + " §7(+" + partType.getStat1Value()
                            + " " + partType.getStat1Key();
                    if (partType.hasStat2()) {
                        partLine += ", +" + partType.getStat2Value()
                                + " " + partType.getStat2Key();
                    }
                    partLine += ")";
                    tooltip.add(Component.literal(partLine));
                }
            }
            if (!hasParts) {
                tooltip.add(Component.literal("§8No parts installed"));
            }

        } else {
            // Compact gemstone display as emojis
            int usedSockets = getUsedSockets(stack);
            int maxSockets = getMaxSockets(stack);

            if (maxSockets > 0) {
                StringBuilder gems = new StringBuilder();
                CompoundTag tag = stack.getOrCreateTag();
                for (int i = 0; i < usedSockets; i++) {
                    String gemStr = tag.getString("Gem_" + i);
                    if (!gemStr.isEmpty()) {
                        String typeName = gemStr.split("_")[0];
                        gems.append(getGemEmoji(typeName)).append(" ");
                    }
                }
                // Empty sockets
                for (int i = usedSockets; i < maxSockets; i++) {
                    gems.append("§8◇ ");
                }
                tooltip.add(Component.literal(gems.toString().trim()));
                tooltip.add(Component.literal(" "));
            }

            tooltip.add(Component.literal("§8[§eShift§8] for more details"));
            tooltip.add(Component.literal(" "));
        }
    }

    private String getGemEmoji(String typeName) {
        return switch (typeName.toUpperCase()) {
            case "RUBY"     -> "§c◆";
            case "SAPPHIRE" -> "§9◆";
            case "EMERALD"  -> "§a◆";
            case "AMETHYST" -> "§5◆";
            default         -> "§7◆";
        };
    }

    private String getGemColor(String typeName) {
        return switch (typeName.toUpperCase()) {
            case "RUBY"     -> "§c";
            case "SAPPHIRE" -> "§9";
            case "EMERALD"  -> "§a";
            case "AMETHYST" -> "§5";
            default         -> "§7";
        };
    }

    // =========================
    // SOCKETS
    // =========================

    public int getMaxSockets(ItemStack stack) {
        // Basic drill = 1, Divan = 5 — override per drill type
        return 1;
    }

    public int getUsedSockets(ItemStack stack) {
        return stack.getOrCreateTag().getInt("UsedSockets");
    }

    public void addGemstone(ItemStack stack, GemstoneItem gem) {
        CompoundTag tag = stack.getOrCreateTag();
        int used = tag.getInt("UsedSockets");
        // Store gemstone in slot
        tag.putString("Gem_" + used, gem.getGemstoneType().name()
                + "_" + gem.getGemstoneTier().name());
        tag.putInt("UsedSockets", used + 1);
        // Apply stat boost
        String statKey = gem.getGemstoneType().getStatKey();
        tag.putInt(statKey, tag.getInt(statKey) + gem.getBoost());
    }

    // =========================
    // PARTS
    // =========================

    public void applyPart(ItemStack stack, DrillPartType part) {
        CompoundTag tag = stack.getOrCreateTag();
        // Remove old part of same type if present
        tag.putString("Part_" + part.name(), part.name());
        // Apply stats
        tag.putInt(part.getStat1Key(),
                tag.getInt(part.getStat1Key()) + part.getStat1Value());
        if (part.hasStat2()) {
            tag.putInt(part.getStat2Key(),
                    tag.getInt(part.getStat2Key()) + part.getStat2Value());
        }
    }
}