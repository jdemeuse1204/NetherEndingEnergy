package com.agrejus.netherendingenergy.blocks.flowers.roots;

import com.agrejus.netherendingenergy.common.enumeration.RootType;
import com.agrejus.netherendingenergy.common.interfaces.IRootConfiguration;

public class OffshootRootConfiguration implements IRootConfiguration {
    @Override
    public int getMinLength() {
        return 8;
    }

    @Override
    public int getMaxLength() {
        return 16;
    }

    @Override
    public int getMinOffshootCount() {
        return 1;
    }

    @Override
    public int getMaxOffshootCount() {
        return 2;
    }

    @Override
    public int getMinOffshootGrowthIndex() {
        return 5;
    }

    @Override
    public int getMaxOffshootGrowthIndex() {
        return 16;
    }

    @Override
    public int getMinDeviation() {
        return 4;
    }

    @Override
    public int getMaxDeviation() {
        return 8;
    }

    @Override
    public RootType getChildRootType() {
        return RootType.OFFSHOOT;
    }
}
