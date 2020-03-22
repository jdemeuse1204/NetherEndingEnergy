package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheFortunateFluid extends AcidFluid {
    public AcidOfTheFortunateFluid() {
        super("fortunate", 0xff063931);
    }

    @Override
    protected float getDecayRate() {
        return 1;
    }

    @Override
    protected int getBaseEnergyPerTick() { return 333; }

    @Override
    protected float getStrength() {
        return 3f;
    }

    @Override
    protected float getEfficiency() {
        return 3f;
    }

    @Override
    protected float getStability() {
        return 5f;
    }

    @Override
    protected float getResponse() {
        return 2f;
    }

    @Override
    protected float getSpatial() {
        return .03f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheFortunateFluid.AcidOfTheFortunateBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheFortunateBlock extends AcidFluid.AcidBlock {

        public AcidOfTheFortunateBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}