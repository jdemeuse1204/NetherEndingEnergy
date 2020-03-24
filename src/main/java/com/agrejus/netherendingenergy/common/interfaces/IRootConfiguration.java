package com.agrejus.netherendingenergy.common.interfaces;

import com.agrejus.netherendingenergy.common.enumeration.RootType;

public interface IRootConfiguration {
    int getMinLength();
    int getMaxLength();
    int getMinOffshootCount();
    int getMaxOffshootCount();
    int getMinOffshootGrowthIndex();
    int getMaxOffshootGrowthIndex();
    int getMinDeviation();
    int getMaxDeviation();
    RootType getChildRootType();
}
