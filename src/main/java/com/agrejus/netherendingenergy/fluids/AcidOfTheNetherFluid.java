package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheNetherFluid extends AcidFluid {
    public AcidOfTheNetherFluid() {
        super("nether");
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