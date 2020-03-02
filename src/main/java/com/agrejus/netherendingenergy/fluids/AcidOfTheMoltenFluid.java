package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheMoltenFluid extends AcidFluid {
    public AcidOfTheMoltenFluid() {
        super("molten");
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheMoltenFluid.AcidOfTheMoltenBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheMoltenBlock extends AcidFluid.AcidBlock {

        public AcidOfTheMoltenBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}