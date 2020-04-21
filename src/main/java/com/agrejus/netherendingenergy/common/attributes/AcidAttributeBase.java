package com.agrejus.netherendingenergy.common.attributes;

public class AcidAttributeBase {
    private final float efficiency;
    private final float stability;
    private final float response;
    private final float strength;

    public AcidAttributeBase(float strength, float efficiency, float stability, float response) {
        this.strength = strength;
        this.efficiency = efficiency;
        this.stability = stability;
        this.response = response;
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
}
