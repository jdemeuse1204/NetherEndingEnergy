package com.agrejus.netherendingenergy.common.interfaces;

import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;

public interface IRedstoneActivatable {
    RedstoneActivationType getRedstoneActivationType();
    void setRedstoneActivationType(RedstoneActivationType type);
}
