package com.agrejus.netherendingenergy.tools;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomVaporStorage  extends VaporStorage implements INBTSerializable<CompoundNBT> {

    public CustomVaporStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    public void setVapor(int vapor) {
        this.vapor = vapor;
    }

    public void addVapor(int vapor) {
        this.vapor += vapor;
        if (this.vapor > getMaxVaporStored()) {
            this.vapor = getVaporStored();
        }
    }

    public void consumeVapor(int vapor) {
        this.vapor -= vapor;
        if (this.vapor < 0) {
            this.vapor = 0;
        }
    }

    /*To Save Energy When Block is Broken*/
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("vapor", getVaporStored());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setVapor(nbt.getInt("vapor"));
    }
}
