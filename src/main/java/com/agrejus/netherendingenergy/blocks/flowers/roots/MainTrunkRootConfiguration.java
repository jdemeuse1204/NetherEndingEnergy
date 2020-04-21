package com.agrejus.netherendingenergy.blocks.flowers.roots;

import com.agrejus.netherendingenergy.common.enumeration.RootType;
import com.agrejus.netherendingenergy.common.interfaces.IRootConfiguration;

public class MainTrunkRootConfiguration implements IRootConfiguration {
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
        return 3;
    }

    @Override
    public int getMaxOffshootGrowthIndex() {
        return this.getMaxLength();
    }

    @Override
    public int getMinDeviation() {
        return 0;
    }

    @Override
    public int getMaxDeviation() {
        return 1;
    }

    @Override
    public RootType getChildRootType() {
        return RootType.OFFSHOOT;
    }


}
