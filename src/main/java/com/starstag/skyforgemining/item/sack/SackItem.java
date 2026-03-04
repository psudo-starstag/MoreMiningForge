package com.starstag.skyforgemining.item.sack;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class SackItem extends Item {

    private static final String DATA_TAG = "SackData";
    private static final String ENABLED_TAG = "SackEnabled";

    private final SackTier tier;

    public SackItem(Properties properties, SackTier tier) {
        super(properties);
        this.tier = tier;
    }

    /* ========================================================= */
    /* ====================== STORAGE ========================== */
    /* ========================================================= */

    private net.minecraft.nbt.CompoundTag getData(ItemStack stack) {
        net.minecraft.nbt.CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(DATA_TAG)) {
            tag.put(DATA_TAG, new net.minecraft.nbt.CompoundTag());
        }
        return tag.getCompound(DATA_TAG);
    }

    public int getCapacityPerItem() {
        return switch (tier) {
            case BASIC -> 1600;
            case REINFORCED -> 6400;
            case ENCHANTED -> 25600;
            case VOID -> 102400;
        };
    }

    public int addItem(ItemStack sackStack, ItemStack incoming) {

        if (!SackFilter.accepts(incoming.getItem()))
            return incoming.getCount();

        if (!isEnabled(sackStack))
            return incoming.getCount();

        var data = getData(sackStack);

        String id = ForgeRegistries.ITEMS.getKey(incoming.getItem()).toString();
        int stored = data.getInt(id);
        int capacity = getCapacityPerItem();

        if (stored >= capacity)
            return incoming.getCount();

        int canStore = capacity - stored;
        int toStore = Math.min(canStore, incoming.getCount());

        data.putInt(id, stored + toStore);

        return incoming.getCount() - toStore;
    }

    public ItemStack withdraw(ItemStack sackStack, String itemId) {

        var data = getData(sackStack);

        int stored = data.getInt(itemId);
        if (stored <= 0)
            return ItemStack.EMPTY;

        int amount = Math.min(64, stored);

        var item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemId));
        if (item == null)
            return ItemStack.EMPTY;

        data.putInt(itemId, stored - amount);

        return new ItemStack(item, amount);
    }

    /* ========================================================= */
    /* ======================= TOGGLE ========================== */
    /* ========================================================= */

    public boolean isEnabled(ItemStack stack) {
        return !stack.getOrCreateTag().contains(ENABLED_TAG)
                || stack.getOrCreateTag().getBoolean(ENABLED_TAG);
    }

    public void toggle(ItemStack stack) {
        boolean current = isEnabled(stack);
        stack.getOrCreateTag().putBoolean(ENABLED_TAG, !current);
    }

    /* ========================================================= */
    /* ======================== TOOLTIP ======================== */
    /* ========================================================= */

    @Override
    public void appendHoverText(ItemStack stack,
                                Level level,
                                List<Component> tooltip,
                                TooltipFlag flag) {

        boolean enabled = isEnabled(stack);

        tooltip.add(Component.literal(""));
        tooltip.add(Component.literal("Auto-Collect: ")
                .append(Component.literal(enabled ? "ENABLED" : "DISABLED")
                        .withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED)));

        var data = getData(stack);

        if (!data.getAllKeys().isEmpty()) {
            tooltip.add(Component.literal(""));
            tooltip.add(Component.literal("Stored Items:")
                    .withStyle(ChatFormatting.GRAY));

            for (String key : data.getAllKeys()) {
                int amount = data.getInt(key);
                var item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));

                if (item != null && amount > 0) {
                    tooltip.add(Component.literal(" - " +
                                    item.getDescription().getString() +
                                    ": " + formatNumber(amount))
                            .withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }
    }

    /* ========================================================= */
    /* ======================== FORMAT ========================= */
    /* ========================================================= */

    private String formatNumber(int value) {
        if (value >= 1_000_000)
            return String.format("%.1fM", value / 1_000_000.0);
        if (value >= 1_000)
            return String.format("%.1fk", value / 1_000.0);
        return String.valueOf(value);
    }

    /* ========================================================= */
    /* ======================== USE ============================ */
    /* ========================================================= */

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                toggle(stack);
                player.displayClientMessage(
                        Component.literal("Sack "
                                + (isEnabled(stack) ? "Enabled" : "Disabled")),
                        true
                );
            }
            return InteractionResultHolder.success(stack);
        }

        return InteractionResultHolder.success(stack);
    }
}