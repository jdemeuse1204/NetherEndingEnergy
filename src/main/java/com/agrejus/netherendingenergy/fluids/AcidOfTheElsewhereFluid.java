package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheElsewhereFluid extends AcidFluid {
    public AcidOfTheElsewhereFluid() {
        super("elsewhere", 0xff063931);
    }

    @Override
    protected int getUses() {
        return 15;
    }

    @Override
    protected float getStrength() {
        return 4.5f;
    }

    @Override
    protected float getEfficiency() {
        return 4.5f;
    }

    @Override
    protected float getStability() {
        return .5f;
    }

    @Override
    protected float getResponse() {
        return .8f;
    }

    @Override
    protected float getSpatial() {
        return .05f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheElsewhereFluid.AcidOfTheElsewhereBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheElsewhereBlock extends AcidFluid.AcidBlock {

        public AcidOfTheElsewhereBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}