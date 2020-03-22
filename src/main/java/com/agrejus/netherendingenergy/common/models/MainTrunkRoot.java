package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import javafx.beans.property.IntegerProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class MainTrunkRoot {
    // is Dead
    // List off shoots with block position
    // each position should have a list of offshoots

    // Left and right just like tree traversal
    private final BlockPos origin;
    private final Direction travelingDirection;
    private final RootPoint[] rootPoints;
    private boolean isDead;

    public static final int MIN_LENGTH = 7;
    public static final int MAX_LENGTH = 12;
    public static final int MIN_DEVIATION = 0;
    public static final int MAX_DEVIATION = 1;
    public static final int MIN_OFFSHOOT_INDEX = 5;
    public static final int MAX_OFFSHOOT_INDEX = 12;
    public static final int MAX_OFFSHOOT_COUNT = 2;
    public static final int MIN_OFFSHOOT_COUNT = 1;

    public MainTrunkRoot(BlockPos origin, Direction travelingDirection, int size) {
        this.origin = origin;
        this.travelingDirection = travelingDirection;
        rootPoints = new RootPoint[size];
        rootPoints[0] = new RootPoint(origin);
    }

    public BlockPos getNextPosition(int lastIndex) {
        BlockPos pos = rootPoints[lastIndex].getPosition();
        return pos.offset(travelingDirection);
    }

    public void plotPoint(BlockPos position, int index, OffshootBud offshootBud) {
        rootPoints[index] = new RootPoint(position, offshootBud);
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

    /*public OffshootRoot getOffshoot(int index) {
        RootPoint rootPoint = rootPoints[index];
        return rootPoint.getOffshootRoot();
    }

    public OffshootRoot createOffshoot(int index) {
        int maxIndex = this.size() - 1;

        if (index > maxIndex) {
            index = maxIndex;
        }

        RootPoint rootPoint = rootPoints[index];
        ArrayList<Direction> perpendicularDirections = BlockHelpers.getPerpendicularDirections(travelingDirection);
        int directionIndex = NetherEndingEnergy.roll(0, 1);
        Direction direction = perpendicularDirections.get(directionIndex);

        if (rootPoint != null) {
            BlockPos startingPoint = rootPoint.getPosition().offset(direction);
            int size = NetherEndingEnergy.roll(OffshootRoot.MIN_LENGTH, OffshootRoot.MAX_LENGTH);
            return rootPoint.addOffshoot(startingPoint, travelingDirection, direction, size);
        }

        throw new IllegalStateException("rootPoint cannot be null");
    }*/

    public boolean canDeviate(BlockPos position) {
        if (travelingDirection == Direction.NORTH || travelingDirection == Direction.SOUTH) {
            // x cannot deviate
            return Math.abs(position.getX() - this.origin.getX()) <= 1;
        }

        return Math.abs(position.getZ() - this.origin.getZ()) <= 1;
    }

    public boolean isDead() {
        return isDead;
    }

    public int size() {
        return this.rootPoints.length;
    }

    public RootPoint get(int index) {
        return rootPoints[index];
    }

    public Direction getTravelingDirection() {
        return this.travelingDirection;
    }
}
