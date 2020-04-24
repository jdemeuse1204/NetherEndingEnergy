package com.agrejus.netherendingenergy.common.interfaces;

import com.agrejus.netherendingenergy.common.enumeration.TransferMode;
import net.minecraft.util.math.BlockPos;

public interface ILinkableTile extends IUpdatableTile {
    boolean hasLink(BlockPos pos);
    boolean addLink(BlockPos pos);
    BlockPos[] getLinks();
    boolean removeLink(BlockPos pos);
    void setLinkMode(TransferMode mode);
    TransferMode getLinkMode();
    int clearLinks();
    int maxAllowedLinks();
    int totalLinks();
    BlockPos getPos();
}
