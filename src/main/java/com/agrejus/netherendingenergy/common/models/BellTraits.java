package com.agrejus.netherendingenergy.common.models;

public class BellTraits {
    private float yield;
    private float strength;
    private float purity;

    public BellTraits(float all) {
        this.yield = all;
        this.strength = all;
        this.purity = all;
    }

    public BellTraits(float yield, float strength, float purity) {
        this.yield = yield;
        this.strength = strength;
        this.purity = purity;
    }

    public float getYield() {
        return yield;
    }

    public float getStrength() {
        return strength;
    }

    public float getPurity() {
        return purity;
    }

    public void setYield(float yield) {
        this.yield = yield;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void setPurity(float purity) {
        this.purity = purity;
    }
}
