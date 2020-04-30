package com.agrejus.netherendingenergy.common.interfaces;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IProcessingUnit extends INBTSerializable<CompoundNBT> {
    void setCurrent(int current);
    int getTotal();
    int getValue();
    void setNext();
    void setNext(int amount);
    boolean canProcess();
    void reset();
    void setTotal(int total);
}
