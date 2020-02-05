package com.agrejus.netherendingenergy.tools;

public class GrowthMediumValues {

    private final int strength;
    private final int yield;
    private final int purity;

    public GrowthMediumValues(int strength, int yield, int purity) {
        this.strength = strength;
        this.yield = yield;
        this.purity = purity;
    }

    public int getStrength() {
        return this.strength;
    }

    public int getYield() {
        return this.yield;
    }

    public int getPurity() {
        return this.purity;
    }
}
