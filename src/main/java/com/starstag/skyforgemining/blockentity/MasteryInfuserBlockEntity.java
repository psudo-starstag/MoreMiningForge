package com.starstag.skyforgemining.blockentity;

import com.starstag.skyforgemining.item.DrillItem;
import com.starstag.skyforgemining.menu.MasteryInfuserMenu;
import com.starstag.skyforgemining.registry.ModBlockEntities;
import com.starstag.skyforgemining.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class MasteryInfuserBlockEntity extends BlockEntity implements MenuProvider {

    /*
     * Slot 0 = Target Drill (receives mastery)
     * Slot 1 = Source (drill or capsule)
     * Output preview stored separately in memory
     */
    private final ItemStackHandler inventory = new ItemStackHandler(2) {

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            if (slot == 0) return stack.getItem() instanceof DrillItem;
            if (slot == 1) return stack.getItem() instanceof DrillItem
                    || stack.getItem() == ModItems.MASTERY_CAPSULE.get();
            return false;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            updateOutput();
        }
    };

    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> inventory);

    // Output preview — not saved, recalculated from inputs
    private ItemStack outputPreview = ItemStack.EMPTY;

    public MasteryInfuserBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MASTERY_INFUSER.get(), pos, state);
    }

    // =========================
    // OUTPUT PREVIEW
    // =========================

    public void updateOutput() {

        ItemStack target = inventory.getStackInSlot(0);
        ItemStack source = inventory.getStackInSlot(1);

        if (target.isEmpty() || source.isEmpty()
                || !(target.getItem() instanceof DrillItem)) {
            outputPreview = ItemStack.EMPTY;
            return;
        }

        ItemStack preview = target.copy();

        if (preview.getItem() instanceof DrillItem previewDrill) {
            if (source.getItem() == ModItems.MASTERY_CAPSULE.get()) {
                previewDrill.addXp(preview, 1000);
            } else if (source.getItem() instanceof DrillItem sourceDrill) {
                int totalXp = sourceDrill.getTotalXp(source);
                previewDrill.addXp(preview, totalXp);
            }
        }

        outputPreview = preview;
    }

    public ItemStack getOutputPreview() {
        return outputPreview;
    }

    // =========================
    // TAKE OUTPUT
    // =========================

    public void takeOutput(Player player) {

        ItemStack target = inventory.getStackInSlot(0);
        ItemStack source = inventory.getStackInSlot(1);

        if (outputPreview.isEmpty()) return;
        if (!(target.getItem() instanceof DrillItem targetDrill)) return;
        if (source.isEmpty()) return;

        // Apply mastery for real
        if (source.getItem() == ModItems.MASTERY_CAPSULE.get()) {
            targetDrill.addXp(target, 1000);
            source.shrink(1);
            if (source.isEmpty()) {
                inventory.setStackInSlot(1, ItemStack.EMPTY);
            }
        } else if (source.getItem() instanceof DrillItem sourceDrill) {
            int totalXp = sourceDrill.getTotalXp(source);
            targetDrill.addXp(target, totalXp);
            sourceDrill.resetDrill(source);
            inventory.setStackInSlot(1, ItemStack.EMPTY);
        }

        // Give upgraded drill to player
        if (!player.getInventory().add(target)) {
            player.drop(target, false);
        }

        // Clear target slot and preview
        inventory.setStackInSlot(0, ItemStack.EMPTY);
        outputPreview = ItemStack.EMPTY;

        playSound();
        setChanged();
    }

    // =========================
    // SOUND
    // =========================

    private void playSound() {
        if (level != null && !level.isClientSide) {
            level.playSound(
                    null,
                    worldPosition,
                    SoundEvents.ANVIL_USE,
                    SoundSource.BLOCKS,
                    1.0f,
                    1.0f
            );
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

    public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
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
        updateOutput();
        super.load(tag);
    }

    public ItemStackHandler getInventory() {
        return inventory;
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }
}