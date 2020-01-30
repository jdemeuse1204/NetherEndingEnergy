package com.agrejus.netherendingenergy.setup;

import com.agrejus.netherendingenergy.blocks.*;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraVaporCollectorScreen;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraVaporCollectorTile;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraVaporCollectorTileEntityRenderer;
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
        ScreenManager.registerFactory(ModBlocks.TERRA_VAPOR_COLLECTOR_CONTAINER, TerraVaporCollectorScreen::new);
        ScreenManager.registerFactory(ModBlocks.TERRA_MIXER_CONTAINER, TerraMixerScreen::new);

        ScreenManager.registerFactory(ModBlocks.Test.FIRSTTILE_CONTAINER, FirstTileScreen::new);


        ClientRegistry.bindTileEntitySpecialRenderer(TileTank.class, new TankTESR());
        ClientRegistry.bindTileEntitySpecialRenderer(TerraVaporCollectorTile.class, new TerraVaporCollectorTileEntityRenderer());
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
