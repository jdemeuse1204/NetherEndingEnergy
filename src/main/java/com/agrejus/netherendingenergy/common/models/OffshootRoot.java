package com.agrejus.netherendingenergy.common.models;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class OffshootRoot {
    private final BlockPos origin;
    private final Direction travelingDirection;
    private final Direction sideOfMainTrunkDirection;
    private final RootPoint[] rootPoints;
    private boolean isDead;

    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 14;
    public static final int MIN_DEVIATION = 2;
    public static final int MAX_DEVIATION = 6;

    // how far away from trunk do we need to travel (deviation)?  Could be 1, but there might not be space to grow, need to check space to grow before growing
    public OffshootRoot(BlockPos origin, Direction travelingDirection, Direction sideOfMainTrunkDirection, int size) {
        this.origin = origin;
        this.travelingDirection = travelingDirection;
        this.sideOfMainTrunkDirection = sideOfMainTrunkDirection;
        rootPoints = new RootPoint[size];

        rootPoints[0] = new RootPoint(origin);
    }

    public int size() {
        return this.rootPoints.length;
    }

    public RootPoint get(int index) {
        return this.rootPoints[index];
    }

    public BlockPos getNextPosition(int lastIndex) {
        BlockPos pos = rootPoints[lastIndex].getPosition();
        return pos.offset(travelingDirection);
    }

    public void plotPoint(BlockPos position, int index) {
        rootPoints[index] = new RootPoint(position);
    }


    private int indexOf(BlockPos pos) {
        int size = rootPoints.length;
        for (int i = 0; i < size; i++) {
            if (pos.equals(rootPoints[i])) {
                return i;
            }
        }

        return -1;
    }

    public Direction getSideOfMainTrunkDirection() {
        return sideOfMainTrunkDirection;
    }
}
