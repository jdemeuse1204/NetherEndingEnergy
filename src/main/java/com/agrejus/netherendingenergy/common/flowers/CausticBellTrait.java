package com.agrejus.netherendingenergy.common.flowers;

import net.minecraft.util.IStringSerializable;

public enum CausticBellTrait implements IStringSerializable {
    // rolls = 0-1000
    DORMANT("dormant", -1, -1),
    POLLINATING("pollinating", 201, 250),
    POTENT("potent", 251, 270),
    ADAPTOR("adaptor", 271, 400),
    NOXIOUS("noxious", 0, 200),
    AIMLESS("aimless", 401, 500),
    ROAMING("roaming", 501, 520),
    UNSTABLE("unstable",521, 700),
    STEADY("steady", 701, 800),
    ALTITUDINAL("altidudinal", 801, 1000);

    private final String trait;
    private final int randomRollStart;
    private final int randomRollEnd;

    private CausticBellTrait(String trait, int randomRollStart, int randomRollEnd) {
        this.trait = trait;
        this.randomRollStart = randomRollStart;
        this.randomRollEnd = randomRollEnd;
    }

    public static CausticBellTrait getTrait(int roll) {
        for(CausticBellTrait trait: values()) {
            if (roll >= trait.randomRollStart && roll <= trait.randomRollEnd) {
                return trait;
            }
        }

        return DORMANT;
    }

    @Override
    public String getName() {
        return this.trait;
    }
}