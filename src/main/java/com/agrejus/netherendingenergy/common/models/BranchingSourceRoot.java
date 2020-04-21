package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.ISourceRoot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
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
                    CompoundNBT budNbt = (CompoundNBT)nbt.get(budKey);
                    bud.deserializeNBT(budNbt);
                }

                this.source.put(direction, bud);
            }
        }
    }
}
