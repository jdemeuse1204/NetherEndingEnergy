package com.agrejus.netherendingenergy;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.enumeration.CausticCloudEffect;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;

public class NetherEndingEnergyBlockStateProperties {
    public static final EnumProperty<RedstoneActivationType> ACTIVATION = EnumProperty.<RedstoneActivationType>create("activation", RedstoneActivationType.class);
    public static final EnumProperty<TerraReactorPartIndex> FORMED = EnumProperty.<TerraReactorPartIndex>create("formed", TerraReactorPartIndex.class);
    public static final IntegerProperty CAUSTIC_0_5 = IntegerProperty.create("caustic", 0, 5);
    public static final IntegerProperty CAUSTIC_CLOUD_1_3 = IntegerProperty.create("caustic_cloud", 1, 3);
    public static final IntegerProperty PEARL_GROWTH_0_6 = IntegerProperty.create("pearl_growth", 0, 6);
    public static final EnumProperty<CausticCloudEffect> CAUSTIC_CLOUD_EFFECT = EnumProperty.<CausticCloudEffect>create("caustic_cloud_effect", CausticCloudEffect.class);
}
