package com.agrejus.netherendingenergy.blocks.general.wireless.energy;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class WirelessEnergyTransferModuleBlock extends ModuleBlock {

    public WirelessEnergyTransferModuleBlock() {
        super(RegistryNames.WIRELESS_ENERGY_TRANSFER_MODULE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WirelessEnergyTransferModuleTile();
    }
}