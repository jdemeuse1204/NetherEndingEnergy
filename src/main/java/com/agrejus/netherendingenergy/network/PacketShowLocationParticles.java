package com.agrejus.netherendingenergy.network;

import com.agrejus.netherendingenergy.common.interfaces.ILocationDiscoverable;
import com.agrejus.netherendingenergy.network.base.ClientMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketShowLocationParticles extends ClientMessage {

    private final BlockPos pos;

    public PacketShowLocationParticles(PacketBuffer buf) {
        this.pos = buf.readBlockPos();
    }

    public PacketShowLocationParticles(BlockPos pos) {
        this.pos = pos;
    }


    @Override
    public void writeBytes(PacketBuffer buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public void handleWork(Supplier<NetworkEvent.Context> ctx, World world) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null && tileEntity instanceof ILocationDiscoverable) {

            ILocationDiscoverable entity = (ILocationDiscoverable) tileEntity;
            entity.showLocationParticles();
        }
    }
}
