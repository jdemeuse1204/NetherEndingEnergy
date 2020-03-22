package com.agrejus.netherendingenergy.common.fluids;

import com.agrejus.netherendingenergy.common.attributes.CustomFluidAttributes;
import com.agrejus.netherendingenergy.common.attributes.AcidAttributes;
import net.minecraft.nbt.CompoundNBT;

public final class FluidHelpers {
    public static AcidAttributes deserializeAttributes(CompoundNBT tag) {
        float strength = tag.getFloat("strength");
        float efficiency = tag.getFloat("efficiency");
        float stability = tag.getFloat("stability");
        float response = tag.getFloat("response");
        float spatialAmount = tag.getFloat("spatialAmount");
        int basePerTick = tag.getInt("basePerTick");
        float spatial = tag.getFloat("spatial");
        float decayRate = tag.getFloat("decayRate");
        return new AcidAttributes(strength, efficiency, stability, response, spatial, spatialAmount, basePerTick, decayRate);
    }

    public static void serializeCustomFluidAttributes(CompoundNBT tag, AcidAttributes attributes) {
        tag.putFloat("strength", attributes.getStrength());
        tag.putFloat("efficiency", attributes.getEfficiency());
        tag.putFloat("stability", attributes.getStability());
        tag.putFloat("response", attributes.getResponse());
        tag.putInt("basePerTick", attributes.getBaseEnergyPerTick());
        tag.putFloat("spatialAmount", attributes.getSpatialAmount());
        tag.putFloat("spatial", attributes.getSpatial());
        tag.putFloat("decayRate", attributes.getDecayRate());
    }

    public static void serializeCustomFluidAttributes(CompoundNBT tag, CustomFluidAttributes attributes, float spatialAmount) {
        tag.putFloat("strength", attributes.getStrength());
        tag.putFloat("efficiency", attributes.getEfficiency());
        tag.putFloat("stability", attributes.getStability());
        tag.putFloat("response", attributes.getResponse());
        tag.putInt("basePerTick", attributes.getBaseEnergyPerTick());
        tag.putFloat("spatialAmount", spatialAmount);
        tag.putFloat("spatial", attributes.getSpatial());
        tag.putFloat("decayRate", attributes.getDecayRate());
    }
}
