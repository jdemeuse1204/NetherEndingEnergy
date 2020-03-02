package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheChorusFluid extends AcidFluid {
    public AcidOfTheChorusFluid() {
        super("chorus");
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheChorusBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheChorusBlock extends AcidFluid.AcidBlock {

        public AcidOfTheChorusBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}