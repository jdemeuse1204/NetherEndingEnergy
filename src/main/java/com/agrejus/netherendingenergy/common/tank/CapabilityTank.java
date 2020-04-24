package com.agrejus.netherendingenergy.common.tank;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;

public class CapabilityTank implements IFluidHandler {

    private final FluidTank drainTank;
    private final FluidTank fillTank;

    public CapabilityTank(FluidTank drainTank, FluidTank fillTank) {
        this.drainTank = drainTank;
        this.fillTank = fillTank;
    }

    @Override
    public int getTanks() {
        return 2;
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {

        if (tank > 2) {
            return FluidStack.EMPTY;
        }

        if (tank == 1) {
            return this.drainTank.getFluidInTank(0);
        }

        return this.fillTank.getFluidInTank(0);
    }

    @Override
    public int getTankCapacity(int tank) {
        if (tank > 2) {
            return 0;
        }

        if (tank == 1) {
            return this.drainTank.getTankCapacity(0);
        }

        return this.fillTank.getTankCapacity(0);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        if (tank > 2) {
            return false;
        }

        if (tank == 1) {
            return this.drainTank.isFluidValid(0, stack);
        }

        return this.fillTank.isFluidValid(0, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return this.fillTank.fill(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return this.drainTank.drain(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return this.drainTank.drain(maxDrain, action);
    }
}
