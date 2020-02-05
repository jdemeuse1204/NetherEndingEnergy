package com.agrejus.netherendingenergy.blocks;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.abyssal.heatsink.AbyssHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.base.reactor.redstoneport.ReactorRedstonePortBlock;
import com.agrejus.netherendingenergy.blocks.base.reactor.redstoneport.ReactorRedstonePortTile;
import com.agrejus.netherendingenergy.blocks.chaotic.heatsink.ChaoticHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellBlock;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineBlock;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineContainer;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineTile;
import com.agrejus.netherendingenergy.blocks.soil.CausticImbuedSoil;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraAcidCollectorBlock;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraAcidCollectorContainer;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraAcidCollectorTile;
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
import com.agrejus.netherendingenergy.blocks.test.BlockTank;
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
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MACHINE_CASING)
    public static TerraMachineCasingBlock TERRA_MACHINE_CASING_BLOCK;

    @ObjectHolder("netherendingenergy:" + RegistryNames.CAUSTIC_IMBUED_SOIL)
    public static CausticImbuedSoil CAUSTIC_IMBUED_SOIL_BLOCK;

    /* Heat Sink */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_HEAT_SINK)
    public static TerraHeatSinkBlock TERRA_HEAT_SINK_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.CHAOTIC_HEAT_SINK)
    public static ChaoticHeatSinkBlock CHAOTIC_HEAT_SINK_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.ABYSS_HEAT_SINK)
    public static AbyssHeatSinkBlock ABYSS_HEAT_SINK_BLOCK;

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

    /* Imbuing Machine */
    @ObjectHolder("netherendingenergy:" + RegistryNames.IMBUING_MACHINE)
    public static ImbuingMachineBlock IMBUING_MACHINE_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.IMBUING_MACHINE)
    public static TileEntityType<ImbuingMachineTile> IMBUING_MACHINE_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.IMBUING_MACHINE)
    public static ContainerType<ImbuingMachineContainer> IMBUING_MACHINE_CONTAINER;

    /* Vapor Collector */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_ACID_COLLECTOR)
    public static TerraAcidCollectorBlock TERRA_ACID_COLLECTOR_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_ACID_COLLECTOR)
    public static TileEntityType<TerraAcidCollectorTile> TERRA_ACID_COLLECTOR_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_ACID_COLLECTOR)
    public static ContainerType<TerraAcidCollectorContainer> TERRA_ACID_COLLECTOR_CONTAINER;

    /* Mixer */
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MIXER)
    public static TerraMixerBlock TERRA_MIXER_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MIXER)
    public static TileEntityType<TerraMixerTile> TERRA_MIXER_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_MIXER)
    public static ContainerType<TerraMixerContainer> TERRA_MIXER_CONTAINER;

    /* General Reactor */
    @ObjectHolder("netherendingenergy:" + RegistryNames.REACTOR_REDSTONE_PORT)
    public static ReactorRedstonePortBlock REACTOR_REDSTONE_PORT_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.REACTOR_REDSTONE_PORT)
    public static TileEntityType<ReactorRedstonePortTile> REACTOR_REDSTONE_PORT_TILE;

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
        public static BlockTank TANK_BLOCK;
        @ObjectHolder("netherendingenergy:tank")
        public static TileEntityType<TileTank> TANK_TILE;
    }
}
