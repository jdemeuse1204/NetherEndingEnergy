package com.agrejus.netherendingenergy.blocks.flowers.roots;

import com.agrejus.netherendingenergy.NetherEndingEnergyConfig;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import com.agrejus.netherendingenergy.common.interfaces.ISourceRoot;
import com.agrejus.netherendingenergy.common.models.BranchingSourceRoot;
import com.agrejus.netherendingenergy.common.models.RootBud;
import com.agrejus.netherendingenergy.common.models.SourceRoot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class RootSystem implements INBTSerializable<CompoundNBT> {
    private ISourceRoot[] sourceRoots;
    private BlockPos origin;
    private int branchingRootCount;
    private int size;

    public RootSystem(){}
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

        this.size = size;
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
        CompoundNBT tag = new CompoundNBT();

        CompoundNBT originNbt = new CompoundNBT();
        NBTHelpers.writeToNBT(originNbt, this.origin);
        tag.put("origin", originNbt);

        tag.putInt("size", this.size);
        tag.putInt("branching_root_count", this.branchingRootCount);

        int size = sourceRoots.length;
        for (int i = 0; i < size; i++) {
            ISourceRoot sourceRoot = sourceRoots[i];

            if (sourceRoot == null) {
                continue;
            }

            tag.put("source_root" + i, sourceRoot.serializeNBT());

        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        CompoundNBT originNbt = (CompoundNBT) nbt.get("origin");
        this.origin = NBTHelpers.readBlockPosFromNBT(originNbt);
        this.branchingRootCount = nbt.getInt("branching_root_count");
        this.size = nbt.getInt("size");
        this.sourceRoots = new ISourceRoot[this.size];

        for (int i = 0; i < this.size; i++) {
            String key = "source_root" + i;

            if (nbt.contains(key)) {
                CompoundNBT sourceRootNbt = (CompoundNBT)nbt.get(key);

                boolean doesBranch = sourceRootNbt.getBoolean("does_branch");
                ISourceRoot root;
                if(doesBranch) {
                    root = new BranchingSourceRoot();
                } else {
                    root = new SourceRoot();
                }
                root.deserializeNBT(sourceRootNbt);
                this.sourceRoots[i] = root;
            }
        }
    }
}
