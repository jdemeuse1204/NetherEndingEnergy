package com.agrejus.netherendingenergy.blocks.terra.link;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.base.tile.NEETileEntity;
import com.agrejus.netherendingenergy.common.enumeration.TransferMode;
import com.agrejus.netherendingenergy.common.interfaces.ILinkable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TerraLinkTile extends NEETileEntity implements ILinkable {

    private TransferMode mode;
    private ArrayList<BlockPos> links = new ArrayList<>();
    private FluidTank tank = new FluidTank(Integer.MAX_VALUE) {

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {

            if (maxDrain == 0) {
                return FluidStack.EMPTY;
            }

            AtomicInteger amountLeft = new AtomicInteger(maxDrain);
            int size = links.size();
            int split = maxDrain / size;
            int splitDrainAmount = Math.min(split, 1);

            for (int i = 0; i < size; i++) {

                if (amountLeft.get() == 0) {
                    break;
                }

                BlockPos link = links.get(i);

                if (link == null) {
                    continue;
                }

                TileEntity tileEntity = world.getTileEntity(link);

                if (tileEntity == null || (tileEntity instanceof TerraLinkTile) == false) {
                    continue;
                }

                TerraLinkTile destinationTile = (TerraLinkTile) tileEntity;
                TransferMode destinationMode = destinationTile.getMode();

                if (destinationMode == TransferMode.NONE || destinationMode == TransferMode.RECEIVE) {
                    continue;
                }

                destinationTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(destination -> {
                    FluidStack drained = destination.drain(splitDrainAmount, action);
                    amountLeft.addAndGet(-drained.getAmount());
                });
            }

            FluidStack result = FluidStack.EMPTY;
            result.setAmount(maxDrain - amountLeft.get());
            return result;
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {

            int amountToFill = resource.getAmount();
            AtomicInteger amountLeft = new AtomicInteger(amountToFill);
            if (amountToFill == 0) {
                return 0;
            }

            // Round Robin
            int size = links.size();
            int split = amountToFill / size;
            int splitFillAmount = Math.min(split, 1);

            for (int i = 0; i < size; i++) {

                if (amountLeft.get() == 0) {
                    break;
                }

                BlockPos link = links.get(i);

                if (link == null) {
                    continue;
                }

                TileEntity tileEntity = world.getTileEntity(link);

                if (tileEntity == null || (tileEntity instanceof TerraLinkTile) == false) {
                    continue;
                }

                TerraLinkTile destinationTile = (TerraLinkTile) tileEntity;
                TransferMode destinationMode = destinationTile.getMode();

                if (destinationMode == TransferMode.NONE || destinationMode == TransferMode.SEND) {
                    continue;
                }

                destinationTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(destination -> {
                    FluidStack fillResource = resource.copy();
                    fillResource.setAmount(splitFillAmount);
                    int filledAmount = destination.fill(fillResource, action);
                    amountLeft.addAndGet(-filledAmount);
                });
            }

            return amountToFill - amountLeft.get();
        }
    };

    public TransferMode getMode() {
        return mode;
    }

    public TerraLinkTile() {
        super(ModBlocks.TERRA_LINK_TILE);
    }

    @Override
    protected void clientTick() {

    }

    @Override
    protected void serverTick() {

    }

    @Override
    protected void readNBT(CompoundNBT tag) {
        super.readNBT(tag);
    }

    @Override
    protected void writeNBT(CompoundNBT tag) {
        super.writeNBT(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) tank);
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void setLink(BlockPos pos) {
        
    }

    @Override
    public void removeLink(BlockPos pos) {

    }

    @Override
    public void clearLinks() {

    }
}
