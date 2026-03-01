package com.starstag.skyforgemining.blockentity;

import com.starstag.skyforgemining.item.DrillItem;
import net.minecraft.network.FriendlyByteBuf;
import com.starstag.skyforgemining.registry.ModBlockEntities;
import com.starstag.skyforgemining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.starstag.skyforgemining.menu.MasteryInfuserMenu;

public class MasteryInfuserBlockEntity extends BlockEntity implements MenuProvider {

    /*
     * Slot 0 = Source Drill
     * Slot 1 = Target Drill
     * Slot 2 = Mastery Capsule
     */
    private final ItemStackHandler inventory = new ItemStackHandler(3) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 0 || slot == 1) return stack.getItem() instanceof DrillItem;
            if (slot == 2) return stack.getItem() == ModItems.MASTERY_CAPSULE.get();
            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inventory);

    public MasteryInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MASTERY_INFUSER.get(), pos, state);
    }

    // =========================
    // TRANSFER LOGIC
    // =========================

    public void processTransfer() {
        ItemStack source = inventory.getStackInSlot(0);
        ItemStack target = inventory.getStackInSlot(1);
        ItemStack capsule = inventory.getStackInSlot(2);

        // Capsule → Target Drill
        if (target.getItem() instanceof DrillItem targetDrill
                && capsule.getItem() == ModItems.MASTERY_CAPSULE.get()) {

            targetDrill.addXp(target, 1000);
            capsule.shrink(1);
            setChanged();
            return;
        }

        // Source Drill → Target Drill
        if (source.getItem() instanceof DrillItem sourceDrill
                && target.getItem() instanceof DrillItem targetDrill) {

            // Get TOTAL xp across all levels, not just current level xp
            int totalXp = sourceDrill.getTotalXp(source);

            if (totalXp > 0) {
                targetDrill.addXp(target, totalXp);
                sourceDrill.resetDrill(source);
                setChanged();
            }
        }
    }

    // =========================
    // DROP CONTENTS ON BREAK
    // =========================

    public void dropContents(Level level, BlockPos pos) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                net.minecraft.world.level.block.Block.popResource(level, pos, stack);
            }
        }
    }

    // =========================
    // MENU
    // =========================

    @Override
    public Component getDisplayName() {
        return Component.literal("§6Mastery Infuser");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new MasteryInfuserMenu(id, inv, this);
    }

    public void writeScreenOpeningData(ServerPlayer player, net.minecraft.network.FriendlyByteBuf buf) {
        buf.writeBlockPos(this.worldPosition);
    }

    // =========================
    // CAPABILITY
    // =========================

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) return itemHandler.cast();
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandler.invalidate();
    }

    // =========================
    // SAVE / LOAD
    // =========================

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.put("inventory", inventory.serializeNBT());
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        inventory.deserializeNBT(tag.getCompound("inventory"));
        super.load(tag);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public net.minecraft.nbt.CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }
}