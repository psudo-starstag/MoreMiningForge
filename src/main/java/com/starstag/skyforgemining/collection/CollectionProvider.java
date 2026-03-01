package com.starstag.skyforgemining.collection;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CollectionProvider implements ICapabilitySerializable<CompoundTag> {

    public static final Capability<CollectionData> COLLECTION_CAP =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final CollectionData data = new CollectionData();
    private final LazyOptional<CollectionData> optional = LazyOptional.of(() -> data);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == COLLECTION_CAP ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return data.saveNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        data.loadNBT(nbt);
    }
}