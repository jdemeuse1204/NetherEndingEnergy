package com.agrejus.netherendingenergy.common.helpers;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class BlockHelpers {
    public static int getDistance(BlockPos origin, BlockPos destination) {
        double x = Math.pow(destination.getX() - origin.getX(), 2);
        double z = Math.pow(destination.getZ() - origin.getZ(), 2);

        return (int) Math.sqrt(x + z);
    }

    public static int getDeviation(BlockPos startingBlockPosition, BlockPos nextBlockPosition, Direction travelingDirection) {
        switch (travelingDirection) {
            default:
            case NORTH:
            case SOUTH:
                return startingBlockPosition.getX() - nextBlockPosition.getX();
            case EAST:
            case WEST:
                return startingBlockPosition.getZ() - nextBlockPosition.getZ();
        }
    }

    public static int deviate(Direction deviationDirection) {
        switch (deviationDirection) {
            default:
            case EAST:
            case SOUTH:
                return 1;
            case NORTH:
            case WEST:
                return -1;
        }
    }

    public static Direction getDeviationDirection(int deviation, Direction travelingDirection) {
        switch (travelingDirection) {
            default:
            case EAST:
            case WEST:
                return deviation < 0 ? Direction.NORTH : Direction.SOUTH;
            case NORTH:
            case SOUTH:
                return deviation < 0 ? Direction.EAST : Direction.WEST;
        }
    }

    public static BlockPos deviateBlockPosition(BlockPos pos, int deviation, Direction travelingDirection) {
        switch (travelingDirection) {
            default:
            case NORTH:
            case SOUTH:
                return new BlockPos(pos.getX(), pos.getY(), pos.getZ() + deviation);
            case EAST:
            case WEST:
                return new BlockPos(pos.getX() + deviation, pos.getY(), pos.getZ());
        }
    }

    public static Direction getDirection(int positiveOrNegative, ArrayList<Direction> perpendicularDirections) {
        if (perpendicularDirections.contains(Direction.NORTH)) {
            return positiveOrNegative < 0 ? Direction.NORTH : Direction.SOUTH;
        }

        return positiveOrNegative < 0 ? Direction.WEST : Direction.EAST;
    }

    public static ArrayList<Direction> getPerpendicularDirections(Direction travelingDirection) {
        switch (travelingDirection) {
            case EAST:
            case WEST:
                return new ArrayList<Direction>() {
                    {
                        add(Direction.NORTH);
                        add(Direction.SOUTH);
                    }
                };
            case NORTH:
            case SOUTH:
                return new ArrayList<Direction>() {
                    {
                        add(Direction.EAST);
                        add(Direction.WEST);
                    }
                };
            default:
                throw new IllegalStateException("Direction not allowed");
        }
    }
}
