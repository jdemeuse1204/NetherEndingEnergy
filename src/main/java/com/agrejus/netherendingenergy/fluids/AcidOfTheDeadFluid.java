package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheDeadFluid extends AcidFluid {
    public AcidOfTheDeadFluid() {
        super("dead");
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