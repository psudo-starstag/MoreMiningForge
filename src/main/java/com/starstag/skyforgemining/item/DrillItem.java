package com.starstag.skyforgemining.item;

import com.starstag.skyforgemining.mining.MiningEvents;
import com.starstag.skyforgemining.player.MiningProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class DrillItem extends PickaxeItem {

    private final int baseMiningSpeed;
    private final int baseMiningFortune;

    public DrillItem(Tier tier,
                     int baseMiningSpeed,
                     int baseMiningFortune,
                     Properties properties) {

        super(tier, 1, -2.8F, properties);
        this.baseMiningSpeed = baseMiningSpeed;
        this.baseMiningFortune = baseMiningFortune;
    }

    // -------------------------
    // Mining Speed Logic
    // -------------------------

    @Override
    public float getDestroySpeed(ItemStack stack,
                                 net.minecraft.world.level.block.state.BlockState state) {

        float vanillaSpeed = super.getDestroySpeed(stack, state);

        int levelBonus = MiningEvents.getMiningSpeedBonus();

        return vanillaSpeed + baseMiningSpeed + levelBonus;
    }

    // -------------------------
    // Tooltip
    // -------------------------

    @Override
    public void appendHoverText(ItemStack stack,
                                Level level,
                                List<Component> tooltip,
                                TooltipFlag flag) {

        if (level != null && level.isClientSide) {

            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.player != null) {

                mc.player.getCapability(MiningProvider.MINING_CAP).ifPresent(data -> {

                    int levelSpeedBonus = data.getMiningLevel() * 2;
                    int levelFortuneBonus = data.getMiningLevel();

                    int totalSpeed = baseMiningSpeed + levelSpeedBonus;
                    int totalFortune = baseMiningFortune + levelFortuneBonus;
                    int extraDrops = totalFortune / 10;

                    tooltip.add(Component.literal(" "));
                    tooltip.add(Component.literal("§6Mining Stats"));
                    tooltip.add(Component.literal("Mining Level: §e" + data.getMiningLevel()));
                    tooltip.add(Component.literal("Total Speed: §a" + totalSpeed));
                    tooltip.add(Component.literal("Total Fortune: §b" + totalFortune));
                    tooltip.add(Component.literal("Extra Drops: §6+" + extraDrops));
                });
            }
        }
    }

    // -------------------------
    // Getters (Used in MiningEvents)
    // -------------------------

    public int getBaseMiningFortune() {
        return baseMiningFortune;
    }

    public int getBaseMiningSpeed() {
        return baseMiningSpeed;
    }
}