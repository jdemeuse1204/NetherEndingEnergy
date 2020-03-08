package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheDeadFluid extends AcidFluid {
    public AcidOfTheDeadFluid() {
        super("dead", 0xffB44420);
    }

    @Override
    protected int getUses() {
        return 8;
    }

    @Override
    protected float getStrength() {
        return 1.5f;
    }

    @Override
    protected float getEfficiency() {
        return 1.35f;
    }

    @Override
    protected float getStability() {
        return .9f;
    }

    @Override
    protected float getResponse() {
        return .8f;
    }

    @Override
    protected float getSpatial() {
        return .02f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheDeadFluid.AcidOfTheDeadBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheDeadBlock extends AcidFluid.AcidBlock {

        public AcidOfTheDeadBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}