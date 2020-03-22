package com.agrejus.netherendingenergy.common.models;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class RootPoint {
    private final BlockPos position;

    // could be an offshoot or a main trunk extension
    private final OffshootBud offshootBud;

    public RootPoint(BlockPos position) {
        this(position, null);
    }

    public RootPoint(BlockPos position, @Nullable OffshootBud offshootBud) {
        this.position = position;
        this.offshootBud = offshootBud;
    }

    public boolean isOffshootBudGrown() {
        return this.offshootBud != null && this.offshootBud.getOffshootRoot() != null;
    }

    public boolean hasOffshootBud() {
        return this.offshootBud != null;
    }

    public OffshootRoot getOffshootRoot() {
        return this.offshootBud == null ? null : this.offshootBud.getOffshootRoot();
    }

    public OffshootRoot addAndGetOffshoot(BlockPos startingPosition, Direction travelingDirection, int size) {
        OffshootRoot offshootRoot = new OffshootRoot(startingPosition, travelingDirection, this.offshootBud.getGrowthDirection(), size);

        this.offshootBud.setOffshootRoot(offshootRoot);

        return offshootRoot;
    }

    public BlockPos getPosition() {
        return this.position;
    }
}
