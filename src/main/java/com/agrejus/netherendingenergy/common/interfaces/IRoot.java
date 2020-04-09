package com.agrejus.netherendingenergy.common.interfaces;

import com.agrejus.netherendingenergy.common.models.RootBud;
import com.agrejus.netherendingenergy.common.models.RootPoint;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;

public interface IRoot extends INBTSerializable<CompoundNBT> {
    int size();
    RootPoint get(int index);
    RootPoint plotAndGetPoint(BlockPos position, int index);
    RootPoint getLastRootPoint();
    Direction getTravelingDirection();
    Direction getLastBudSideOfTrunk();
    BlockPos getOrigin();
    BlockPos getNextPosition(int lastIndex);
    ArrayList<RootBud> getBuds();
}
