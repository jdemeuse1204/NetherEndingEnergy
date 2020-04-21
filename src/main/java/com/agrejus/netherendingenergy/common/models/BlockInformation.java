package com.agrejus.netherendingenergy.common.models;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockInformation {

    private final BlockPos pos;
    private final Block block;
    private final BlockState state;

    public BlockInformation(BlockPos pos, Block block, BlockState state) {
        this.pos = pos;
        this.block = block;
        this.state = state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Block getBlock() {
        return block;
    }

    public BlockState getState() {
        return state;
    }

}
