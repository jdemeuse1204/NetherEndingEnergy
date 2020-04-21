package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheNetherFluid extends AcidFluid {
    public AcidOfTheNetherFluid() {
        super("nether", 0xffD4550E);
    }

    @Override
    protected float getDecayRate() {
        return 1;
    }

    @Override
    protected int getBaseEnergyPerTick() { return 350; }

    @Override
    protected float getStrength() {
        return 3.4f;
    }

    @Override
    protected float getEfficiency() {
        return 3.8f;
    }

    @Override
    protected float getStability() {
        return .9f;
    }

    @Override
    protected float getResponse() {
        return 4.5f;
    }

    @Override
    protected float getSpatial() {
        return .025f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheNetherFluid.AcidOfTheNetherBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheNetherBlock extends AcidFluid.AcidBlock {

        public AcidOfTheNetherBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}