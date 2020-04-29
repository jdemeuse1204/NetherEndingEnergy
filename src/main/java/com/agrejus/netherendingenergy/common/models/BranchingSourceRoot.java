package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.NetherEndingEnergyBlockStateProperties;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import com.agrejus.netherendingenergy.common.interfaces.ISourceRoot;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class BranchingSourceRoot implements ISourceRoot {
    private BlockPos pos;
    private Map<Direction, RootBud> source;
    private int maxSpread;

    public BranchingSourceRoot() {
    }

    public BranchingSourceRoot(BlockPos pos, int maxSpread) {
        this.pos = pos;
        this.source = new HashMap<>();
        this.maxSpread = maxSpread;
    }

    public BlockPos setGrowth(World world) {
        if (this.source == null) {
            return null;
        }

        Collection<RootBud> budCollection = this.source.values();
        RootBud[] buds = new RootBud[budCollection.size()];
        this.source.values().toArray(buds);

        int index = NetherEndingEnergy.roll(0, (buds.length - 1));
        RootBud bud = buds[index];

        if (bud == null) {
            return null;
        }

        IRoot root = bud.getRoot();

        int max = root.size() - 1;
        int randomIndex = NetherEndingEnergy.roll(0, max);
        RootPoint point = root.get(randomIndex);
        BlockState state = world.getBlockState(point.getPosition());
        Block block = state.getBlock();

        // check max of 5 times
        for (int i = 0; i < 5; i++) {
            if (block == ModBlocks.CAUSTIC_ROOTS_BLOCK) {
                int rootStage = state.get(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5);

                if (rootStage == 5) {
                    BlockPos growthPos = point.getPosition().offset(Direction.UP);
                    BlockState growthPositionState = world.getBlockState(growthPos);

                    if(growthPositionState.getBlock() != Blocks.AIR) {

                    }

                    world.setBlockState(growthPos, ModBlocks.TERRA_CAUSTIC_PEARL_GROWTH_BLOCK.getDefaultState(),3);
                    return growthPos;
                }
            }

            randomIndex = NetherEndingEnergy.roll(0, max);
            point = root.get(randomIndex);
            state = world.getBlockState(point.getPosition());
            block = state.getBlock();
        }
        return null;
    }

    public int getMaxSpread() {
        return maxSpread;
    }

    public BlockPos getPos() {
        return pos;
    }

    public RootBud getMainTrunk(Direction direction) {
        return this.source.getOrDefault(direction, null);
    }

    public void setMainTrunk(Direction direction, RootBud bud) {
        this.source.put(direction, bud);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        CompoundNBT positionNbt = new CompoundNBT();
        NBTHelpers.writeToNBT(positionNbt, this.pos);
        tag.put("position", positionNbt);

        tag.putBoolean("does_branch", true);
        tag.putInt("max_spread", this.maxSpread);

        if (this.source != null) {
            AtomicInteger sourceCount = new AtomicInteger(0);
            this.source.forEach((direction, bud) -> {

                tag.putString("direction" + sourceCount.get(), direction.getName());

                if (bud != null) {
                    CompoundNBT budNbt = bud.serializeNBT();
                    tag.put("bud" + sourceCount.get(), budNbt);
                }

                sourceCount.set(sourceCount.get() + 1);
            });
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

        CompoundNBT positionNbt = (CompoundNBT) nbt.get("position");
        this.pos = NBTHelpers.readBlockPosFromNBT(positionNbt);
        this.maxSpread = nbt.getInt("max_spread");
        this.source = new HashMap<>();

        for (int i = 0; i < 4; i++) {
            String directionKey = "direction" + i;
            String budKey = "bud" + i;
            if (nbt.contains(directionKey)) {
                Direction direction = Direction.byName(nbt.getString(directionKey));
                RootBud bud = null;
                if (nbt.contains(budKey)) {
                    bud = new RootBud();
                    CompoundNBT budNbt = (CompoundNBT) nbt.get(budKey);
                    bud.deserializeNBT(budNbt);
                }

                this.source.put(direction, bud);
            }
        }
    }
}
