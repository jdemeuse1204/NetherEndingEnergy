package com.agrejus.netherendingenergy.common.tank;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class AcidFluidTank extends FluidTank {
    public AcidFluidTank(int capacity) {
        super(capacity);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {

        if (action == FluidAction.SIMULATE) {
            return super.fill(resource, action);
        }

        // Lets make sure we can add before we start mixing
        int simulatedFillAmount = super.fill(resource, FluidAction.SIMULATE);
        if (simulatedFillAmount != resource.getAmount() || simulatedFillAmount == 0) {
            return 0;
        }

        // Set the fluid stack first
        int result = super.fill(resource, action);

        //Set the tag, this is temporary!
        fluid.setTag(resource.getTag());

        // Mix fluid stack tags if they are the

        return result;
    }
}
