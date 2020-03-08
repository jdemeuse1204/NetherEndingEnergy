package com.agrejus.netherendingenergy.common.reactor.fuel;

public class FuelBase {
    private final float strengthPercent;
    private final float efficiencyPercent;
    private final float stabilityPercent;
    private final float responsePercent;

    private final int baseEnergyPerTick;
    private final int maxSpace;

    public FuelBase(float strengthPercent, float efficiencyPercent, float stabilityPercent, float responsePercent, int baseEnergyPerTick, int maxSpace) {
        this.strengthPercent = strengthPercent;
        this.efficiencyPercent = efficiencyPercent;
        this.stabilityPercent = stabilityPercent;
        this.responsePercent = responsePercent;
        this.baseEnergyPerTick = baseEnergyPerTick;
        this.maxSpace = maxSpace;
    }

    public float getStrengthPercent() {
        return strengthPercent;
    }

    public float getEfficiencyPercent() {
        return efficiencyPercent;
    }

    public float getStabilityPercent() {
        return stabilityPercent;
    }

    public float getResponsePercent() {
        return responsePercent;
    }

    public int getBaseEnergyPerTick() {
        return baseEnergyPerTick;
    }

    public int getMaxSpace() {
        return maxSpace;
    }
}
