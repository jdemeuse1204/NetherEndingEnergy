package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.common.reactor.attributes.AcidAttributes;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseType;
import com.agrejus.netherendingenergy.common.reactor.attributes.PotionAttributes;
import com.agrejus.netherendingenergy.common.reactor.fuel.FuelBase;
import net.minecraft.potion.Potion;

import java.util.List;

public class TerraReactorEnergyMatrix {
    public static int getEnergyPerTick(ReactorBaseType type, AcidAttributes attributes, List<PotionAttributes> injectedAttributes) {
        FuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        return computeEnergyPerTick(attributes, fuelBase, injectedAttributes);
    }

    public static float computeHeat(float currentHeat, float absorptionRate, int tickRate, ReactorBaseType type, AcidAttributes attributes, List<PotionAttributes> injectedAttributes) {
        // higher the absorption rate the faster the reactor heats up
        float stepIncrease = 5f;
        float stability = getStability(type, attributes, injectedAttributes);
        float maxReactingHeatGenerated = ((((1f - stability) * 1000f) * stepIncrease) + stepIncrease);
        float heatGenerated = (((maxReactingHeatGenerated / (float) attributes.getUses())) / absorptionRate) / (float) tickRate;
        float result = (currentHeat + heatGenerated);

        if (result >= maxReactingHeatGenerated) {
            return maxReactingHeatGenerated;
        }

        return result;
    }

    public static float getEfficiency(ReactorBaseType type, AcidAttributes attributes, List<PotionAttributes> injectedAttributes) {
        FuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        float injectorEfficiency = getInjectedPotionsEfficiency(injectedAttributes);
        float efficiency = attributes.getEfficiency() * fuelBase.getEfficiencyPercent();

        return augmentWithInjector(efficiency, injectorEfficiency);
    }

    public static float getResponse(ReactorBaseType type, AcidAttributes attributes) {
        FuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        float injectorResponse = 0;
        return (attributes.getResponse() * fuelBase.getResponsePercent()) + injectorResponse;
    }

    private static float getInjectedPotionsStrength(List<PotionAttributes> injectedPotions) {
        // optimize this, no streams
        return (float) injectedPotions.stream().mapToDouble(w -> w.getStrength()).sum();
    }

    private static float getInjectedPotionsStability(List<PotionAttributes> injectedPotions) {
        return (float) injectedPotions.stream().mapToDouble(w -> w.getStability()).sum();
    }

    private static float getInjectedPotionsEfficiency(List<PotionAttributes> injectedPotions) {
        return (float) injectedPotions.stream().mapToDouble(w -> w.getEfficiency()).sum();
    }

    private static float getInjectedPotionsResponse(List<PotionAttributes> injectedPotions) {
        return (float) injectedPotions.stream().mapToDouble(w -> w.getResponse()).sum();
    }

    private static float augmentWithInjector(float base, float injector) {
        return injector == 0 ? base : (base + (injector * base));
    }

    private static float getStabilityEnergy(FuelBase fuelBase, AcidAttributes attributes, List<PotionAttributes> injectedPotions) {
        float stability = getStability(fuelBase, attributes, injectedPotions);
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

    private static float getStability(FuelBase fuelBase, AcidAttributes attributes, List<PotionAttributes> injectedPotions) {
        float stability = attributes.getStability() * fuelBase.getStabilityPercent();
        float injectorStability = getInjectedPotionsStability(injectedPotions);
        return augmentWithInjector(stability, injectorStability);
    }

    public static float getStability(ReactorBaseType type, AcidAttributes attributes, List<PotionAttributes> injectedPotions) {
        FuelBase fuelBase = ReactorBaseConfig.getBaseFuel(type);
        return getStability(fuelBase, attributes, injectedPotions);
    }

    private static int computeEnergyPerTick(AcidAttributes attributes, FuelBase fuelBase, List<PotionAttributes> injectedPotions) {
        float injectorStrength = getInjectedPotionsStrength(injectedPotions);

        // Get base properties
        float strength = attributes.getStrength() * fuelBase.getStrengthPercent();

        // Adjust for injectors
        float injectorAdjustedStrength = augmentWithInjector(strength, injectorStrength);

        // Base spatial modifiers on the fluid not the world, just get the steps from the world
        float spatial = (float) Math.pow((1 + attributes.getSpatial()), attributes.getSpatialAmount());
        float stability = getStabilityEnergy(fuelBase, attributes, injectedPotions);

        // Stability only modifies RF/t
        float energyPerTick = ((fuelBase.getBaseEnergyPerTick() * injectorAdjustedStrength) * spatial) + stability;

        return (int) energyPerTick;
    }
}
