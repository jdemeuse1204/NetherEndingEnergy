package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheWinterFluid extends AcidFluid {
    public AcidOfTheWinterFluid() {
        super("winter", 0xff86AEFD);
    }

    @Override
    protected int getUses() {
        return 15;
    }

    @Override
    protected float getStrength() {
        return 1.7f;
    }

    @Override
    protected float getEfficiency() {
        return 1.3f;
    }

    @Override
    protected float getStability() {
        return .95f;
    }

    @Override
    protected float getResponse() {
        return 2f;
    }

    @Override
    protected float getSpatial() {
        return .0175f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheWinterBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheWinterBlock extends AcidFluid.AcidBlock {

        public AcidOfTheWinterBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}