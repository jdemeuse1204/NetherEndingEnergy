package com.agrejus.netherendingenergy.blocks.general.wireless.fluid;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleTileBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class WirelessFluidTransferModuleTile extends ModuleTile<IFluidHandler> {

    private int transferRate = 50;

    public WirelessFluidTransferModuleTile() {
        super(ModBlocks.WIRELESS_FLUID_TRANSFER_MODULE_TILE, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY);
    }

    @Override
    protected void onTransfer(TileEntity pullingTileEntity, TileEntity receivingTileEntity, ModuleTileBase linkedModule, IFluidHandler pulling, IFluidHandler receiving) {

        FluidStack sourceTank = pulling.getFluidInTank(0);

        if (sourceTank.getAmount() > 0) {

            int transferAmount = sourceTank.getAmount() > this.transferRate ? this.transferRate : sourceTank.getAmount();

            FluidStack drainedStack = pulling.drain(transferAmount, IFluidHandler.FluidAction.SIMULATE);
            int fillAmount = receiving.fill(drainedStack, IFluidHandler.FluidAction.SIMULATE);

            if (fillAmount > 0) {
                receiving.fill(drainedStack, IFluidHandler.FluidAction.EXECUTE);
                pulling.drain(drainedStack, IFluidHandler.FluidAction.EXECUTE);

                pullingTileEntity.markDirty();
                receivingTileEntity.markDirty();
            }
        }
    }
}
