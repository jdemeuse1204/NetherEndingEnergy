package com.agrejus.netherendingenergy.common.models;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class ProcessingUnit implements INBTSerializable<CompoundNBT> {

    private int tick;
    private int totalTicks;

    public ProcessingUnit(int totalTicks) {
        this(0, totalTicks);
    }

    public ProcessingUnit(int tick, int totalTicks) {
        this.tick = tick;
        this.totalTicks = totalTicks;
    }

    public int getTick() {
        return tick;
    }

    public int getTotalTicks() {
        return totalTicks;
    }

    public boolean isProcessingFinished() {
        return this.tick >= this.totalTicks;
    }

    public int increment() {
        ++this.tick;
        return this.tick;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
