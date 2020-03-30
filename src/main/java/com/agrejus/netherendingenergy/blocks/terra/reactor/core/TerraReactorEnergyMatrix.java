package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.common.attributes.AcidAttributes;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseType;
import com.agrejus.netherendingenergy.common.attributes.PotionAttributes;
import com.agrejus.netherendingenergy.common.reactor.fuel.WorldFuelBase;

import java.util.List;

public class TerraReactorEnergyMatrix {
    public static int getEnergyPerTick(ReactorBaseType type, AcidAttributes attributes, List<PotionAttributes> injectedAttributes) {
        WorldFuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        return computeEnergyPerTick(attributes, fuelBase, injectedAttributes);
    }

    public static float computeHeat(float currentHeat, float absorptionRate, int tickRate, ReactorBaseType type, AcidAttributes attributes, List<PotionAttributes> injectedAttributes) {
        // higher the absorption rate the faster the reactor heats up
        int uses = 100;
        float stepIncrease = 5f;
        float stability = getStability(type, attributes, injectedAttributes);
        float maxReactingHeatGenerated = ((((1f - stability) * 1000f) * stepIncrease) + stepIncrease);
        float heatGenerated = (((maxReactingHeatGenerated / (float) uses)) / absorptionRate) / (float) tickRate;
        float result = (currentHeat + heatGenerated);

        if (result >= maxReactingHeatGenerated) {
            return maxReactingHeatGenerated;
        }

        return result;
    }

    public static float getEfficiency(ReactorBaseType type, AcidAttributes attributes, List<PotionAttributes> injectedAttributes) {
        WorldFuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        float response = getTotalResponse(fuelBase, attributes, injectedAttributes);
        float injectorEfficiency = getInjectedPotionsEfficiency(injectedAttributes, response);
        return modify(attributes.getEfficiency(), injectorEfficiency);
    }

    public static float getTotalResponse(WorldFuelBase fuelBase, AcidAttributes attributes, List<PotionAttributes> injectedAttributes) {
        float injectorResponse = getInjectedPotionsResponse(injectedAttributes);
        return fuelBase.getResponsePercent() + attributes.getResponse() + injectorResponse;
    }

    private static float getInjectedPotionsStrength(List<PotionAttributes> injectedPotions, float response) {
        int size = injectedPotions.size();
        float result = 0;
        for (int i = 0; i < size; i++) {
            PotionAttributes attributes = injectedPotions.get(i);
            result += attributes.getStrength();
        }

        return modify(result, response);
    }

    private static float getInjectedPotionsStability(List<PotionAttributes> injectedPotions, float response) {
        int size = injectedPotions.size();
        float result = 0;
        for (int i = 0; i < size; i++) {
            PotionAttributes attributes = injectedPotions.get(i);
            result += attributes.getStability();
        }

        return modify(result, response);
    }

    private static float getInjectedPotionsEfficiency(List<PotionAttributes> injectedPotions, float response) {
        int size = injectedPotions.size();
        float result = 0;
        for (int i = 0; i < size; i++) {
            PotionAttributes attributes = injectedPotions.get(i);
            result += attributes.getEfficiency();
        }

        return modify(result, response);
    }

    private static float getInjectedPotionsResponse(List<PotionAttributes> injectedPotions) {
        int size = injectedPotions.size();
        float result = 0;
        for (int i = 0; i < size; i++) {
            PotionAttributes attributes = injectedPotions.get(i);
            result += attributes.getResponse();
        }

        return result;
    }

    public static float modify(float initial, float modifyPercent) {

        if (initial < 0) {
            return initial + Math.abs(initial * modifyPercent);
        }

        return initial + (initial * modifyPercent);
    }

    private static float getStabilityEnergy(AcidAttributes attributes, List<PotionAttributes> injectedPotions, float response) {
        float stability = getStability(attributes, injectedPotions, response);
        float stabilityCalculated = 1;

        if (stability < 1) {
            stabilityCalculated = (Math.abs(stability - 1f) * 10f) / .05f;
        }

        if (stability > 1) {
            // Penalize for too much stability, change .1 to smaller number to penalize more
            stabilityCalculated = (-Math.abs(stability - 1f) * 10f) / .1f;
        }

        return stabilityCalculated;
    }

    private static float getStrength(AcidAttributes attributes, List<PotionAttributes> injectedAttributes, float response) {
        float injectorStrength = getInjectedPotionsStrength(injectedAttributes, response);
        return modify(attributes.getStrength(), injectorStrength);
    }

    private static float getStability(AcidAttributes attributes, List<PotionAttributes> injectedPotions, float response) {
        float injectorStability = getInjectedPotionsStability(injectedPotions, response);
        return modify(attributes.getStability(), injectorStability);
    }

    private static float getSpatial(WorldFuelBase fuelBase, AcidAttributes attributes) {
        return modify(fuelBase.getSpatialModPercent(), attributes.getSpatial());
    }

    public static float getStability(ReactorBaseType type, AcidAttributes attributes, List<PotionAttributes> injectedPotions) {
        WorldFuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        float response = getTotalResponse(fuelBase, attributes, injectedPotions);
        return getStability(attributes, injectedPotions, response);
    }

    private static int computeEnergyPerTick(AcidAttributes attributes, WorldFuelBase fuelBase, List<PotionAttributes> injectedPotions) {

        // Get base properties
        float response = getTotalResponse(fuelBase, attributes, injectedPotions);
        float strength = getStrength(attributes, injectedPotions, response);

        // Base spatial modifiers on the fluid not the world, just get the steps from the world
        float spatial = getSpatial(fuelBase, attributes);
        float spatialSteps = attributes.getSpatialAmount();
        float spatialModified = modify(spatial, spatialSteps);

        float stabilityEnergy = getStabilityEnergy(attributes, injectedPotions, response);

        // Stability only modifies RF/t
        float strengthEnergy = modify(attributes.getBaseEnergyPerTick(), strength);
        float spatialEnergy = modify(strengthEnergy, spatialModified);
        float energyPerTick = spatialEnergy + stabilityEnergy;

        return (int) energyPerTick;
    }
}
