package com.starstag.skyforgemining.item.sack;

public enum SackTier {
    BASIC(      "§7Basic",       27,   "§7"),
    REINFORCED( "§aReinforced",  54,   "§a"),
    ENCHANTED(  "§9Enchanted",   108,  "§9"),
    VOID(       "§5Void",        216,  "§5");

    private final String displayName;
    private final int maxStacks; // number of item stacks storable
    private final String color;

    SackTier(String displayName, int maxStacks, String color) {
        this.displayName = displayName;
        this.maxStacks = maxStacks;
        this.color = color;
    }

    public String getDisplayName() { return displayName; }
    public int getMaxStacks() { return maxStacks; }
    public String getColor() { return color; }
}