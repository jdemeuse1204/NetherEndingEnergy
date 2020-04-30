package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.interfaces.IProcessingUnit;
import net.minecraft.nbt.CompoundNBT;

public class ProcessingUnit implements IProcessingUnit {

    public int current;
    public int total;

    public ProcessingUnit() {
        this(0, 0);
    }

    public ProcessingUnit(int total) {
        this(total, 0);
    }

    public ProcessingUnit(int total, int seed) {
        this.total = total;
        this.current = seed;
    }

    @Override
    public boolean canProcess() {
        return this.total == this.current;
    }

    @Override
    public int getTotal() {
        return this.total;
    }

    @Override
    public void setCurrent(int current) {
        this.current = current;
    }

    @Override
    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public void reset() {
        this.current = 0;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("total", this.total);
        tag.putInt("current", this.current);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.total = nbt.getInt("total");
        this.current = nbt.getInt("current");
    }

    @Override
    public int getValue() {
        return this.current;
    }

    @Override
    public void setNext() {
        ++this.current;

        if (this.current > this.total) {
            this.current = 0;
        }
    }

    @Override
    public void setNext(int amount) {
        this.current += amount;

        if (this.current > this.total) {
            this.current = 0;
        }
    }
}
