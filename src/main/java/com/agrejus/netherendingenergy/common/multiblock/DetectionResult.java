package com.agrejus.netherendingenergy.common.multiblock;

import lombok.Data;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

@Data
public class DetectionResult {

    private final Vec3i size;
    private final ITextComponent error;

    public DetectionResult(Vec3i size) {
        this.size = size;
        this.error = null;
    }

    public DetectionResult(ITextComponent error) {
        this.size = LocationHelpers.copyLocation(Vec3i.NULL_VECTOR);
        this.error = error;
    }

    public DetectionResult(String error) {
        this(new StringTextComponent(error));
    }

}
