package com.agrejus.netherendingenergy.common.tank;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;

public class PassThroughFluidTank extends FluidTank {

    private int transferRate;
    private IFluidHandler passThroughTank;

    public PassThroughFluidTank(int transferRate) {
        super(0);
        this.transferRate = transferRate;
    }

    public void setPassThroughTank(IFluidHandler tank) {
        this.passThroughTank = tank;
    }

    public boolean hasPassThrough() {
        return this.passThroughTank != null;
    }

    public int getTransferRate() {
        return this.transferRate;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.getAmount() > this.getTransferRate()) {
            FluidStack reducedFluidStack = getReducedFluidStack(resource);
            return this.passThroughTank.fill(reducedFluidStack, action);
        }
        return this.passThroughTank.fill(resource, action);
    }

    private FluidStack getReducedFluidStack(FluidStack resource) {
        FluidStack reducedFluidStack = new FluidStack(resource.getFluid(), this.getTransferRate());

        // Transfer Tag is exists
        CompoundNBT tag = resource.getTag();
        if (tag != null && tag.isEmpty() == false) {
            reducedFluidStack.setTag(tag);
        }

        return reducedFluidStack;
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.getAmount() > this.getTransferRate()) {
            FluidStack reducedFluidStack = getReducedFluidStack(resource);
            return this.passThroughTank.drain(reducedFluidStack, action);
        }
        return this.passThroughTank.drain(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        int rate = maxDrain;
        if (maxDrain > this.getTransferRate()) {
            rate = this.getTransferRate();
        }
        return this.passThroughTank.drain(rate, action);
    }
}
