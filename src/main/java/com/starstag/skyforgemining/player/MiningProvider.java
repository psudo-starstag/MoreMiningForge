package com.starstag.skyforgemining.player;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MiningProvider implements ICapabilitySerializable<CompoundTag> {

    public static Capability<PlayerMiningData> MINING_CAP =
            CapabilityManager.get(new CapabilityToken<>() {});

    private final PlayerMiningData data = new PlayerMiningData();
    private final LazyOptional<PlayerMiningData> optional = LazyOptional.of(() -> data);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == MINING_CAP ? optional.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("MiningLevel", data.getMiningLevel());
        tag.putInt("MiningXp", data.getMiningXp());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        data.setMiningLevel(nbt.getInt("MiningLevel"));
        data.setMiningXp(nbt.getInt("MiningXp"));
    }
}