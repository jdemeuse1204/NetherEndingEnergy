package com.agrejus.netherendingenergy.common.interfaces;

import net.minecraft.util.math.BlockPos;

public interface ILinkable {
    void setLink(BlockPos pos);
    void removeLink(BlockPos pos);
    void clearLinks();
}
