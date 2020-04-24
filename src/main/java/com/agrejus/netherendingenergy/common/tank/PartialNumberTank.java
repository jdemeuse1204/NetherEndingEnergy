package com.agrejus.netherendingenergy.common.tank;

import net.minecraft.fluid.Fluid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

public class PartialNumberTank extends NEEFluidTank {

    private float remainder;

    public PartialNumberTank(int capacity) {
        super(capacity);
    }

    public float simulateFillAmount(float amount) {
        if (this.getFluidAmount() == 0 && this.getCapacity() > amount) {
            return amount;
        }

        Fluid fluid = this.fluid.getFluid();
        return this.fill(new FluidStack(fluid, (int)amount), amount, FluidAction.SIMULATE);
    }

    public int fill(FluidStack resource, float actualAmount, FluidAction action) {

        this.remainder += actualAmount % 1;
        int wholeAmount = (int) actualAmount;

        if (this.remainder > 1) {
            int amountToAdd = (int) this.remainder;
            wholeAmount += amountToAdd;
            this.remainder -= amountToAdd;
        }

        resource.setAmount(wholeAmount);

        return super.fill(resource, action);
    }

    @Override
    public PartialNumberTank readFromNBT(CompoundNBT nbt) {
        FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
        setFluid(fluid);
        this.remainder = nbt.getFloat("remainder");
        return this;
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        super.writeToNBT(nbt);
        nbt.putFloat("remainder",this.remainder);
        return nbt;
    }
}
