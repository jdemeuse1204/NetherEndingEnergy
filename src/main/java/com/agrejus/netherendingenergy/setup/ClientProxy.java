package com.agrejus.netherendingenergy.setup;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineScreen;
import com.agrejus.netherendingenergy.blocks.general.botanistscodex.BotanistsCodexScreen;
import com.agrejus.netherendingenergy.blocks.general.generator.FurnaceGeneratorScreen;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationScreen;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationTile;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationTileEntityRenderer;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerScreen;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreScreen;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreTileEntityRenderer;
import com.agrejus.netherendingenergy.blocks.terra.reactor.injector.TerraReactorInjectorTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.injector.TerraReactorInjectorTileEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        ScreenManager.registerFactory(ModBlocks.TERRA_REACTOR_CORE_CONTAINER, TerraReactorCoreScreen::new);
        ScreenManager.registerFactory(ModBlocks.FURNACE_GENERATOR_CONTAINER, FurnaceGeneratorScreen::new);
        ScreenManager.registerFactory(ModBlocks.TERRA_COLLECTING_STATION_CONTAINER, TerraCollectingStationScreen::new);
        ScreenManager.registerFactory(ModBlocks.TERRA_MIXER_CONTAINER, TerraMixerScreen::new);
        ScreenManager.registerFactory(ModBlocks.BOTANISTS_CODEX_CONTAINER, BotanistsCodexScreen::new);

        ScreenManager.registerFactory(ModBlocks.IMBUING_MACHINE_CONTAINER, ImbuingMachineScreen::new);

        ClientRegistry.bindTileEntitySpecialRenderer(TerraCollectingStationTile.class, new TerraCollectingStationTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TerraReactorCoreTile.class, new TerraReactorCoreTileEntityRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TerraReactorInjectorTile.class, new TerraReactorInjectorTileEntityRenderer());
        //ClientRegistry.bindTileEntitySpecialRenderer(TerraMixerTile.class, new TileEntityRendererAnimation<>());
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
