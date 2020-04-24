package com.agrejus.netherendingenergy.common.factories;

import com.agrejus.netherendingenergy.common.enumeration.LevelType;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;

public class EnergyStoreFactory {
    public static CustomEnergyStorage createEnergyStore(LevelType type) {
        switch (type) {
            default:
            case TERRA:
                return new CustomEnergyStorage(60000, 80, 40);
            case CHAOTIC:
                return new CustomEnergyStorage(120000, 80, 40);
            case ABYSSAL:
                return new CustomEnergyStorage(240000, 80, 40);
        }
    }
}
