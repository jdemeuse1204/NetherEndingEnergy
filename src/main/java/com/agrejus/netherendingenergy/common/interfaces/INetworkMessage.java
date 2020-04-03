package com.agrejus.netherendingenergy.common.interfaces;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface INetworkMessage {
    void writeBytes(PacketBuffer buf);
    void handle(Supplier<NetworkEvent.Context> ctx);
}
