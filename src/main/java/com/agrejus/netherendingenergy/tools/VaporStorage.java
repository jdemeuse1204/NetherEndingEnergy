package com.agrejus.netherendingenergy.tools;

import com.agrejus.netherendingenergy.common.interfaces.IVaporStorage;

public class VaporStorage implements IVaporStorage {
    protected int vapor;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public VaporStorage(int capacity) {
        this(capacity, capacity, capacity, 0);
    }

    public VaporStorage(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer, 0);
    }

    public VaporStorage(int capacity, int maxReceive, int maxExtract) {
        this(capacity, maxReceive, maxExtract, 0);
    }

    public VaporStorage(int capacity, int maxReceive, int maxExtract, int vapor) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
        this.vapor = Math.max(0, Math.min(capacity, vapor));
    }

    @Override
    public int receiveVapor(int maxReceive, boolean simulate) {
        if (!canReceive())
            return 0;

        int vaporReceived = Math.min(capacity - vapor, Math.min(this.maxReceive, maxReceive));
        if (!simulate)
            vapor += vaporReceived;
        return vaporReceived;
    }

    @Override
    public int extractVapor(int maxExtract, boolean simulate) {
        if (!canExtract())
            return 0;

        int energyExtracted = Math.min(vapor, Math.min(this.maxExtract, maxExtract));
        if (!simulate)
            vapor -= energyExtracted;
        return energyExtracted;
    }

    @Override
    public int getVaporStored() {
        return vapor;
    }

    @Override
    public int getMaxVaporStored() {
        return capacity;
    }

    @Override
    public boolean canExtract() {
        return this.maxExtract > 0;
    }

    @Override
    public boolean canReceive() {
        return this.maxReceive > 0;
    }
}
