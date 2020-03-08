package com.agrejus.netherendingenergy.common.fluids.attributes;

public class AcidAttributes {

    private final float efficiency;
    private final float stability;
    private final float response;
    private final int uses;
    private final float strength;
    private final float spatial;
    private final int spatialAmount;

    public AcidAttributes(float strength, float efficiency, float stability, float response, int uses, float spatial, int spatialAmount) {
        this.strength = strength;
        this.efficiency = efficiency;
        this.stability = stability;
        this.response = response;
        this.uses = uses;
        this.spatial = spatial;
        this.spatialAmount = spatialAmount;
    }

    public float getStrength() {
        return strength;
    }

    public float getSpatial() {
        return spatial;
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

    public int getSpatialAmount() {
        return spatialAmount;
    }
}
