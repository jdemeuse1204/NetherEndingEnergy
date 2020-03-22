package com.agrejus.netherendingenergy.common.flowers;

import net.minecraft.util.IStringSerializable;

public enum CausticBellTrait implements IStringSerializable {
    // rolls = 0-1000

    // Dormant - Nothing special, chance of recessive trait becoming dominant
    // Yield - 1mB
    // Purity - 5:6
    // Strength - 1:1
    // Burn Tick Augment - 1:1
    // 15%
    DORMANT("dormant", 851, 1000),

    // Pollinating accelerates breeding/spreading process in surrounding flowers
    // Yield - 3mB
    // Purity - 5:6
    // Strength - 1:1
    // Burn Tick Augment - 1:1
    // 10%
    POLLINATING("pollinating", 281, 380),

    // Potent - High Yield
    // Yield - 15mB
    // Purity - 5:6
    // Strength - 1:1
    // Burn Tick Augment - 1:1
    POTENT("potent", 31, 100),

    // Adaptor - Easily breedable, can take on any trait
    // Yield - 6mB
    // Purity - 5:6
    // Strength - 1:1
    // Burn Tick Augment - 1:1
    // 9%
    ADAPTOR("adaptor", 101, 190),

    // Noxious - Best trait - Easily breedable, withering if touched.  if player stays in the area too long it will poison and then wither
    // Yield - 20mB
    // Purity - 5:6
    // Strength - 1:1
    // Burn Tick Augment - 1:1
    // 3%
    NOXIOUS("noxious", 1, 30),

    // Roaming - Very hard to breed, but high chance of getting a good trait
    // Yield - 3mB
    // Purity - 5:6
    // Strength - 1:1
    // Burn Tick Augment - 1:1
    // 15%
    AIMLESS("aimless", 701, 850),

    // Roaming - Will spread to other blocks when planted on caustic farmland
    // Yield - 3mB
    // Purity - 5:6
    // Strength - 1:1
    // Burn Tick Augment - 1:1
    // 10%
    ROAMING("roaming", 381, 480),

    // Unstable - Causes random effects (nausea, poison, mining fatigue, withering)
    // Yield - 10mB
    // Purity - 5:6
    // Strength - 1:1
    // Burn Tick Augment - 1:1
    // 9%
    UNSTABLE("unstable",191, 280),

    // Steady - Nothing Special
    // Yield - 5mB
    // Purity - 5:6
    // Strength - 1:1
    // Burn Tick Augment - 1:1
    // 10%
    STEADY("steady", 481, 580),

    // Higher the altitude the better the yield
    // STEP -  8 Blocks, sea level=Y64, 24 Steps
    // Yield - 1 mB
    // Purity - 5:6
    // Strength - 1:1
    // Burn Tick Augment - 1:1
    // 12%
    ALTITUDINAL("altitudinal", 581, 700);

    private final String trait;
    private final int randomRollStart;
    private final int randomRollEnd;

    // Optimization
    public static final CausticBellTrait[] VALUES = CausticBellTrait.values();

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