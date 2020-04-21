package com.agrejus.netherendingenergy.blocks.general.wireless.energy;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleTileBase;
import com.agrejus.netherendingenergy.common.enumeration.TransferMode;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class WirelessEnergyTransferModuleTile extends ModuleTile<IEnergyStorage> {

    private int transferRate = 200;

    public WirelessEnergyTransferModuleTile() {
        super(ModBlocks.WIRELESS_ENERGY_TRANSFER_MODULE_TILE, CapabilityEnergy.ENERGY);
    }

    @Override
    protected void onTransfer(TileEntity pullingTileEntity, TileEntity receivingTileEntity, ModuleTileBase linkedModule, IEnergyStorage pulling, IEnergyStorage receiving) {

        int sourceEnergyStored = pulling.getEnergyStored();

        if(sourceEnergyStored > 0) {
            int transferAmount = sourceEnergyStored > this.transferRate ? this.transferRate : sourceEnergyStored;

            int drainedAmount = pulling.extractEnergy(transferAmount, true);
            int receivedAmount = receiving.receiveEnergy(drainedAmount, true);

            if (receivedAmount > 0) {
                pulling.extractEnergy(receivedAmount, false);
                receiving.receiveEnergy(receivedAmount, false);

                pullingTileEntity.markDirty();
                receivingTileEntity.markDirty();
            }
        }
    }
}
