package com.agrejus.netherendingenergy.common.multiblock;

import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;

public interface ISizeValidator {

    /**
     * Check if the given size is valid.
     * @param size The size to check.
     * @return Null if the size is valid, otherwise the error message.
     */
    public ITextComponent isSizeValid(Vec3i size);

}
