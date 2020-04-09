package com.agrejus.netherendingenergy.blocks.general.wireless.fluid;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.tank.PassThroughFluidTank;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WirelessFluidTransferModuleTile extends TileEntity implements ITickableTileEntity {

    // run through a linking machine to link two modules
    private BlockPos linkedBlockPosition;
    private int transferRate = 100;
    private int tick;

    // Internal Storage (pass through)
    private PassThroughFluidTank inputTank = this.createInputTank();

    private PassThroughFluidTank createInputTank() {
        return new PassThroughFluidTank(this.transferRate);
    }

    public WirelessFluidTransferModuleTile() {
        super(ModBlocks.WIRELESS_FLUID_TRANSFER_MODULE_TILE);
    }

    public BlockPos getDestination() {
        BlockState state = world.getBlockState(pos);
        Direction facing = state.get(BlockStateProperties.FACING);
        return pos.offset(facing);
    }

    public void setLinkedBlockPosition(BlockPos pos) {
        this.linkedBlockPosition = pos;
        this.updateBlock();
        markDirty();
    }

    public BlockPos getLinkedBlockPosition() {
        return this.linkedBlockPosition;
    }

    public void updateBlock() {
        BlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 3);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (this.linkedBlockPosition != null && cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            TileEntity linkedTileEntity = world.getTileEntity(this.linkedBlockPosition);

            if (linkedTileEntity != null && linkedTileEntity instanceof WirelessFluidTransferModuleTile) {

                // Make sure destination Tile Entity is linked to its caller.  If broken, it should unlink

                WirelessFluidTransferModuleTile module = (WirelessFluidTransferModuleTile) linkedTileEntity;

                if (this.pos.equals(module.getLinkedBlockPosition()) == false) {
                    return super.getCapability(cap, side);
                }

                TileEntity destination = world.getTileEntity(module.getDestination());

                int distance = BlockHelpers.getThreeDimensionalDistance(pos, module.getDestination());

                if (distance > 12) {
                    return super.getCapability(cap, side);
                }

                if (destination != null) {
                    BlockState destinationModuleBlockState = world.getBlockState(module.getPos());

                    if (destinationModuleBlockState.has(BlockStateProperties.FACING)) {
                        Direction destinationFacing = destinationModuleBlockState.get(BlockStateProperties.FACING);
                        destination.getCapability(cap, destinationFacing.getOpposite()).ifPresent(w -> {
                            if (w instanceof IFluidHandler) {
                                IFluidHandler handler = (IFluidHandler) w;
                                inputTank.setPassThroughTank(handler);
                            }
                        });

                        if (inputTank.hasPassThrough()) {
                            return LazyOptional.of(() -> (T) inputTank);
                        }
                    }
                }
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void read(CompoundNBT tag) {
        readNBT(tag);
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        writeNBT(tag);
        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        writeNBT(tag);
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        CompoundNBT nbt = packet.getNbtCompound();

        readNBT(nbt);
    }

    private void readNBT(CompoundNBT tag) {
        if (tag.contains("linked_position")) {
            CompoundNBT linkedPositionNbt = (CompoundNBT) tag.get("linked_position");
            this.linkedBlockPosition = NBTHelpers.readBlockPosFromNBT(linkedPositionNbt);
        }
    }

    private void writeNBT(CompoundNBT tag) {
        if (this.linkedBlockPosition != null) {
            CompoundNBT linkedPositionNbt = new CompoundNBT();
            NBTHelpers.writeToNBT(linkedPositionNbt, this.linkedBlockPosition);
            tag.put("linked_position", linkedPositionNbt);
        }
    }

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        if (tick > 20) {
            BlockState state = world.getBlockState(pos);
            boolean powered = state.get(BlockStateProperties.POWERED);

            if (powered == true && this.linkedBlockPosition == null) {
                BlockState newState = state.with(BlockStateProperties.POWERED, Boolean.valueOf(false));
                world.setBlockState(pos, newState);
                world.notifyBlockUpdate(pos, state, newState, 3);
            } else if (powered == false && this.linkedBlockPosition != null && isLinked() == true) {
                BlockState newState = state.with(BlockStateProperties.POWERED, Boolean.valueOf(true));
                world.setBlockState(pos, newState);
                world.notifyBlockUpdate(pos, state, newState, 3);
            } else if (powered == true && this.linkedBlockPosition != null && isLinked() == false) {
                BlockState newState = state.with(BlockStateProperties.POWERED, Boolean.valueOf(false));
                world.setBlockState(pos, newState);
                world.notifyBlockUpdate(pos, state, newState, 3);
            }

            this.tick = 0;
        }

        ++tick;
    }

    private boolean isLinked() {
        if (this.linkedBlockPosition == null) {
            return false;
        }

        TileEntity tileEntity = world.getTileEntity(this.linkedBlockPosition);

        if (tileEntity instanceof WirelessFluidTransferModuleTile) {
            WirelessFluidTransferModuleTile wirelessFluidTransferModuleTile = (WirelessFluidTransferModuleTile) tileEntity;

            return pos.equals(wirelessFluidTransferModuleTile.getLinkedBlockPosition());
        }

        return false;
    }
}
