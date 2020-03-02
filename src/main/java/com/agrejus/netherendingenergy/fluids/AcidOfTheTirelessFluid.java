package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheTirelessFluid extends AcidFluid {
    public AcidOfTheTirelessFluid() {
        super("tireless");
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