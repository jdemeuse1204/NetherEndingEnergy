package com.agrejus.netherendingenergy.common.attributes;

public class AcidAttributes extends AcidAttributeBase {

    private final float spatial;
    private final float spatialAmount; // change to a float for mixing purposes
    private final int baseEnergyPerTick;
    private final float decayRate;// change to a float for mixing purposes

    public AcidAttributes(float strength, float efficiency, float stability, float response, float spatial, float spatialAmount, int baseEnergyPerTick, float decayRate) {
        super(strength, efficiency, stability, response);
        this.spatial = spatial;
        this.spatialAmount = spatialAmount;
        this.baseEnergyPerTick = baseEnergyPerTick;
        this.decayRate = decayRate;
    }

    public float getDecayRate() {
        return this.decayRate;
    }

    public float getSpatial() {
        return spatial;
    }

    public float getSpatialAmount() {
        return spatialAmount;
    }

    public int getBaseEnergyPerTick() {
        return baseEnergyPerTick;
    }
}
