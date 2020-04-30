package com.agrejus.netherendingenergy.common.models;

public class InvertedProcessingUnit extends ProcessingUnit {

    public InvertedProcessingUnit() {
        super(0, 0);
    }

    public InvertedProcessingUnit(int total) {
        super(total, 0);
    }

    public InvertedProcessingUnit(int total, int seed) {
        super(total, seed);
    }

    @Override
    public void setNext() {
        --this.current;

        if (this.current < 0) {
            this.current = this.total;
        }
    }

    @Override
    public void setNext(int amount) {
        this.current -= amount;

        if (this.current < 0) {
            this.current = this.total;
        }
    }

    @Override
    public boolean canProcess() {
        return this.current == 0;
    }

    @Override
    public void reset() {
        this.current = this.total;
    }
}
