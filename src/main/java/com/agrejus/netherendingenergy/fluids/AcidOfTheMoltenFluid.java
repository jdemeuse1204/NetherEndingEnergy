package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheMoltenFluid extends AcidFluid {
    public AcidOfTheMoltenFluid() {
        super("molten", 0xffD4550E);
    }

    @Override
    protected int getUses() {
        return 5;
    }

    @Override
    protected float getStrength() {
        return 2f;
    }

    @Override
    protected float getEfficiency() {
        return 2.5f;
    }

    @Override
    protected float getStability() {
        return .75f;
    }

    @Override
    protected float getResponse() {
        return 1.6f;
    }

    @Override
    protected float getSpatial() {
        return .0275f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheMoltenFluid.AcidOfTheMoltenBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheMoltenBlock extends AcidFluid.AcidBlock {

        public AcidOfTheMoltenBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}