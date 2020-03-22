package com.agrejus.netherendingenergy.common.models;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class JunctionRootRun extends RootRun {

    public JunctionRootRun(BlockPos origin, Direction travelingDirection) {
        this(origin,travelingDirection, 1, 3);
    }

    public JunctionRootRun(BlockPos origin, Direction travelingDirection, int minDistance, int maxDistance) {
        super(origin, travelingDirection, minDistance, maxDistance);
    }
}
