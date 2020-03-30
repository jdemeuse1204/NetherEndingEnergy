package com.agrejus.netherendingenergy.common.tank;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class NEEFluidTank extends FluidTank {
    public NEEFluidTank(int capacity) {
        super(capacity);
    }

    public boolean hasRoom() {
        return this.getCapacity() > this.getFluidAmount();
    }

    public int simulateFillAmount(int amount) {

        if (this.getFluidAmount() == 0 && this.getCapacity() > amount) {
            return amount;
        }

        Fluid fluid = this.fluid.getFluid();
        return this.fill(new FluidStack(fluid, amount), FluidAction.SIMULATE);
    }

    public FluidStack simulateDrainAmount(int amount) {
        if (this.getFluidAmount() == 0) {
            return FluidStack.EMPTY;
        }

        Fluid fluid = this.fluid.getFluid();
        return this.drain(new FluidStack(fluid, amount), FluidAction.SIMULATE);
    }
}
