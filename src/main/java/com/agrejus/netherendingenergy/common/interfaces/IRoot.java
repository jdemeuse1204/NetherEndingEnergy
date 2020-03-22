package com.agrejus.netherendingenergy.common.interfaces;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public interface IRoot {
    int getDistance(BlockPos position);
    BlockPos getOrigin();
    boolean canTurn(BlockPos position);
    boolean mustTurn(BlockPos position);
    Direction getTravelingDirection();
}
