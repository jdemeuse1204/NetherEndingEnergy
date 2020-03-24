package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.enumeration.RootType;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class MainTrunkRoot implements IRoot {

    private final BlockPos origin;
    private final Direction travelingDirection;
    private final RootPoint[] rootPoints;

    public MainTrunkRoot(BlockPos origin, Direction travelingDirection, int size) {
        this.origin = origin;
        this.travelingDirection = travelingDirection;
        rootPoints = new RootPoint[size];
        rootPoints[0] = new RootPoint(origin);
    }

    public RootPoint plotAndGetPoint(BlockPos position, int index) {
        RootPoint point = new RootPoint(position);
        rootPoints[index] = point;
        return point;
    }

    public boolean canDeviate(BlockPos position) {
        if (travelingDirection == Direction.NORTH || travelingDirection == Direction.SOUTH) {
            // x cannot deviate
            return Math.abs(position.getX() - this.origin.getX()) <= 1;
        }

        return Math.abs(position.getZ() - this.origin.getZ()) <= 1;
    }

    @Override
    public ArrayList<RootBud> getBuds() {
        ArrayList<RootBud> result = new ArrayList<>();

        for (int i = 0; i < rootPoints.length; i++) {
            RootPoint point = rootPoints[i];
            RootBud bud = point.getOffshootBud();
            if (bud != null) {
                result.add(bud);
            }
        }

        return result;
    }

    public int size() {
        return this.rootPoints.length;
    }

    public RootPoint get(int index) {
        return rootPoints[index];
    }

    public RootPoint getLastRootPoint() {
        return rootPoints[rootPoints.length - 1];
    }

    public Direction getTravelingDirection() {
        return this.travelingDirection;
    }

    @Override
    public BlockPos getOrigin() {
        return rootPoints[0].getPosition();
    }

    @Override
    public Direction getLastBudSideOfTrunk() {
        for (int i = rootPoints.length - 1; i >= 0; i--) {
            RootPoint point = rootPoints[i];

            if (point == null) {
                continue;
            }

            RootBud bud = point.getOffshootBud();
            if (bud != null) {
                return bud.getSideOfTrunkDirection();
            }
        }

        ArrayList<Direction> perpendicularDirections = BlockHelpers.getPerpendicularDirections(travelingDirection);
        int directionIndex = NetherEndingEnergy.roll(0, 1);

        return perpendicularDirections.get(directionIndex);
    }

    public BlockPos getNextPosition(int lastIndex) {
        BlockPos pos = rootPoints[lastIndex].getPosition();
        return pos.offset(travelingDirection);
    }
}
