package com.agrejus.netherendingenergy;

import com.agrejus.netherendingenergy.tools.GrowthMediumValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class Capabilities {
    @CapabilityInject(IFluidHandler[].class)
    public static Capability<IFluidHandler[]> MULTI_FLUID_HANDLER_CAPABILITY = null;

    @CapabilityInject(GrowthMediumValues.class)
    public static Capability<GrowthMediumValues> GROWTH_MEDIUM_CAPABILITY = null;
}
