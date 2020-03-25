package com.agrejus.netherendingenergy.blocks.flowers.roots;

import com.agrejus.netherendingenergy.common.interfaces.ISourceRoot;
import com.agrejus.netherendingenergy.common.models.BranchingSourceRoot;
import com.agrejus.netherendingenergy.common.models.SourceRoot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class RootSystem implements INBTSerializable<CompoundNBT> {
    private ISourceRoot[] sourceRoots;
    private final BlockPos origin;
    private int branchingRootCount;

    public RootSystem(int size, BlockPos origin) {
        this.origin = origin;
        this.sourceRoots = new ISourceRoot[size];
        BlockPos startingPosition = new BlockPos(origin.getX(), origin.getY(), origin.getZ());
        int maxSpreadSize = 32;
        branchingRootCount = 0;

        for (int i = 0; i < size; i++) {
            if (i == 0 || i % 5 == 0) {
                int maxSpread = maxSpreadSize - (branchingRootCount * 8);
                this.sourceRoots[i] = new BranchingSourceRoot(startingPosition, maxSpread);
                branchingRootCount++;
            } else {
                this.sourceRoots[i] = new SourceRoot(startingPosition);
            }
            startingPosition = startingPosition.offset(Direction.DOWN);
        }
    }

    public int getBranchingRootCount() {
        return branchingRootCount;
    }

    public int size() {
        return this.sourceRoots.length;
    }

    public ISourceRoot get(int index) {
        return this.sourceRoots[index];
    }

    public BlockPos getOrigin() {
        return origin;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

    }
}
