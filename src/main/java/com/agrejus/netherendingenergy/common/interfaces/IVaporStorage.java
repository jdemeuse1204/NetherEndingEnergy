package com.agrejus.netherendingenergy.common.interfaces;

public interface IVaporStorage {
    int receiveVapor(int maxReceive, boolean simulate);
    int extractVapor(int maxExtract, boolean simulate);
    int getVaporStored();
    int getMaxVaporStored();
    boolean canExtract();
    boolean canReceive();
}
