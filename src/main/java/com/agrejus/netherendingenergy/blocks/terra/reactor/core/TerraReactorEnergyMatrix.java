package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.common.fluids.attributes.AcidAttributes;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseType;
import com.agrejus.netherendingenergy.common.reactor.fuel.FuelBase;

public class TerraReactorEnergyMatrix {
    public static int getEnergyPerTick(ReactorBaseType type, AcidAttributes attributes) {
        FuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        return computeEnergyPerTick(attributes, fuelBase);
    }

    public static float getEfficiency(ReactorBaseType type, AcidAttributes attributes) {
        FuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        float injectorEfficiency = 0;
        return (attributes.getEfficiency() * fuelBase.getEfficiencyPercent()) + injectorEfficiency;
    }

    public static float getResponse(ReactorBaseType type, AcidAttributes attributes) {
        FuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        float injectorResponse = 0;
        return (attributes.getResponse() * fuelBase.getResponsePercent()) + injectorResponse;
    }

    private static int computeEnergyPerTick(AcidAttributes attributes, FuelBase fuelBase) {
        float injectorStrength = 0;
        float injectorStability = 0;

        float strength = (attributes.getStrength() * fuelBase.getStrengthPercent()) + injectorStrength;
        float stability = (attributes.getStability() * fuelBase.getStabilityPercent()) + injectorStability;

        // Base spatial modifiers on the fluid not the world, just get the steps from the world
        float spatial = (float) Math.pow((1 + attributes.getSpatial()), attributes.getSpatialAmount());

        float stabilityCalculated = 1;

        if (stability < 1) {
            stabilityCalculated = (Math.abs(stability - 1f) * 10f) / .05f;
        }

        if (stability > 1) {
            // Penalize for too much stability, change .1 to smaller number to penalize more
            stabilityCalculated = (-Math.abs(stability - 1f) * 10f) / .1f;
        }

        // Stability only modifies RF/t
        float energyPerTick = ((fuelBase.getBaseEnergyPerTick() * strength) * spatial) + stabilityCalculated;

        return (int) energyPerTick;
    }
}
