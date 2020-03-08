package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheForrestFluid extends AcidFluid {
    public AcidOfTheForrestFluid() {
        super("forrest", 0xff063931);
    }

    @Override
    protected int getUses() {
        return 12;
    }

    @Override
    protected float getStrength() {
        return 1.15f;
    }

    @Override
    protected float getEfficiency() {
        return 1.2f;
    }

    @Override
    protected float getStability() {
        return 1f;
    }

    @Override
    protected float getResponse() {
        return 1.3f;
    }

    @Override
    protected float getSpatial() {
        return .0125f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheForrestFluid.AcidOfTheForrestBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheForrestBlock extends AcidFluid.AcidBlock {

        public AcidOfTheForrestBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}