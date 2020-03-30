package com.agrejus.netherendingenergy.network;

import com.agrejus.netherendingenergy.common.interfaces.INetworkMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class PacketEmptyTank implements INetworkMessage {
    private final BlockPos pos;
    private final Direction capabilitySide;

    public PacketEmptyTank(PacketBuffer buf) {
        this.pos = buf.readBlockPos();
        Integer index = buf.readInt();

        if (index != null) {
            this.capabilitySide = Direction.byIndex(index);
        } else {
            this.capabilitySide = null;
        }
    }

    public PacketEmptyTank(BlockPos pos, @Nullable Direction capabilitySide) {
        this.pos = pos;
        this.capabilitySide = capabilitySide;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(this.pos);
        if (capabilitySide != null) {
            buf.writeInt(capabilitySide.getIndex());
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld world = ctx.get().getSender().getServerWorld();

            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity != null) {
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, this.capabilitySide).ifPresent(w -> {
                    int capacity = w.getTankCapacity(0);
                    w.drain(capacity, IFluidHandler.FluidAction.EXECUTE);
                    tileEntity.markDirty();
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
