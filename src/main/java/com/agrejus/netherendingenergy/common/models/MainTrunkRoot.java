package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.enumeration.RootType;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class MainTrunkRoot implements IRoot {

    private BlockPos origin;
    private Direction travelingDirection;
    private RootPoint[] rootPoints;
    private int size;

    public MainTrunkRoot(){}

    public MainTrunkRoot(BlockPos origin, Direction travelingDirection, int size) {
        this.size = size;
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

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        CompoundNBT originTag = new CompoundNBT();
        NBTHelpers.writeToNBT(originTag, this.origin);
        tag.put("origin", originTag);
        tag.putString("traveling_direction", travelingDirection.getName());
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

        CompoundNBT originTag = (CompoundNBT)nbt.get("origin");
        this.origin = NBTHelpers.readBlockPosFromNBT(originTag);
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
