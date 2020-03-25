package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class RootPoint {
    private final BlockPos position;

    private RootBud offshootBud;

    public RootPoint(BlockPos position) {
        this.position = position;
    }

    public RootBud getOffshootBud() {
        return this.offshootBud;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public void setOffshootBud(RootBud offshootBud) {
        this.offshootBud = offshootBud;
    }
}
