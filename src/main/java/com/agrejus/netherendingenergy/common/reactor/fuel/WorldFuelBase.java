package com.agrejus.netherendingenergy.common.reactor.fuel;

public class WorldFuelBase {

    private final float responsePercent;

    private final int energyAugment;
    private final int stepAmount;
    private final int maxLevel;
    private final int baseLevel;//y or xz
    private final float spatialModPercent;


    public WorldFuelBase(int energyAugment, float responsePercent, int maxLevel, int baseLevel, float spatialModPercent, int stepAmount) {
        this.responsePercent = responsePercent;
        this.maxLevel = maxLevel;
        this.baseLevel = baseLevel;
        this.spatialModPercent = spatialModPercent;
        this.stepAmount = stepAmount;
        this.energyAugment = energyAugment;
    }

    public float getResponsePercent() {
        return responsePercent;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getBaseLevel() {
        return baseLevel;
    }

    public float getSpatialModPercent() {
        return spatialModPercent;
    }

    public int getStepAmount() {
        return stepAmount;
    }

    public int getEnergyAugment() {
        return energyAugment;
    }
}
