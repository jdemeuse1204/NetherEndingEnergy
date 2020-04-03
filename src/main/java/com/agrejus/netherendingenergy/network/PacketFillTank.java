package com.agrejus.netherendingenergy.network;

import com.agrejus.netherendingenergy.common.interfaces.INetworkMessage;
import com.agrejus.netherendingenergy.network.base.ServerMessage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PacketFillTank extends ServerMessage {
    private final BlockPos pos;
    private final Direction capabilitySide;
    private final FluidStack fluidStack;

    public PacketFillTank(PacketBuffer buf) {
        this.pos = buf.readBlockPos();
        Integer index = buf.readInt();

        if (index != null) {
            this.capabilitySide = Direction.byIndex(index);
        } else {
            this.capabilitySide = null;
        }

        CompoundNBT tag = buf.readCompoundTag();
        this.fluidStack = FluidStack.loadFluidStackFromNBT(tag);
    }

    public PacketFillTank(BlockPos pos, FluidStack fluidStack, @Nullable Direction capabilitySide) {
        this.pos = pos;
        this.capabilitySide = capabilitySide;
        this.fluidStack = fluidStack;
    }

    public void writeBytes(PacketBuffer buf) {
        buf.writeBlockPos(this.pos);
        if (capabilitySide != null) {
            buf.writeInt(capabilitySide.getIndex());
        }
    }

    public void handleWork(Supplier<NetworkEvent.Context> ctx, World world) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, this.capabilitySide).ifPresent(w -> {
                w.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                tileEntity.markDirty();
            });
        }
    }
}
