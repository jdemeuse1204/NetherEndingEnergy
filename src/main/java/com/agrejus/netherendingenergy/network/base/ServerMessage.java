package com.agrejus.netherendingenergy.network.base;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.interfaces.INetworkMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;


public abstract class ServerMessage implements INetworkMessage {


    public abstract void writeBytes(PacketBuffer buf);

    public abstract void handleWork(Supplier<NetworkEvent.Context> ctx, World world);

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerWorld serverWorld = ctx.get().getSender().getServerWorld();
            handleWork(ctx, serverWorld);
        });
        ctx.get().setPacketHandled(true);
    }
}

