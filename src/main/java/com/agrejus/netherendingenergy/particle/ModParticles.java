package com.agrejus.netherendingenergy.particle;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NetherEndingEnergy.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModParticles {
    public static final BasicParticleType CAUSTIC_CLOUD = new BasicParticleType(false);

    @SubscribeEvent
    public static void registerParticleTypes(RegistryEvent.Register<ParticleType<?>> evt)
    {
        CAUSTIC_CLOUD.setRegistryName("caustic_cloud");
        evt.getRegistry().registerAll(CAUSTIC_CLOUD);
    }

    @SubscribeEvent
    public static void registerParticleFactories(ParticleFactoryRegisterEvent event)
    {
        ParticleManager manager = Minecraft.getInstance().particles;
        manager.registerFactory(ModParticles.CAUSTIC_CLOUD, CausticCloudParticle.Factory::new);
    }
}
