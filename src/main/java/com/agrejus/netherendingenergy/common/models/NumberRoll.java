package com.agrejus.netherendingenergy.common.models;

public class NumberRoll {
    private final int min;
    private final int max;
    private final int result;

    public NumberRoll(int min, int max, int result) {
        this.min = min;
        this.max = max;
        this.result = result;
    }

    public boolean isBetween(int value) {
        return value >= this.min && value <= this.max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getResult() {
        return result;
    }
}
