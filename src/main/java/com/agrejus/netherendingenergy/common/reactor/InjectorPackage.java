package com.agrejus.netherendingenergy.common.reactor;

import com.agrejus.netherendingenergy.common.attributes.PotionAttributes;

import java.util.List;

public class InjectorPackage {

    private final List<PotionAttributes> potionAttributes;
    private final List<Runnable> deferredUsages;

    public InjectorPackage(List<PotionAttributes> potionAttributes, List<Runnable> deferredUsages) {
        this.potionAttributes = potionAttributes;
        this.deferredUsages = deferredUsages;
    }

    public List<PotionAttributes> getPotionAttributes() {
        return potionAttributes;
    }

    public List<Runnable> getDeferredUsages() {
        return deferredUsages;
    }
}
