package com.agrejus.netherendingenergy.common.interfaces;

import com.agrejus.netherendingenergy.common.models.RootBud;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public interface ISourceRoot extends INBTSerializable<CompoundNBT> {
    int getMaxSpread();
    BlockPos getPos();
    RootBud getMainTrunk(Direction direction);
    void setMainTrunk(Direction direction, RootBud bud);
}
