package com.starstag.skyforgemining.item.gemstone;

public enum GemstoneTier {
    ROUGH(1, "§7Rough"),
    FLAWED(2, "§aFlawed"),
    FINE(3, "§9Fine"),
    FLAWLESS(4, "§5Flawless"),
    PERFECT(5, "§6Perfect");

    private final int tier;
    private final String prefix;

    GemstoneTier(int tier, String prefix) {
        this.tier = tier;
        this.prefix = prefix;
    }

    public int getTier() { return tier; }
    public String getPrefix() { return prefix; }
}