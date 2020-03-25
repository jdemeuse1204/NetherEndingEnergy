package com.agrejus.netherendingenergy.common.interfaces;

import com.agrejus.netherendingenergy.common.models.RootBud;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public interface ISourceRoot {
    int getMaxSpread();
    BlockPos getPos();
    RootBud getMainTrunk(Direction direction);
    void setMainTrunk(Direction direction, RootBud bud);
}
