package com.agrejus.netherendingenergy.setup;

import com.agrejus.netherendingenergy.blocks.*;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraVaporCollectorScreen;
import com.agrejus.netherendingenergy.blocks.terra.generator.TerraFurnaceGeneratorScreen;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerScreen;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        ScreenManager.registerFactory(ModBlocks.TERRA_REACTOR_CORE_CONTAINER, TerraReactorCoreScreen::new);
        ScreenManager.registerFactory(ModBlocks.TERRA_FURNACE_GENERATOR_CONTAINER, TerraFurnaceGeneratorScreen::new);
        ScreenManager.registerFactory(ModBlocks.TERRA_VAPOR_COLLECTOR_CONTAINER, TerraVaporCollectorScreen::new);
        ScreenManager.registerFactory(ModBlocks.TERRA_MIXER_CONTAINER, TerraMixerScreen::new);




        ScreenManager.registerFactory(ModBlocks.FIRSTTILE_CONTAINER, FirstTileScreen::new);
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
