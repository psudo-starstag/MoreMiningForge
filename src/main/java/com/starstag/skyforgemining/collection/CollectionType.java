package com.starstag.skyforgemining.collection;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum CollectionType {

    COBBLESTONE(Items.COBBLESTONE),
    RAW_IRON(Items.RAW_IRON),
    DIAMOND(Items.DIAMOND);

    private final Item item;

    CollectionType(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public static CollectionType fromItem(Item item) {
        for (CollectionType type : values()) {
            if (type.item == item) return type;
        }
        return null;
    }
}