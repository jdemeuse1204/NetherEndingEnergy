package com.agrejus.netherendingenergy;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.Direction;

public class NetherEndingEnergyBlockStateProperties {
    public static final EnumProperty<TerraReactorPartIndex> FORMED = EnumProperty.<TerraReactorPartIndex>create("formed", TerraReactorPartIndex.class);
    public static final IntegerProperty CAUSTIC_0_5 = IntegerProperty.create("caustic", 0, 5);
}
