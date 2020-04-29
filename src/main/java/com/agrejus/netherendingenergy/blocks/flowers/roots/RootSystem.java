package com.agrejus.netherendingenergy.blocks.flowers.roots;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.ISourceRoot;
import com.agrejus.netherendingenergy.common.models.BranchingSourceRoot;
import com.agrejus.netherendingenergy.common.models.SourceRoot;
import jdk.nashorn.internal.ir.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class RootSystem implements INBTSerializable<CompoundNBT> {
    private ISourceRoot[] sourceRoots;
    private BlockPos origin;
    private int branchingRootCount;
    private int size;

    public RootSystem() {
    }

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

    private BlockPos trySetRandomGrowth(World world) {
        int rootIndex = NetherEndingEnergy.roll(0, (this.sourceRoots.length - 1));
        ISourceRoot root = this.sourceRoots[rootIndex];

        // Need a branching root
        while (root == null || root instanceof SourceRoot) {
            rootIndex = NetherEndingEnergy.roll(0, (this.sourceRoots.length - 1));
            root = this.sourceRoots[rootIndex];
        }

        BranchingSourceRoot branchingSourceRoot = (BranchingSourceRoot) root;
        return branchingSourceRoot.setGrowth(world);
    }

    public BlockPos setRandomGrowth(World world) {

        for (int i = 0; i < 5; i++) {
            BlockPos growthPosition = trySetRandomGrowth(world);
            if (growthPosition != null) {
                return growthPosition;
            }
        }
        return null;
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
                CompoundNBT sourceRootNbt = (CompoundNBT) nbt.get(key);

                boolean doesBranch = sourceRootNbt.getBoolean("does_branch");
                ISourceRoot root;
                if (doesBranch) {
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
