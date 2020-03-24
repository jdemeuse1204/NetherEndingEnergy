package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class RootPoint {
    private final BlockPos position;

    private RootBud offshootBud;
    private RootBud mainTrunkOffshootBud;

    public RootPoint(BlockPos position) {
        this.position = position;
    }

    public boolean isOffshootBudGrown() {
        return this.offshootBud != null && this.offshootBud.getRoot() != null;
    }

    public RootBud getMainTrunkOffshootBud() {
        return this.mainTrunkOffshootBud;
    }

    public RootBud getOffshootBud() {
        return this.offshootBud;
    }

    public boolean hasOffshootBud() {
        return this.offshootBud != null;
    }

    public IRoot getOffshootRoot() {
        return this.offshootBud == null ? null : this.offshootBud.getRoot();
    }

    public OffshootRoot addAndGetOffshoot(BlockPos startingPosition, Direction travelingDirection, int size) {
        OffshootRoot offshootRoot = new OffshootRoot(startingPosition, travelingDirection, this.offshootBud.getGrowthDirection(), size);

        this.offshootBud.setRoot(offshootRoot);

        return offshootRoot;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public void setOffshootBud(RootBud offshootBud) {
        this.offshootBud = offshootBud;
    }

    public void setMainTrunkOffshootBud(RootBud mainTrunkOffshootBud) {
        this.mainTrunkOffshootBud = mainTrunkOffshootBud;
    }
}
