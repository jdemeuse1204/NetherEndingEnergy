package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheElsewhereFluid extends AcidFluid {
    public AcidOfTheElsewhereFluid() {
        super("elsewhere");
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