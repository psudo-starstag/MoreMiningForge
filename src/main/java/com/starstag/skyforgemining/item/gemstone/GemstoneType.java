package com.starstag.skyforgemining.item.gemstone;

public enum GemstoneType {
    RUBY("Ruby", "speed", 10),
    SAPPHIRE("Sapphire", "fortune", 5),
    EMERALD("Emerald", "xp_gain", 5),
    AMETHYST("Amethyst", "pristine", 2);

    private final String displayName;
    private final String statKey;
    private final int boostPerTier;

    GemstoneType(String displayName, String statKey, int boostPerTier) {
        this.displayName = displayName;
        this.statKey = statKey;
        this.boostPerTier = boostPerTier;
    }

    public String getDisplayName() { return displayName; }
    public String getStatKey() { return statKey; }
    public int getBoostPerTier() { return boostPerTier; }
}