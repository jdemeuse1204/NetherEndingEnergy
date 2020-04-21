package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.ISourceRoot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

public class SourceRoot implements ISourceRoot {
    private BlockPos pos;

    public SourceRoot() {
    }

    public SourceRoot(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public int getMaxSpread() {
        return 0;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public RootBud getMainTrunk(Direction direction) {
        return null;
    }

    @Override
    public void setMainTrunk(Direction direction, RootBud bud) {

    }

    @Override
    public CompoundNBT serializeNBT() {

        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("does_branch", false);
        NBTHelpers.writeToNBT(tag, this.pos);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

        this.pos = NBTHelpers.readBlockPosFromNBT(nbt);
    }
}
