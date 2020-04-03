package com.agrejus.netherendingenergy;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;

public class NetherEndingEnergyBlockStateProperties {
    public static final EnumProperty<RedstoneActivationType> ACTIVATION = EnumProperty.<RedstoneActivationType>create("activation", RedstoneActivationType.class);
    public static final EnumProperty<TerraReactorPartIndex> FORMED = EnumProperty.<TerraReactorPartIndex>create("formed", TerraReactorPartIndex.class);
    public static final IntegerProperty CAUSTIC_0_5 = IntegerProperty.create("caustic", 0, 5);
}
