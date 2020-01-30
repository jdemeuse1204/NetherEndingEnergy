package com.agrejus.netherendingenergy.blocks.test;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

public class TileTank extends TileEntity {
    public static final int MAX_CONTENTS = 10000;       // 10 buckets

    public TileTank() {
        super(ModBlocks.Test.TANK_TILE);
    }

    private FluidTank tank = new FluidTank(MAX_CONTENTS) {
        @Override
        protected void onContentsChanged() {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    };

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbtTag = super.getUpdateTag();
        CompoundNBT tankNBT = new CompoundNBT();
        tank.writeToNBT(tankNBT);
        nbtTag.put("tank", tankNBT);
        return nbtTag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        tank.readFromNBT(packet.getNbtCompound().getCompound("tank"));
    }

    @Override
    public void read(CompoundNBT tagCompound) {
        super.read(tagCompound);
        tank.readFromNBT(tagCompound.getCompound("tank"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        CompoundNBT tankNBT = new CompoundNBT();
        tank.writeToNBT(tankNBT);
        compound.put("tank", tankNBT);
        return super.write(compound);
    }

    public FluidTank getTank() {
        return tank;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) tank);
        }
        return super.getCapability(capability);
    }
}
