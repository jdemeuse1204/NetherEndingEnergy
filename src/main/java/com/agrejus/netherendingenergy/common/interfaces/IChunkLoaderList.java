package com.agrejus.netherendingenergy.common.interfaces;

import net.minecraft.util.math.BlockPos;

public interface IChunkLoaderList {
    void add(BlockPos pos);
    void remove(BlockPos pos);
    boolean contains(BlockPos pos);
}