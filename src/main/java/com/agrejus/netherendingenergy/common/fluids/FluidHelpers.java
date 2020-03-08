package com.agrejus.netherendingenergy.common.fluids;

import com.agrejus.netherendingenergy.common.fluids.attributes.AcidAttributes;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.registry.Registry;

public final class FluidHelpers {
/*    public static <T extends Fluid> T registerFluid(String key, T p_215710_1_) {
        return (T)(Registry.register(Registry.FLUID, key, p_215710_1_));
    }

    public static Block registerFluidBlock(String key, Block p_222382_1_) {
        return Registry.register(Registry.BLOCK, key, p_222382_1_);
    }*/

    public static AcidAttributes deserializeAttributes(CompoundNBT tag) {
        float strength = tag.getFloat("strength");
        float efficiency = tag.getFloat("efficiency");
        float stability = tag.getFloat("stability");
        float response = tag.getFloat("response");
        int uses = tag.getInt("uses");
        int spatialAmount = tag.getInt("spatialAmount");
        float spatial = tag.getFloat("spatial");
        return new AcidAttributes(strength, efficiency, stability, response, uses, spatial, spatialAmount);
    }

    public static void serializeCustomFluidAttributes(CompoundNBT tag, AcidAttributes attributes) {
        tag.putFloat("strength", attributes.getStrength());
        tag.putFloat("efficiency", attributes.getEfficiency());
        tag.putFloat("stability", attributes.getStability());
        tag.putFloat("response", attributes.getResponse());
        tag.putInt("uses", attributes.getUses());
        tag.putInt("spatialAmount", attributes.getSpatialAmount());
        tag.putFloat("spatial", attributes.getSpatial());
    }

    public static void serializeCustomFluidAttributes(CompoundNBT tag, CustomFluidAttributes attributes) {
        tag.putFloat("strength", attributes.getStrength());
        tag.putFloat("efficiency", attributes.getEfficiency());
        tag.putFloat("stability", attributes.getStability());
        tag.putFloat("response", attributes.getResponse());
        tag.putInt("uses", attributes.getUses());
        tag.putInt("spatialAmount", attributes.getSpatialAmount());
        tag.putFloat("spatial", attributes.getSpatial());
    }
}
