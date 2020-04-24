package com.agrejus.netherendingenergy.common.models;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public class NumberRange implements INBTSerializable<CompoundNBT> {
    private float min;
    private float max;
    private float current;

    public NumberRange(float min, float max) {
        this.min = min;
        this.max = max;
        this.current = min;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getCurrent() {
        return current;
    }

    public boolean isWithinRange(float value) {
        return value >= min && value <= max;
    }

    public void setCurrent(float current) {

        if (current < min || current > max) {
            throw new IllegalStateException("current number must be within number range");
        }

        this.current = current;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putFloat("min", this.min);
        tag.putFloat("max", this.max);
        tag.putFloat("current", this.current);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.min = nbt.getFloat("min");
        this.max = nbt.getFloat("max");
        this.current = nbt.getFloat("current");
    }
}
