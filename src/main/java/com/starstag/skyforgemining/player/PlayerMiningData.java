package com.starstag.skyforgemining.player;

public class PlayerMiningData {

    private int miningLevel = 1;
    private int miningXp = 0;

    public int getMiningLevel() {
        return miningLevel;
    }

    public void setMiningLevel(int level) {
        this.miningLevel = level;
    }

    public int getMiningXp() {
        return miningXp;
    }

    public void setMiningXp(int xp) {
        this.miningXp = xp;
    }

    public void addXp(int amount) {
        this.miningXp += amount;
    }

    public void levelUp() {
        this.miningLevel++;
        this.miningXp = 0;
    }
}