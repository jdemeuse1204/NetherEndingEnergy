package com.agrejus.netherendingenergy.blocks;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.abyssal.heatsink.AbyssHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.soil.CausticFarmlandBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.injector.TerraReactorInjectorBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.injector.TerraReactorInjectorTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.energy.TerraReactorEnergyPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.energy.TerraReactorEnergyPortTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone.TerraReactorRedstoneInputPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone.TerraReactorRedstoneInputPortTile;
import com.agrejus.netherendingenergy.blocks.chaotic.heatsink.ChaoticHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellBlock;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineBlock;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineContainer;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineTile;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationBlock;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationContainer;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationTile;
import com.agrejus.netherendingenergy.blocks.general.generator.FurnaceGeneratorBlock;
import com.agrejus.netherendingenergy.blocks.general.generator.FurnaceGeneratorContainer;
import com.agrejus.netherendingenergy.blocks.general.generator.FurnaceGeneratorTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.heatsink.TerraHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.casing.TerraReactorCasingBlock;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerBlock;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerContainer;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.item.TerraReactorItemPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.item.TerraReactorItemPortTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.liquid.TerraReactorAcidPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.liquid.TerraReactorAcidPortTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone.TerraReactorRedstoneOutputPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone.TerraReactorRedstoneOutputPortTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreContainer;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.stabilizer.TerraReactorItemStabilizerBlock;
import com.agrejus.netherendingenergy.blocks.test.TileTank;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    /* Flowers */
    @ObjectHolder("netherendingenergy:" + RegistryNames.CAUSTIC_BELL)
    public static CausticBellBlock CAUSTIC_BELL_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.CAUSTIC_BELL)
    public static TileEntityType<CausticBellTile> CAUSTIC_BELL_TILE;

    /* General */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_CASING)
    public static TerraReactorCasingBlock TERRA_REACTOR_CASING_BLOCK;

    @ObjectHolder("netherendingenergy:" + RegistryNames.CAUSTIC_FARMLAND)
    public static CausticFarmlandBlock CAUSTIC_FARMLAND_BLOCK;

    /* Heat Sink */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_HEAT_SINK)
    public static TerraHeatSinkBlock TERRA_HEAT_SINK_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.CHAOTIC_HEAT_SINK)
    public static ChaoticHeatSinkBlock CHAOTIC_HEAT_SINK_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.ABYSS_HEAT_SINK)
    public static AbyssHeatSinkBlock ABYSS_HEAT_SINK_BLOCK;

    /* Reactor */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_INJECTOR)
    public static TerraReactorInjectorBlock TERRA_REACTOR_INJECTOR_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_INJECTOR)
    public static TileEntityType<TerraReactorInjectorTile> TERRA_REACTOR_INJECTOR_TILE;

    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_ITEM_STABILIZER)
    public static TerraReactorItemStabilizerBlock TERRA_REACTOR_ITEM_STABILIZER_BLOCK;

    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_ITEM_PORT)
    public static TerraReactorItemPortBlock TERRA_REACTOR_ITEM_PORT_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_ITEM_PORT)
    public static TileEntityType<TerraReactorItemPortTile> TERRA_REACTOR_ITEM_PORT_TILE;

    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_ACID_PORT)
    public static TerraReactorAcidPortBlock TERRA_REACTOR_ACID_PORT_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_ACID_PORT)
    public static TileEntityType<TerraReactorAcidPortTile> TERRA_REACTOR_ACID_PORT_TILE;

    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_REDSTONE_OUTPUT_PORT)
    public static TerraReactorRedstoneOutputPortBlock TERRA_REACTOR_REDSTONE_OUTPUT_PORT_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_REDSTONE_OUTPUT_PORT)
    public static TileEntityType<TerraReactorRedstoneOutputPortTile> TERRA_REACTOR_REDSTONE_OUTPUT_PORT_TILE;

    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_CORE)
    public static TerraReactorCoreBlock TERRA_REACTOR_CORE_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_CORE)
    public static TileEntityType<TerraReactorCoreTile> TERRA_REACTOR_CORE_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_CORE)
    public static ContainerType<TerraReactorCoreContainer> TERRA_REACTOR_CORE_CONTAINER;

    /* Generator */
    @ObjectHolder("netherendingenergy:" + RegistryNames.FURNACE_GENERATOR)
    public static FurnaceGeneratorBlock FURNACE_GENERATOR_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.FURNACE_GENERATOR)
    public static TileEntityType<FurnaceGeneratorTile> FURNACE_GENERATOR_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.FURNACE_GENERATOR)
    public static ContainerType<FurnaceGeneratorContainer> FURNACE_GENERATOR_CONTAINER;

    /* Imbuing Machine */
    @ObjectHolder("netherendingenergy:" + RegistryNames.IMBUING_MACHINE)
    public static ImbuingMachineBlock IMBUING_MACHINE_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.IMBUING_MACHINE)
    public static TileEntityType<ImbuingMachineTile> IMBUING_MACHINE_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.IMBUING_MACHINE)
    public static ContainerType<ImbuingMachineContainer> IMBUING_MACHINE_CONTAINER;

    /* Vapor Collector */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_COLLECTING_STATION)
    public static TerraCollectingStationBlock TERRA_COLLECTING_STATION_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_COLLECTING_STATION)
    public static TileEntityType<TerraCollectingStationTile> TERRA_COLLECTING_STATION_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_COLLECTING_STATION)
    public static ContainerType<TerraCollectingStationContainer> TERRA_COLLECTING_STATION_CONTAINER;

    /* Mixer */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MIXER)
    public static TerraMixerBlock TERRA_MIXER_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MIXER)
    public static TileEntityType<TerraMixerTile> TERRA_MIXER_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MIXER)
    public static ContainerType<TerraMixerContainer> TERRA_MIXER_CONTAINER;

    /* General Reactor */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_REDSTONE_INPUT_PORT)
    public static TerraReactorRedstoneInputPortBlock REACTOR_REDSTONE_PORT_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_REDSTONE_INPUT_PORT)
    public static TileEntityType<TerraReactorRedstoneInputPortTile> REACTOR_REDSTONE_PORT_TILE;

    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_ENERGY_PORT)
    public static TerraReactorEnergyPortBlock TERRA_REACTOR_ENERGY_PORT_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_REACTOR_ENERGY_PORT)
    public static TileEntityType<TerraReactorEnergyPortTile> TERRA_REACTOR_ENERGY_PORT_TILE;

    public static class Test {
        /* Tutorial */
        @ObjectHolder("netherendingenergy:firstblock")
        public static FirstBlock FIRSTBLOCK;

        @ObjectHolder("netherendingenergy:firsttile")
        public static FirstTile FIRSTTILE;

        @ObjectHolder("netherendingenergy:firsttile")
        public static TileEntityType<FirstBlockTile> FIRSTBLOCK_TILE;

        @ObjectHolder("netherendingenergy:firsttile")
        public static ContainerType<FirstTileContainer> FIRSTTILE_CONTAINER;

        @ObjectHolder("netherendingenergy:tank")
        public static TileEntityType<TileTank> TANK_TILE;
    }
}
