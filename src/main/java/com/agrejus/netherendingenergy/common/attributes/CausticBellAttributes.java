package com.agrejus.netherendingenergy.common.attributes;

import com.agrejus.netherendingenergy.common.Ratio;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;

public class CausticBellAttributes {

    private CausticBellTrait superiorTrait;
    private CausticBellTrait inferiorTrait;
    private CausticBellTrait recessiveTrait;
    private Ratio strength;
    private Ratio purity;
    private Ratio burnTimeAugmentRatio;
    private int yield;

    public CausticBellAttributes(CausticBellTrait superiorTrait, CausticBellTrait inferiorTrait, CausticBellTrait recessiveTrait, Ratio strength, Ratio purity, Ratio burnTimeAugmentRatio, int yield) {
        this.superiorTrait = superiorTrait;
        this.inferiorTrait = inferiorTrait;
        this.recessiveTrait = recessiveTrait;
        this.strength = strength;
        this.purity = purity;
        this.burnTimeAugmentRatio = burnTimeAugmentRatio;
        this.yield = yield;
    }

    public CausticBellTrait getSuperiorTrait() {
        return superiorTrait;
    }

    public CausticBellTrait getInferiorTrait() {
        return inferiorTrait;
    }

    public CausticBellTrait getRecessiveTrait() {
        return recessiveTrait;
    }

    public Ratio getStrength() {
        return strength;
    }

    public Ratio getPurity() {
        return purity;
    }

    public Ratio getBurnTimeAugmentRatio() {
        return burnTimeAugmentRatio;
    }

    public int getYield() {
        return yield;
    }
}
