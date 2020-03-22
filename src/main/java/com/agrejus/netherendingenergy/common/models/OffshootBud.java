package com.agrejus.netherendingenergy.common.models;

import net.minecraft.util.Direction;

public class OffshootBud {

    private final Direction growthDirection;
    private OffshootRoot offshootRoot;

    public OffshootBud(Direction growthDirection) {
        this.growthDirection = growthDirection;
    }

    public OffshootRoot getOffshootRoot() {
        return this.offshootRoot;
    }

    public void setOffshootRoot(OffshootRoot offshootRoot) {
        this.offshootRoot = offshootRoot;
    }

    public Direction getGrowthDirection() {
        return this.growthDirection;
    }
}
