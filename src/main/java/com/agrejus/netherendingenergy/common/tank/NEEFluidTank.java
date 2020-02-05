package com.agrejus.netherendingenergy.common.tank;

import net.minecraftforge.fluids.capability.templates.FluidTank;

public class NEEFluidTank extends FluidTank {

    private final String name;

    public NEEFluidTank(String name, int capacity) {
        super(capacity);
        this.name = name;
    }

    public boolean canFill() {
        return getFluidAmount() < getCapacity();
    }

    public String getName() {
        return name;
    }

    public int resolveDrainAmount(int amount) {
        int fillAmount = getFluidAmount();
        if (amount > fillAmount) {
            return fillAmount;
        }
        return amount;
    }

    public int resolveFillAmount(int amount) {
        int space = getSpace();
        if (amount > space) {
            return space;
        }
        return amount;
    }
}
