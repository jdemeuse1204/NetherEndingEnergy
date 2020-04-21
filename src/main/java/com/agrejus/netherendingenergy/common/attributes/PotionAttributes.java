package com.agrejus.netherendingenergy.common.attributes;

public class PotionAttributes extends AcidAttributeBase {
    private final int uses;
    private final float decayResistance;

    public PotionAttributes(float strength, float efficiency, float stability, float response, int uses, float decayResistance) {
        super(strength, efficiency, stability, response);
        this.uses = uses;
        this.decayResistance = decayResistance;
    }

    public int getUses() {
        return uses;
    }

    public float getDecayResistance() {
        return decayResistance;
    }
}
