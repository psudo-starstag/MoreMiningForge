package com.starstag.skyforgemining.menu;

import com.starstag.skyforgemining.registry.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DrillStationMenu extends AbstractContainerMenu {

    private final Player player;

    public DrillStationMenu(int id, Inventory inv) {
        super(ModMenus.DRILL_STATION_MENU.get(), id);
        this.player = inv.player;

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 168 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 226));
        }
    }

    // Called server side when player clicks an upgrade button
    // id encodes: section (0=mastery,1=gems,2=parts) * 100 + item index
    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (player.level().isClientSide) return false;

        int section = id / 100;
        int itemIndex = id % 100;

        DrillStationLogic.applyUpgrade(player, section, itemIndex);
        return true;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}