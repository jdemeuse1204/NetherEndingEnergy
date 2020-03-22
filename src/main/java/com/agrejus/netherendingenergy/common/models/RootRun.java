package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class RootRun implements IRoot {
    private final BlockPos origin;
    private final Direction travelingDirection;
    private final int minDistance;
    private final int maxDistance;

    public RootRun(BlockPos origin, Direction travelingDirection){
        this(origin, travelingDirection, 3, 10);
    }

    public RootRun(BlockPos origin, Direction travelingDirection, int minDistance, int maxDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.origin = origin;
        this.travelingDirection = travelingDirection;
    }

    public int getDistance(BlockPos position) {
        return BlockHelpers.getDistance(this.origin, position);
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public boolean mustTurn(BlockPos position) {
        int distance = getDistance(position);
        return distance > maxDistance;
    }

    public boolean canTurn(BlockPos position) {
        int distance = getDistance(position);
        return distance >= minDistance;
    }

    public Direction getTravelingDirection() {
        return travelingDirection;
    }
}
