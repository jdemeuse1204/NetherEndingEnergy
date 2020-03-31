package com.agrejus.netherendingenergy.blocks.terra.mixer.tasks.payload;

import com.agrejus.netherendingenergy.common.interfaces.ITickingTaskPayload;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;

public class TerraMixerTaskPayload implements ITickingTaskPayload {

    public TerraMixerTaskPayload(LazyOptional<NEEFluidTank> inputTank) {
        this.inputFluidStack =  inputTank.map(w -> w.getFluid()).orElse(FluidStack.EMPTY);
        this.inputFluid = inputFluidStack.getFluid();
    }

    private FluidStack inputFluidStack;
    private Fluid inputFluid;

    public FluidStack getInputFluidStack() {
        return inputFluidStack;
    }

    public Fluid getInputFluid() {
        return inputFluid;
    }
}
