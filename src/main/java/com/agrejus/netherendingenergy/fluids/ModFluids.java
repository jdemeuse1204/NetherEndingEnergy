package com.agrejus.netherendingenergy.fluids;

import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.registries.ObjectHolder;

public class ModFluids {

    public static AcidOfTheBlazeFluid AcidOfTheBlaze;
    public static AcidOfTheOrdinaryFluid AcidOfTheOrdinary;

    @ObjectHolder("netherendingenergy:raw_acid_fluid")
    public static FlowingFluid RAW_ACID_FLUID_STILL;
    @ObjectHolder("netherendingenergy:raw_acid_fluid_flowing")
    public static FlowingFluid RAW_ACID_FLUID_FLOWING;

    @ObjectHolder("netherendingenergy:acid_of_the_blaze")
    public static FlowingFluid ACID_OF_THE_BLAZE;
    @ObjectHolder("netherendingenergy:acid_of_the_blaze_flowing")
    public static FlowingFluid ACID_OF_THE_BLAZE_FLOWING;

    @ObjectHolder("netherendingenergy:acid_of_the_ordinary")
    public static FlowingFluid ACID_OF_THE_ORDINARY;
    @ObjectHolder("netherendingenergy:acid_of_the_ordinary_flowing")
    public static FlowingFluid ACID_OF_THE_ORDINARY_FLOWING;
}
