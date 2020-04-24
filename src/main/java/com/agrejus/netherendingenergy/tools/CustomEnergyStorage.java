package com.agrejus.netherendingenergy.tools;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {

    private int usageTrackingStartingAmount;
    private int usageTrackingEndingAmount;

    public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public void consumeEnergyPerTick() {
        this.extractEnergy(this.maxExtract, false);
    }

    public boolean hasEnoughEnergyStored() {
        return this.getEnergyStored() >= this.maxExtract;
    }

    public int getMaxExtract() {
        return this.maxExtract;
    }

    public int getMaxReceive() {
        return this.maxReceive;
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
        if (this.energy > getMaxEnergyStored()) {
            this.energy = getEnergyStored();
        }
    }

    public void consumeEnergy(int energy) {
        this.energy -= energy;
        if (this.energy < 0) {
            this.energy = 0;
        }
    }

    public void startUsageTracking() {
        this.usageTrackingStartingAmount = getEnergyStored();
    }

    public void endUsageTracking() {
        this.usageTrackingEndingAmount = getEnergyStored();
    }

    public int getUsage() {
        return this.usageTrackingStartingAmount - this.usageTrackingEndingAmount;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("energy", getEnergyStored());
        tag.putInt("maxReceive", this.maxReceive);
        tag.putInt("maxExtract", this.maxExtract);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setEnergy(nbt.getInt("energy"));
        setMaxExtract(nbt.getInt("maxExtract"));
        setMaxReceive(nbt.getInt("maxReceive"));
    }
}
