package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheMessengerFluid extends AcidFluid {
    public AcidOfTheMessengerFluid() {
        super("messenger");
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