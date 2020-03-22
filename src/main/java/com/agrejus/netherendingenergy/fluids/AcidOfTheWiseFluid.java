package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheWiseFluid extends AcidFluid {
    public AcidOfTheWiseFluid() {
        super("wise", 0xffB9E45A);
    }

    @Override
    protected float getDecayRate() {
        return 1;
    }

    @Override
    protected int getBaseEnergyPerTick() { return 314; }

    @Override
    protected float getStrength() {
        return 3.14f;
    }

    @Override
    protected float getEfficiency() {
        return 3.14f;
    }

    @Override
    protected float getStability() {
        return 3.14f;
    }

    @Override
    protected float getResponse() {
        return 3.14f;
    }

    @Override
    protected float getSpatial() {
        return .0314f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheWiseFluid.AcidOfTheWiseBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheWiseBlock extends AcidFluid.AcidBlock {

        public AcidOfTheWiseBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}