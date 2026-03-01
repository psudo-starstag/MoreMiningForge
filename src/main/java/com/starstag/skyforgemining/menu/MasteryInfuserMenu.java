package com.starstag.skyforgemining.menu;

import com.starstag.skyforgemining.blockentity.MasteryInfuserBlockEntity;
import com.starstag.skyforgemining.registry.ModMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class MasteryInfuserMenu extends AbstractContainerMenu {

    private final MasteryInfuserBlockEntity blockEntity;

    public MasteryInfuserMenu(int id, Inventory inv, MasteryInfuserBlockEntity be) {
        super(ModMenus.MASTERY_INFUSER_MENU.get(), id);
        this.blockEntity = be;

        var handler = be.getInventory();

        // Slot 0 — Source Drill
        this.addSlot(new SlotItemHandler(handler, 0, 44, 35));
        // Slot 1 — Target Drill
        this.addSlot(new SlotItemHandler(handler, 1, 116, 35));
        // Slot 2 — Capsule
        this.addSlot(new SlotItemHandler(handler, 2, 80, 60));

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 102 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 160));
        }
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == 0) {
            blockEntity.processTransfer();
            return true;
        }
        return super.clickMenuButton(player, id);
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