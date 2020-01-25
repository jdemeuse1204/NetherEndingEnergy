package com.agrejus.netherendingenergy.blocks;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.superchest.*;
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

    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_HEAT_SINK)
    public static TerraHeatSinkBlock TERRA_HEAT_SINK;

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


    @ObjectHolder("netherendingenergy:superchest")
    public static BlockSuperchest blockSuperchest;
    @ObjectHolder("netherendingenergy:superchest_part")
    public static BlockSuperchestPart blockSuperchestPart;
    @ObjectHolder("netherendingenergy:superchest")
    public static TileEntityType<TileSuperchest> TYPE_SUPERCHEST;
    @ObjectHolder("netherendingenergy:superchest_part")
    public static TileEntityType<TileSuperchestPart> TYPE_SUPERCHEST_PART;
    @ObjectHolder("netherendingenergy:firsttile")
    public static ContainerType<ContainerSuperchest> ContainerSuperChest;

}
