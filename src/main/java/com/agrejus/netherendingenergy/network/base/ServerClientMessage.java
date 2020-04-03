package com.agrejus.netherendingenergy.network.base;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import com.agrejus.netherendingenergy.common.interfaces.INetworkMessage;
import com.agrejus.netherendingenergy.common.interfaces.IRedstoneActivatable;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class ServerClientMessage extends ServerMessage {

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld serverWorld = ctx.get().getSender().getServerWorld();
            World clientWorld = NetherEndingEnergy.proxy.getClientWorld();
            handleWork(ctx, serverWorld);
            handleWork(ctx, clientWorld);
        });
        ctx.get().setPacketHandled(true);
    }
}

