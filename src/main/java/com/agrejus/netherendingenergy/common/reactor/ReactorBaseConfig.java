package com.agrejus.netherendingenergy.common.reactor;

import com.agrejus.netherendingenergy.common.reactor.fuel.FuelBase;

public class ReactorBaseConfig {
    public static FuelBase getBaseFuel(ReactorBaseType type) {
        switch (type) {
            default:
            case Terra:
                return new FuelBase(1f, .7f, 1f,  .06f, 360, 16);
            case Chaotic:
                return new FuelBase(2.4f, 2.5f, .5f, .08f, 420, 32);
            case Abyssal:
                return new FuelBase(1.7f, 3.5f, 2f,  .1f, 640, 64);
        }
    }
}
