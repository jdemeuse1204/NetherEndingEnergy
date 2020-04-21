package com.agrejus.netherendingenergy.common.models;

import net.minecraft.util.math.BlockPos;

public class AbsorbableBlock {
    private final BlockPos triggeringPos;
    private final BlockPos pos;
    private boolean shouldAbsorb;

    public AbsorbableBlock(BlockPos pos, BlockPos triggeringPos, boolean shouldAbsorb) {
        this.triggeringPos = triggeringPos;
        this.pos = pos;
        this.shouldAbsorb = shouldAbsorb;
    }

    public BlockPos getTriggeringPos() {
        return triggeringPos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public boolean isShouldAbsorb() {
        return shouldAbsorb;
    }

    public void setShouldAbsorb(boolean shouldAbsorb) {
        this.shouldAbsorb = shouldAbsorb;
    }
}
