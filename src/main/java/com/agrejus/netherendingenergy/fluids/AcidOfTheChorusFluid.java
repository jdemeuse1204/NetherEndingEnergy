package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheChorusFluid extends AcidFluid {
    public AcidOfTheChorusFluid() {
        super("chorus", 0xff8E678D);
    }

    @Override
    protected float getDecayRate() {
        return 1;
    }

    @Override
    protected int getBaseEnergyPerTick() { return 480; }

    @Override
    protected float getStrength() {
        return 5f;
    }

    @Override
    protected float getEfficiency() {
        return 6;
    }

    @Override
    protected float getStability() {
        return 1f;
    }

    @Override
    protected float getResponse() {
        return 5f;
    }

    @Override
    protected float getSpatial() {
        return .075f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheChorusBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheChorusBlock extends AcidFluid.AcidBlock {

        public AcidOfTheChorusBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}