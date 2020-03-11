package com.agrejus.netherendingenergy.common.reactor.attributes;

public class PotionAttributes {
    private final float efficiency;
    private final float stability;
    private final float response;
    private final int uses;
    private final float strength;

    public PotionAttributes(float strength, float efficiency, float stability, float response, int uses) {
        this.strength = strength;
        this.efficiency = efficiency;
        this.stability = stability;
        this.response = response;
        this.uses = uses;
    }

    public float getStrength() {
        return strength;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public float getStability() {
        return stability;
    }

    public float getResponse() {
        return response;
    }

    public int getUses() {
        return uses;
    }
}
