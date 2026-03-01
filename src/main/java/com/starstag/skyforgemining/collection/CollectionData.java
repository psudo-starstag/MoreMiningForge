package com.starstag.skyforgemining.collection;

import net.minecraft.nbt.CompoundTag;

import java.util.EnumMap;
import java.util.Map;

public class CollectionData {

    private final Map<CollectionType, Integer> collections = new EnumMap<>(CollectionType.class);

    public void add(CollectionType type, int amount) {
        collections.put(type, get(type) + amount);
    }

    public int get(CollectionType type) {
        return collections.getOrDefault(type, 0);
    }

    public CompoundTag saveNBT() {
        CompoundTag tag = new CompoundTag();
        for (CollectionType type : CollectionType.values()) {
            tag.putInt(type.name(), get(type));
        }
        return tag;
    }

    public void loadNBT(CompoundTag tag) {
        for (CollectionType type : CollectionType.values()) {
            collections.put(type, tag.getInt(type.name()));
        }
    }
}