package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class OffshootRoot implements IRoot {

    private Direction travelingDirection;
    private Direction sideOfMainTrunkDirection;
    private RootPoint[] rootPoints;
    private int size;

    public OffshootRoot() {
    }

    // how far away from trunk do we need to travel (deviation)?  Could be 1, but there might not be space to grow, need to check space to grow before growing
    public OffshootRoot(BlockPos origin, Direction travelingDirection, Direction sideOfMainTrunkDirection, int size) {
        this.travelingDirection = travelingDirection;
        this.sideOfMainTrunkDirection = sideOfMainTrunkDirection;
        this.size = size;
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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putString("traveling_direction", travelingDirection.getName());
        tag.putString("side_of_main_trunk_direction", sideOfMainTrunkDirection.getName());
        tag.putInt("size", this.size);

        for (int i = 0; i < this.size; i++) {
            RootPoint point = this.rootPoints[i];
            CompoundNBT pointNbt = point.serializeNBT();
            tag.put("root_point" + i, pointNbt);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.travelingDirection = Direction.byName(nbt.getString("traveling_direction"));
        this.sideOfMainTrunkDirection = Direction.byName(nbt.getString("side_of_main_trunk_direction"));
        this.size = nbt.getInt("size");
        this.rootPoints = new RootPoint[this.size];

        for (int i = 0; i < this.size; i++) {
            String key = "root_point" + i;

            if (nbt.contains(key)) {
                RootPoint point = new RootPoint();
                CompoundNBT pointNbt = (CompoundNBT)nbt.get(key);
                point.deserializeNBT(pointNbt);
                this.rootPoints[i] = point;
            }
        }
    }
}
