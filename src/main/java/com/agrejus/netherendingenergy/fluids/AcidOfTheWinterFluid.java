package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheWinterFluid extends AcidFluid {
    public AcidOfTheWinterFluid() {
        super("winter");
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