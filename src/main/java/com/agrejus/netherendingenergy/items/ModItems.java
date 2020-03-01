package com.agrejus.netherendingenergy.items;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.fluids.RawAcidFluid;
import net.minecraft.item.BucketItem;
import net.minecraftforge.registries.ObjectHolder;

public class ModItems {

    @ObjectHolder("netherendingenergy:firstitem")
    public  static FirstItem FIRSTITEM;

    @ObjectHolder("netherendingenergy:raw_acid_fluid_bucket")
    public static BucketItem RAW_ACID_FLUID_BUCKET;

    @ObjectHolder("netherendingenergy:acid_of_the_blaze_bucket")
    public static BucketItem ACID_OF_THE_BLAZE_BUCKET;
}
