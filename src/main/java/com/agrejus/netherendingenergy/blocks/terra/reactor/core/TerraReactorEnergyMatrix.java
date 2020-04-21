package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.common.attributes.AcidAttributes;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseType;
import com.agrejus.netherendingenergy.common.attributes.PotionAttributes;
import com.agrejus.netherendingenergy.common.reactor.fuel.WorldFuelBase;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

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
        float sumInjectorEfficiency = getInjectedAndModifiedPotionsEfficiency(injectedAttributes, response);
        float spatialAmount = getSpatialAmount(fuelBase, attributes);
        float injectedTotal = sumInjectorEfficiency * response;
        float spatialTotal = modify(attributes.getEfficiency(), spatialAmount);

        return injectedTotal + spatialTotal;
    }

    public static int computeBurnTime(int burnTime, float efficiency, ReactorBaseType type, AcidAttributes attributes, List<PotionAttributes> injectedAttributes) {
        WorldFuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        float response = getTotalResponse(fuelBase, attributes, injectedAttributes);
        float decayResistance = getInjectedAndModifiedPotionsDecayResistance(injectedAttributes, response);
        float resistanceAugmentation = modify(burnTime, decayResistance);
        float decayBurnTime = resistanceAugmentation / attributes.getDecayRate();
        return Math.round(efficiency * decayBurnTime);
    }

    public static float getTotalResponse(WorldFuelBase fuelBase, AcidAttributes attributes, List<PotionAttributes> injectedAttributes) {
        float sumInjectorResponse = getInjectedPotionsResponse(injectedAttributes);
        return fuelBase.getResponsePercent() + attributes.getResponse() + sumInjectorResponse;
    }

    private static float getInjectedAndModifiedPotionsDecayResistance(List<PotionAttributes> injectedPotions, float response) {
        return modify(getInjectorSum(injectedPotions, w -> w.getDecayResistance()), response);
    }

    private static float getInjectedAndModifiedPotionsStrength(List<PotionAttributes> injectedPotions, float response) {
        return modify(getInjectorSum(injectedPotions, w -> w.getStrength()), response);
    }

    private static float getInjectedAndModifiedPotionsStability(List<PotionAttributes> injectedPotions, float response) {
        return modify(getInjectorSum(injectedPotions, w -> w.getStability()), response);
    }

    private static float getInjectedAndModifiedPotionsEfficiency(List<PotionAttributes> injectedPotions, float response) {
        return modify(getInjectorSum(injectedPotions, w -> w.getEfficiency()), response);
    }

    private static float getInjectedPotionsResponse(List<PotionAttributes> injectedPotions) {
        return getInjectorSum(injectedPotions, w -> w.getResponse());
    }

    private static float getInjectorSum(List<PotionAttributes> injectedPotions, Function<PotionAttributes, Float> sumFunction) {
        int size = injectedPotions.size();
        float result = 0;
        for (int i = 0; i < size; i++) {
            PotionAttributes attributes = injectedPotions.get(i);
            result += sumFunction.apply(attributes);
        }

        return result;
    }

    public static float modify(float initial, float modifyPercent) {

        if (initial < 0) {
            return initial + Math.abs(initial * modifyPercent);
        }

        return initial + (initial * modifyPercent);
    }

    // Extra Energy Produced By Instability
    private static float getStabilityEnergy(AcidAttributes attributes, List<PotionAttributes> injectedPotions, float response) {
        float stability = getStability(attributes, injectedPotions, response);
        float stabilityCalculated = 0;

        if (stability < 1) {
            stabilityCalculated = (Math.abs(stability - 1f) * 10f) / .05f;
        }

        return stabilityCalculated;
    }


    // Any Number
    private static float getStrength(AcidAttributes attributes, List<PotionAttributes> injectedAttributes, float response, float spatialTotalAmount) {
        float sumInjectorStrength = getInjectedAndModifiedPotionsStrength(injectedAttributes, response);
        float injectedTotal = sumInjectorStrength * response;
        float spatialTotal = modify(attributes.getStrength(), spatialTotalAmount);

        return spatialTotal + injectedTotal;
    }

    // Between 0-1, Over 1 = No energy, below 0 = boom
    private static float getStability(AcidAttributes attributes, List<PotionAttributes> injectedPotions, float response) {
        float sumInjectorStability = getInjectedAndModifiedPotionsStability(injectedPotions, response);
        float injectedTotal = sumInjectorStability * response;
        // No Spatial Effect
        return injectedTotal + attributes.getStability();
    }

    // Spatial Amount
    private static float getSpatialAmount(WorldFuelBase fuelBase, AcidAttributes attributes) {
        float result = Math.round((attributes.getSpatialAmount() - fuelBase.getBaseLevel()) / fuelBase.getStepAmount());
        return result < 0 ? 0 : result;
    }

    // Between 0-1, Over 1 = No energy, below 0 = boom
    public static float getStability(ReactorBaseType type, AcidAttributes attributes, List<PotionAttributes> injectedPotions) {
        WorldFuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        float response = getTotalResponse(fuelBase, attributes, injectedPotions);
        return getStability(attributes, injectedPotions, response);
    }

    private static int computeEnergyPerTick(AcidAttributes attributes, WorldFuelBase fuelBase, List<PotionAttributes> injectedPotions) {

        float spatialAmount = getSpatialAmount(fuelBase, attributes);

        // Get base properties
        float response = getTotalResponse(fuelBase, attributes, injectedPotions);
        float strength = getStrength(attributes, injectedPotions, response, spatialAmount);

        // Base spatial modifiers on the fluid not the world, just get the steps from the world
        float stabilityEnergy = getStabilityEnergy(attributes, injectedPotions, response);
        float energy = modify(attributes.getBaseEnergyPerTick(), strength);

        return Math.round(energy + stabilityEnergy);
    }
}
