package com.agrejus.netherendingenergy.common.models;

import net.minecraftforge.energy.IEnergyStorage;

public class EnergyUsePackage {
    private int lastEnergy;
    private int useage;

    public void track(IEnergyStorage storage) {
        this.useage = storage.getEnergyStored() - this.lastEnergy;
        this.lastEnergy =  storage.getEnergyStored();
    }

    public int trackAndGet(IEnergyStorage storage) {
        this.useage = storage.getEnergyStored() - this.lastEnergy;
        this.lastEnergy =  storage.getEnergyStored();

        return this.useage;
    }

    public int getUsage() {
        return this.useage;
    }
}
