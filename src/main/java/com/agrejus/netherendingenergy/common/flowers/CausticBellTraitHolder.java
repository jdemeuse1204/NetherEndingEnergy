package com.agrejus.netherendingenergy.common.flowers;

public class CausticBellTraitHolder {
    public final CausticBellTrait SuperiorTrait;
    public final CausticBellTrait InferiorTrait;
    public final CausticBellTrait RecessiveTrait;

    public  CausticBellTraitHolder(CausticBellTrait superiorTrait, CausticBellTrait inferiorTrait, CausticBellTrait recessiveTrait) {
        this.SuperiorTrait = superiorTrait;
        this.InferiorTrait = inferiorTrait;
        this.RecessiveTrait = recessiveTrait;
    }
}
