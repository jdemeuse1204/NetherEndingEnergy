package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheTearfulFluid extends AcidFluid {
    public AcidOfTheTearfulFluid() {
        super("tearful", 0xffCFF1F1);
    }

    @Override
    protected float getDecayRate() {
        return 1;
    }

    @Override
    protected int getBaseEnergyPerTick() { return 460; }

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
        return 1f;
    }

    @Override
    protected float getResponse() {
        return 3.5f;
    }

    @Override
    protected float getSpatial() {
        return .0675f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheTearfulFluid.AcidOfTheTearfulBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheTearfulBlock extends AcidFluid.AcidBlock {

        public AcidOfTheTearfulBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}