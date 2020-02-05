package com.agrejus.netherendingenergy.common.fluids;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.registry.Registry;

public final class FluidHelpers {
    public static <T extends Fluid> T registerFluid(String key, T p_215710_1_) {
        return (T)(Registry.register(Registry.FLUID, key, p_215710_1_));
    }

    public static Block registerFluidBlock(String key, Block p_222382_1_) {
        return Registry.register(Registry.BLOCK, key, p_222382_1_);
    }
}
