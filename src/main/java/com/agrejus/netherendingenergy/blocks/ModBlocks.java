package com.agrejus.netherendingenergy.blocks;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.abyss.heatsink.AbyssHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellBlock;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraVaporCollectorBlock;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraVaporCollectorContainer;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraVaporCollectorTile;
import com.agrejus.netherendingenergy.blocks.terra.generator.TerraFurnaceGeneratorBlock;
import com.agrejus.netherendingenergy.blocks.terra.generator.TerraFurnaceGeneratorContainer;
import com.agrejus.netherendingenergy.blocks.terra.generator.TerraFurnaceGeneratorTile;
import com.agrejus.netherendingenergy.blocks.terra.heatsink.TerraHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.terra.machinecasing.TerraMachineCasingBlock;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerBlock;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerContainer;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreContainer;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreTile;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    /* Tutorial */
    @ObjectHolder("netherendingenergy:firstblock")
    public static FirstBlock FIRSTBLOCK;

    @ObjectHolder("netherendingenergy:firsttile")
    public static FirstTile FIRSTTILE;

    @ObjectHolder("netherendingenergy:firsttile")
    public static TileEntityType<FirstBlockTile> FIRSTBLOCK_TILE;

    @ObjectHolder("netherendingenergy:firsttile")
    public static ContainerType<FirstTileContainer> FIRSTTILE_CONTAINER;

    /* Flowers */
    @ObjectHolder("netherendingenergy:" + RegistryNames.CAUSTIC_BELL)
    public static CausticBellBlock CAUSTIC_BELL_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.CAUSTIC_BELL)
    public static TileEntityType<CausticBellTile> CAUSTIC_BELL_TILE;

    /* General */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MACHINE_CASING)
    public static TerraMachineCasingBlock TERRA_MACHINE_CASING;

    /* Heat Sink */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_HEAT_SINK)
    public static TerraHeatSinkBlock TERRA_HEAT_SINK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.ABYSS_HEAT_SINK)
    public static AbyssHeatSinkBlock ABYSS_HEAT_SINK;

    /* Reactor */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_CORE)
    public static TerraReactorCoreBlock TERRA_REACTOR_CORE_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_CORE)
    public static TileEntityType<TerraReactorCoreTile> TERRA_REACTOR_CORE_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_CORE)
    public static ContainerType<TerraReactorCoreContainer> TERRA_REACTOR_CORE_CONTAINER;

    /* Generator */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_FURNACE_GENERATOR)
    public static TerraFurnaceGeneratorBlock TERRA_FURNACE_GENERATOR_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_FURNACE_GENERATOR)
    public static TileEntityType<TerraFurnaceGeneratorTile> TERRA_FURNACE_GENERATOR_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_FURNACE_GENERATOR)
    public static ContainerType<TerraFurnaceGeneratorContainer> TERRA_FURNACE_GENERATOR_CONTAINER;

    /* Vapor Collector */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_VAPOR_COLLECTOR)
    public static TerraVaporCollectorBlock TERRA_VAPOR_COLLECTOR_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_VAPOR_COLLECTOR)
    public static TileEntityType<TerraVaporCollectorTile> TERRA_VAPOR_COLLECTOR_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_VAPOR_COLLECTOR)
    public static ContainerType<TerraVaporCollectorContainer> TERRA_VAPOR_COLLECTOR_CONTAINER;

    /* Mixer */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MIXER)
    public static TerraMixerBlock TERRA_MIXER_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MIXER)
    public static TileEntityType<TerraMixerTile> TERRA_MIXER_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MIXER)
    public static ContainerType<TerraMixerContainer> TERRA_MIXER_CONTAINER;
}
