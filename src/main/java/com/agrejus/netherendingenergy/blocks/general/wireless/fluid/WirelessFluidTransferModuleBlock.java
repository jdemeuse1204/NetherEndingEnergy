package com.agrejus.netherendingenergy.blocks.general.wireless.fluid;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class WirelessFluidTransferModuleBlock extends ModuleBlock {

    public WirelessFluidTransferModuleBlock() {
        super(RegistryNames.WIRELESS_FLUID_TRANSFER_MODULE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WirelessFluidTransferModuleTile();
    }
}
