package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class OffshootRoot implements IRoot {
    private final BlockPos origin;
    private final Direction travelingDirection;
    private final Direction sideOfMainTrunkDirection;
    private final RootPoint[] rootPoints;
    private boolean isDead;

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

    public RootPoint plotAndGetPoint(BlockPos position, int index) {
        RootPoint point = new RootPoint(position);
        rootPoints[index] = point;
        return point;
    }

    public RootPoint getLastRootPoint() {
        return rootPoints[rootPoints.length - 1];
    }

    public Direction getTravelingDirection() {
        return this.travelingDirection;
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

    @Override
    public BlockPos getOrigin() {
        return rootPoints[0].getPosition();
    }

    public Direction getSideOfMainTrunkDirection() {
        return sideOfMainTrunkDirection;
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
}
