package com.agrejus.netherendingenergy.blocks;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.abyssal.heatsink.AbyssHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.abyssal.link.AbyssalLinkBlock;
import com.agrejus.netherendingenergy.blocks.abyssal.link.AbyssalLinkTile;
import com.agrejus.netherendingenergy.blocks.creative.energy.CreativeEnergyStoreBlock;
import com.agrejus.netherendingenergy.blocks.creative.energy.CreativeEnergyStoreTile;
import com.agrejus.netherendingenergy.blocks.flowers.roots.CausticBellRootBlock;
import com.agrejus.netherendingenergy.blocks.flowers.vines.CausticBellVineBlock;
import com.agrejus.netherendingenergy.blocks.general.botanistscodex.BotanistsCodexBlock;
import com.agrejus.netherendingenergy.blocks.general.botanistscodex.BotanistsCodexContainer;
import com.agrejus.netherendingenergy.blocks.general.botanistscodex.BotanistsCodexTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.energy.WirelessEnergyTransferModuleBlock;
import com.agrejus.netherendingenergy.blocks.general.wireless.energy.WirelessEnergyTransferModuleTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.fluid.WirelessFluidTransferModuleBlock;
import com.agrejus.netherendingenergy.blocks.general.wireless.fluid.WirelessFluidTransferModuleTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.item.WirelessItemTransferModuleBlock;
import com.agrejus.netherendingenergy.blocks.general.wireless.item.WirelessItemTransferModuleTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.redstone.WirelessRedstoneTransferModuleBlock;
import com.agrejus.netherendingenergy.blocks.general.wireless.redstone.WirelessRedstoneTransferModuleTile;
import com.agrejus.netherendingenergy.blocks.soil.CausticDirtBlock;
import com.agrejus.netherendingenergy.blocks.terra.link.TerraLinkBlock;
import com.agrejus.netherendingenergy.blocks.terra.link.TerraLinkContainer;
import com.agrejus.netherendingenergy.blocks.terra.link.TerraLinkTile;
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
import com.agrejus.netherendingenergy.fluids.AcidOfTheTearfulFluid;
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

    @ObjectHolder("netherendingenergy:" + RegistryNames.WIRELESS_FLUID_TRANSFER_MODULE)
    public static WirelessFluidTransferModuleBlock WIRELESS_FLUID_TRANSFER_MODULE_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.WIRELESS_FLUID_TRANSFER_MODULE)
    public static TileEntityType<WirelessFluidTransferModuleTile> WIRELESS_FLUID_TRANSFER_MODULE_TILE;

    @ObjectHolder("netherendingenergy:" + RegistryNames.WIRELESS_ENERGY_TRANSFER_MODULE)
    public static WirelessEnergyTransferModuleBlock WIRELESS_ENERGY_TRANSFER_MODULE_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.WIRELESS_ENERGY_TRANSFER_MODULE)
    public static TileEntityType<WirelessEnergyTransferModuleTile> WIRELESS_ENERGY_TRANSFER_MODULE_TILE;

    @ObjectHolder("netherendingenergy:" + RegistryNames.WIRELESS_ITEM_TRANSFER_MODULE)
    public static WirelessItemTransferModuleBlock WIRELESS_ITEM_TRANSFER_MODULE_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.WIRELESS_ITEM_TRANSFER_MODULE)
    public static TileEntityType<WirelessItemTransferModuleTile> WIRELESS_ITEM_TRANSFER_MODULE_TILE;

    @ObjectHolder("netherendingenergy:" + RegistryNames.WIRELESS_REDSTONE_TRANSFER_MODULE)
    public static WirelessRedstoneTransferModuleBlock WIRELESS_REDSTONE_TRANSFER_MODULE_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.WIRELESS_REDSTONE_TRANSFER_MODULE)
    public static TileEntityType<WirelessRedstoneTransferModuleTile> WIRELESS_REDSTONE_TRANSFER_MODULE_TILE;

    @ObjectHolder("netherendingenergy:" + RegistryNames.CAUSTIC_DIRT)
    public static CausticDirtBlock CAUSTIC_DIRT_BLOCK;

    @ObjectHolder("netherendingenergy:" + RegistryNames.CAUSTIC_VINES)
    public static CausticBellVineBlock CAUSTIC_VINES_BLOCK;

    @ObjectHolder("netherendingenergy:" + RegistryNames.CAUSTIC_ROOTS)
    public static CausticBellRootBlock CAUSTIC_ROOTS_BLOCK;

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

    /* Botanists Collector */
    @ObjectHolder("netherendingenergy:" + RegistryNames.BOTANISTS_CODEX)
    public static BotanistsCodexBlock BOTANISTS_CODEX_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.BOTANISTS_CODEX)
    public static TileEntityType<BotanistsCodexTile> BOTANISTS_CODEX_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.BOTANISTS_CODEX)
    public static ContainerType<BotanistsCodexContainer> BOTANISTS_CODEX_CONTAINER;

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

    @ObjectHolder("netherendingenergy:" + RegistryNames.ABYSSAL_LINK)
    public static AbyssalLinkBlock ABYSSAL_LINK_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.ABYSSAL_LINK)
    public static TileEntityType<AbyssalLinkTile> ABYSSAL_LINK_TILE;

    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_LINK)
    public static TerraLinkBlock TERRA_LINK_BLOCK;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_LINK)
    public static TileEntityType<TerraLinkTile> TERRA_LINK_TILE;
    @ObjectHolder("netherendingenergy:" + RegistryNames.TERRA_LINK)
    public static ContainerType<TerraLinkContainer> TERRA_LINK_CONTAINER;

    public static class Creative {

        @ObjectHolder("netherendingenergy:" + RegistryNames.Creative.CREATIVE_ENERGY_STORE)
        public static CreativeEnergyStoreBlock CREATIVE_ENERGY_STORE_BLOCK;
        @ObjectHolder("netherendingenergy:" + RegistryNames.Creative.CREATIVE_ENERGY_STORE)
        public static TileEntityType<CreativeEnergyStoreTile> CREATIVE_ENERGY_STORE_TILE;
    }
}
