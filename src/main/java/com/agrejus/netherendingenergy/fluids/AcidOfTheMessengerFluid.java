package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheMessengerFluid extends AcidFluid {
    public AcidOfTheMessengerFluid() {
        super("messenger",0xffAA0F01);
    }

    @Override
    protected float getDecayRate() {
        return 1;
    }

    @Override
    protected int getBaseEnergyPerTick() { return 290; }

    @Override
    protected float getStrength() {
        return 2.7f;
    }

    @Override
    protected float getEfficiency() {
        return 3.8f;
    }

    @Override
    protected float getStability() {
        return 2f;
    }

    @Override
    protected float getResponse() {
        return 4f;
    }

    @Override
    protected float getSpatial() {
        return .02333f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheMessengerFluid.AcidOfTheMessengerBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheMessengerBlock extends AcidFluid.AcidBlock {

        public AcidOfTheMessengerBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}