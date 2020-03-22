package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheLivingFluid extends AcidFluid {
    public AcidOfTheLivingFluid() {
        super("living", 0xffEFBCAC);
    }

    @Override
    protected float getDecayRate() {
        return 1;
    }

    @Override
    protected int getBaseEnergyPerTick() { return 300; }

    @Override
    protected float getStrength() {
        return 1.3f;
    }

    @Override
    protected float getEfficiency() {
        return 1.25f;
    }

    @Override
    protected float getStability() {
        return 1f;
    }

    @Override
    protected float getResponse() {
        return 1.4f;
    }

    @Override
    protected float getSpatial() {
        return .015f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheLivingFluid.AcidOfTheLivingBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheLivingBlock extends AcidFluid.AcidBlock {

        public AcidOfTheLivingBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}