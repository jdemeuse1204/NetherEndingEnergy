package com.agrejus.netherendingenergy.setup;

import com.agrejus.netherendingenergy.blocks.*;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineScreen;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraAcidCollectorScreen;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraAcidCollectorTile;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraAcidCollectorTileEntityRenderer;
import com.agrejus.netherendingenergy.blocks.terra.generator.TerraFurnaceGeneratorScreen;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerScreen;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreScreen;
import com.agrejus.netherendingenergy.blocks.test.TankTESR;
import com.agrejus.netherendingenergy.blocks.test.TileTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        ScreenManager.registerFactory(ModBlocks.TERRA_REACTOR_CORE_CONTAINER, TerraReactorCoreScreen::new);
        ScreenManager.registerFactory(ModBlocks.TERRA_FURNACE_GENERATOR_CONTAINER, TerraFurnaceGeneratorScreen::new);
        ScreenManager.registerFactory(ModBlocks.TERRA_ACID_COLLECTOR_CONTAINER, TerraAcidCollectorScreen::new);
        ScreenManager.registerFactory(ModBlocks.TERRA_MIXER_CONTAINER, TerraMixerScreen::new);

        ScreenManager.registerFactory(ModBlocks.Test.FIRSTTILE_CONTAINER, FirstTileScreen::new);
        ScreenManager.registerFactory(ModBlocks.IMBUING_MACHINE_CONTAINER, ImbuingMachineScreen::new);


        ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new TankTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TerraAcidCollectorTile.class, new TerraAcidCollectorTileEntityRenderer());
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
