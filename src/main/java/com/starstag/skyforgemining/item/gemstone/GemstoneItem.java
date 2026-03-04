package com.starstag.skyforgemining.item.gemstone;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class GemstoneItem extends Item {

    private final GemstoneType type;
    private final GemstoneTier tier;

    public GemstoneItem(GemstoneType type, GemstoneTier tier, Properties properties) {
        super(properties);
        this.type = type;
        this.tier = tier;
    }

    public GemstoneType getGemstoneType() { return type; }
    public GemstoneTier getGemstoneTier() { return tier; }

    public int getBoost() {
        return type.getBoostPerTier() * tier.getTier();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Type: §f" + type.getDisplayName()));
        tooltip.add(Component.literal("§7Tier: " + tier.getPrefix()));
        tooltip.add(Component.literal("§7Boost: §f+" + getBoost() + " "
                + type.getStatKey().replace("_", " ")));
    }
}