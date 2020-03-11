package com.agrejus.netherendingenergy.common.reactor;

public enum ReactorSlotType {
    MainBacklog,
    MainBurning,
    NorthInjector,
    WestInjector,
    SouthInjector,
    EastInjector;

    public static final ReactorSlotType[] VALUES = ReactorSlotType.values();
}
