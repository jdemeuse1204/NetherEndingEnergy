package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.interfaces.ISourceRoot;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class BranchingSourceRoot implements ISourceRoot {
    private BlockPos pos;
    private Map<Direction, RootBud> source;
    private final int maxSpread;

    public BranchingSourceRoot(BlockPos pos, int maxSpread) {
        this.pos = pos;
        this.source = new HashMap<>();
        this.maxSpread = maxSpread;
    }

    public int getMaxSpread() {
        return maxSpread;
    }

    public BlockPos getPos() {
        return pos;
    }

    public RootBud getMainTrunk(Direction direction) {
        return this.source.getOrDefault(direction, null);
    }

    public void setMainTrunk(Direction direction, RootBud bud) {
        this.source.put(direction, bud);
    }
}
