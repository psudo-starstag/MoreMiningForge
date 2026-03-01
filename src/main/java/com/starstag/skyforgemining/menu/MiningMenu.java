package com.starstag.skyforgemining.menu;

import com.starstag.skyforgemining.item.DrillItem;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;

public class MiningMenu implements MenuProvider {

    @Override
    public Component getDisplayName() {
        return Component.literal("§6Drill Mastery");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, net.minecraft.world.entity.player.Player player) {

        ChestMenu menu = new ChestMenu(
                MenuType.GENERIC_9x3,
                id,
                inv,
                new SimpleContainer(27),
                3
        ) {
            @Override
            public boolean stillValid(net.minecraft.world.entity.player.Player player) {
                return true;
            }

            @Override
            public void clicked(int slotId, int button, ClickType type,
                                net.minecraft.world.entity.player.Player player) {
                // Prevent item movement
            }
        };

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return menu;
        }

        ItemStack held = serverPlayer.getMainHandItem();

        if (!(held.getItem() instanceof DrillItem drill)) {

            ItemStack warning = new ItemStack(Items.BARRIER);
            warning.setHoverName(Component.literal("§cHold a Drill"));

            menu.setItem(13, 0, warning);
            return menu;
        }

        int level = drill.getDrillLevel(held);
        int xp = drill.getDrillXp(held);

        int xpRequired = (int)(100 * Math.pow(level + 1, 1.4));

        int speedBonus = level * 2;
        int fortuneBonus = level;
        int extraDrops = fortuneBonus / 10;

        ItemStack display = new ItemStack(Items.DIAMOND_PICKAXE);
        display.setHoverName(Component.literal("§6Drill Mastery " + level));

        CompoundTag displayTag = display.getOrCreateTagElement("display");
        ListTag loreList = new ListTag();

        loreList.add(StringTag.valueOf(Component.Serializer.toJson(
                Component.literal("§7XP: §e" + xp + "§7/§e" + xpRequired))));
        loreList.add(StringTag.valueOf(Component.Serializer.toJson(
                Component.literal(" "))));

        loreList.add(StringTag.valueOf(Component.Serializer.toJson(
                Component.literal("§aMining Speed Bonus: +" + speedBonus))));
        loreList.add(StringTag.valueOf(Component.Serializer.toJson(
                Component.literal("§bMining Fortune Bonus: +" + fortuneBonus))));
        loreList.add(StringTag.valueOf(Component.Serializer.toJson(
                Component.literal("§6Extra Drops: +" + extraDrops))));

        displayTag.put("Lore", loreList);

        menu.setItem(13, 0, display);

        return menu;
    }
}