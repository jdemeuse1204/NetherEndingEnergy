package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheUnstableFluid extends AcidFluid {
    public AcidOfTheUnstableFluid() {
        super("unstable", 0xff727272);
    }

    @Override
    protected float getDecayRate() {
        return 1;
    }

    @Override
    protected int getBaseEnergyPerTick() { return 310; }

    @Override
    protected float getStrength() {
        return 2.1f;
    }

    @Override
    protected float getEfficiency() {
        return 1.2f;
    }

    @Override
    protected float getStability() {
        return .5f;
    }

    @Override
    protected float getResponse() {
        return 1.25f;
    }

    @Override
    protected float getSpatial() {
        return .03f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheUnstableFluid.AcidOfTheUnstableBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheUnstableBlock extends AcidFluid.AcidBlock {

        public AcidOfTheUnstableBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}