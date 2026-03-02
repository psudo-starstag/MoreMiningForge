package com.starstag.skyforgemining.menu;

import com.starstag.skyforgemining.blockentity.MasteryInfuserBlockEntity;
import com.starstag.skyforgemining.registry.ModMenus;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class MasteryInfuserMenu extends AbstractContainerMenu {

    private final MasteryInfuserBlockEntity blockEntity;
    private final SimpleContainer outputContainer;

    public MasteryInfuserMenu(int id, Inventory inv, MasteryInfuserBlockEntity be) {
        super(ModMenus.MASTERY_INFUSER_MENU.get(), id);
        this.blockEntity = be;

        var handler = be.getInventory();

        // Slot 0 — Target drill
        this.addSlot(new SlotItemHandler(handler, 0, 44, 47));

        // Slot 1 — Source (drill or capsule)
        this.addSlot(new SlotItemHandler(handler, 1, 80, 47));

        // Output container — separate from ItemStackHandler
        outputContainer = new SimpleContainer(1) {
            @Override
            public boolean canPlaceItem(int slot, ItemStack stack) {
                return false;
            }
        };

        outputContainer.setItem(0, be.getOutputPreview());

        // Slot 2 — Output (click to take)
        this.addSlot(new Slot(outputContainer, 0, 134, 47) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(Player player, ItemStack stack) {
                blockEntity.takeOutput(player);
                super.onTake(player, stack);
            }
        });

        // Player inventory
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 142));
        }
    }

    // Keep output preview synced with block entity
    @Override
    public void broadcastChanges() {
        outputContainer.setItem(0, blockEntity.getOutputPreview());
        super.broadcastChanges();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {

        ItemStack returnStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            returnStack = slotStack.copy();

            // Clicking output slot — move to inventory
            if (index == 2) {
                if (!this.moveItemStackTo(slotStack, 3, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }

            // Clicking from player inventory — try input slots 0 and 1
            else if (index >= 3) {
                if (!this.moveItemStackTo(slotStack, 0, 2, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // Clicking input slot — move back to inventory
            else {
                if (!this.moveItemStackTo(slotStack, 3, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return returnStack;
    }
}