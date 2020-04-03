package com.agrejus.netherendingenergy.network;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetherEndingEnergyNetworking {
    public static SimpleChannel INSTANCE;
    private static int ID = 0;

    public static int nextID() {
        return ++ID;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(NetherEndingEnergy.MODID, NetherEndingEnergy.MODID), () -> "1.0", s -> true, s -> true);

        INSTANCE.registerMessage(nextID(), PacketEmptyTank.class, PacketEmptyTank::writeBytes, PacketEmptyTank::new, PacketEmptyTank::handle);
        INSTANCE.registerMessage(nextID(), PacketFillTank.class, PacketFillTank::writeBytes, PacketFillTank::new, PacketFillTank::handle);
        INSTANCE.registerMessage(nextID(), PacketChangeRedstoneActivationType.class, PacketChangeRedstoneActivationType::writeBytes, PacketChangeRedstoneActivationType::new, PacketChangeRedstoneActivationType::handle);
    }
}
