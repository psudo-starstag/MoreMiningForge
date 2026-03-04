package com.starstag.skyforgemining.item.drillpart;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class DrillPartItem extends Item {

    private final DrillPartType partType;

    public DrillPartItem(DrillPartType partType, Properties properties) {
        super(properties);
        this.partType = partType;
    }

    public DrillPartType getPartType() { return partType; }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§7Part: §f" + partType.getDisplayName()));
        tooltip.add(Component.literal("§a+" + partType.getStat1Value()
                + " §7" + partType.getStat1Key()));
        if (partType.hasStat2()) {
            tooltip.add(Component.literal("§a+" + partType.getStat2Value()
                    + " §7" + partType.getStat2Key()));
        }
    }
}