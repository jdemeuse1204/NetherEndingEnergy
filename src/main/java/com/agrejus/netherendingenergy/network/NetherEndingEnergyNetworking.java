package com.agrejus.netherendingenergy.network;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.google.common.graph.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.time.chrono.IsoChronology;

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
        INSTANCE.registerMessage(nextID(), PacketShowLocationParticles.class, PacketShowLocationParticles::writeBytes, PacketShowLocationParticles::new, PacketShowLocationParticles::handle);
        INSTANCE.registerMessage(nextID(), PacketChangeActiveStatus.class, PacketChangeActiveStatus::writeBytes, PacketChangeActiveStatus::new, PacketChangeActiveStatus::handle);
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}
