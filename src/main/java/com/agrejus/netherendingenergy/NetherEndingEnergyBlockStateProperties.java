package com.agrejus.netherendingenergy;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import net.minecraft.state.EnumProperty;

public class NetherEndingEnergyBlockStateProperties {
    public static final EnumProperty<TerraReactorPartIndex> FORMED = EnumProperty.<TerraReactorPartIndex>create("formed", TerraReactorPartIndex.class);
}
