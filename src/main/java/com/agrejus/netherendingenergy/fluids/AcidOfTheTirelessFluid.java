package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheTirelessFluid extends AcidFluid {
    public AcidOfTheTirelessFluid() {
        super("tireless", 0xffEAEAEA);
    }

    @Override
    protected int getUses() {
        return 7;
    }

    @Override
    protected float getStrength() {
        return 1.1f;
    }

    @Override
    protected float getEfficiency() {
        return 1.4f;
    }

    @Override
    protected float getStability() {
        return .8f;
    }

    @Override
    protected float getResponse() {
        return 2.3f;
    }

    @Override
    protected float getSpatial() {
        return .0195f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheTirelessFluid.AcidOfTheTirelessBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheTirelessBlock extends AcidFluid.AcidBlock {

        public AcidOfTheTirelessBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}