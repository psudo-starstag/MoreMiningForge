package com.starstag.skyforgemining.item.drillpart;

public enum DrillPartType {
    ENGINE("Drill Engine", "speed", 15),
    PLATE("Drill Plate", "fortune", 10),
    MOTOR("Drill Motor", "speed", 8, "fortune", 8),
    FUEL_TANK("Fuel Tank", "energy", 100);

    private final String displayName;
    private final String stat1Key;
    private final int stat1Value;
    private final String stat2Key;
    private final int stat2Value;

    DrillPartType(String displayName, String stat1Key, int stat1Value) {
        this(displayName, stat1Key, stat1Value, null, 0);
    }

    DrillPartType(String displayName, String stat1Key, int stat1Value,
                  String stat2Key, int stat2Value) {
        this.displayName = displayName;
        this.stat1Key = stat1Key;
        this.stat1Value = stat1Value;
        this.stat2Key = stat2Key;
        this.stat2Value = stat2Value;
    }

    public String getDisplayName() { return displayName; }
    public String getStat1Key() { return stat1Key; }
    public int getStat1Value() { return stat1Value; }
    public String getStat2Key() { return stat2Key; }
    public int getStat2Value() { return stat2Value; }
    public boolean hasStat2() { return stat2Key != null; }
}