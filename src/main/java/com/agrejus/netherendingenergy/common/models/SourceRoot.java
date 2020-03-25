package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.interfaces.ISourceRoot;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class SourceRoot implements ISourceRoot {
    private final BlockPos pos;

    public SourceRoot(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public int getMaxSpread() {
        return 0;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public RootBud getMainTrunk(Direction direction) {
        return null;
    }

    @Override
    public void setMainTrunk(Direction direction, RootBud bud) {

    }
}
