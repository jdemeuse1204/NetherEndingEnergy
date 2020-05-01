package com.agrejus.netherendingenergy;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.abyssal.heatsink.AbyssHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.abyssal.link.AbyssalLinkBlock;
import com.agrejus.netherendingenergy.blocks.abyssal.link.AbyssalLinkTile;
import com.agrejus.netherendingenergy.blocks.chaotic.heatsink.ChaoticHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.creative.energy.CreativeEnergyStoreBlock;
import com.agrejus.netherendingenergy.blocks.creative.energy.CreativeEnergyStoreTile;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellBlock;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import com.agrejus.netherendingenergy.blocks.flowers.pearls.TerraCausticPearlGrowthBlock;
import com.agrejus.netherendingenergy.blocks.flowers.roots.CausticBellRootBlock;
import com.agrejus.netherendingenergy.blocks.flowers.vines.CausticBellVineBlock;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineBlock;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineContainer;
import com.agrejus.netherendingenergy.blocks.general.ImbuingMachineTile;
import com.agrejus.netherendingenergy.blocks.general.botanistscodex.BotanistsCodexBlock;
import com.agrejus.netherendingenergy.blocks.general.botanistscodex.BotanistsCodexContainer;
import com.agrejus.netherendingenergy.blocks.general.botanistscodex.BotanistsCodexTile;
import com.agrejus.netherendingenergy.blocks.general.generator.FurnaceGeneratorBlock;
import com.agrejus.netherendingenergy.blocks.general.generator.FurnaceGeneratorContainer;
import com.agrejus.netherendingenergy.blocks.general.generator.FurnaceGeneratorTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.energy.WirelessEnergyTransferModuleBlock;
import com.agrejus.netherendingenergy.blocks.general.wireless.energy.WirelessEnergyTransferModuleTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.fluid.WirelessFluidTransferModuleBlock;
import com.agrejus.netherendingenergy.blocks.general.wireless.fluid.WirelessFluidTransferModuleTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.item.WirelessItemTransferModuleBlock;
import com.agrejus.netherendingenergy.blocks.general.wireless.item.WirelessItemTransferModuleTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.redstone.WirelessRedstoneTransferModuleBlock;
import com.agrejus.netherendingenergy.blocks.general.wireless.redstone.WirelessRedstoneTransferModuleTile;
import com.agrejus.netherendingenergy.blocks.soil.CausticDirtBlock;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationBlock;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationContainer;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationTile;
import com.agrejus.netherendingenergy.blocks.terra.link.TerraLinkBlock;
import com.agrejus.netherendingenergy.blocks.terra.link.TerraLinkContainer;
import com.agrejus.netherendingenergy.blocks.terra.link.TerraLinkTile;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerBlock;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerContainer;
import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.casing.TerraReactorCasingBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreContainer;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.heatsink.TerraHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.injector.TerraReactorInjectorBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.injector.TerraReactorInjectorTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.energy.TerraReactorEnergyPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.energy.TerraReactorEnergyPortTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.item.TerraReactorItemPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.item.TerraReactorItemPortTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.liquid.TerraReactorAcidPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.liquid.TerraReactorAcidPortTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone.TerraReactorRedstoneInputPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone.TerraReactorRedstoneInputPortTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone.TerraReactorRedstoneOutputPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone.TerraReactorRedstoneOutputPortTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.stabilizer.TerraReactorItemStabilizerBlock;
import com.agrejus.netherendingenergy.common.chunkloading.ChunkLoaderList;
import com.agrejus.netherendingenergy.common.interfaces.IChunkLoaderList;
import com.agrejus.netherendingenergy.fluids.*;
import com.agrejus.netherendingenergy.items.CausticMashItem;
import com.agrejus.netherendingenergy.items.TerraCausticPearlItem;
import com.agrejus.netherendingenergy.items.wireless.LinkingRemoteItem;
import com.agrejus.netherendingenergy.items.wireless.WirelessBotanistsCodexItem;
import com.agrejus.netherendingenergy.network.NetherEndingEnergyNetworking;
import com.agrejus.netherendingenergy.setup.ClientProxy;
import com.agrejus.netherendingenergy.setup.IProxy;
import com.agrejus.netherendingenergy.setup.ModSetup;
import com.agrejus.netherendingenergy.setup.ServerProxy;
import com.agrejus.netherendingenergy.tools.CapabilityVapor;
import com.agrejus.netherendingenergy.worldgen.feature.CausticBellFeature;
import com.agrejus.netherendingenergy.worldgen.feature.config.CausticBellConfig;
import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.Set;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("netherendingenergy")
public class NetherEndingEnergy {

    public static final String MODID = "netherendingenergy";
    public static final String LOADERID = "loader";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static ModSetup setup = new ModSetup();
    public static Random random = new Random();

    private static final Logger LOGGER = LogManager.getLogger();

    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Fluid> FLUIDS = new DeferredRegister<>(ForgeRegistries.FLUIDS, MODID);
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
    public static final Feature<CausticBellConfig> CAUSTIC_BELLS = new CausticBellFeature(CausticBellConfig::deserialize);

    @CapabilityInject(IChunkLoaderList.class)
    public static Capability<IChunkLoaderList> CAPABILITY = null;

    static {
        ModFluids.RawAcidFluid = new RawAcidFluid();
        ModFluids.AcidOfTheOrdinary = new AcidOfTheOrdinaryFluid();
        ModFluids.AcidOfTheBlaze = new AcidOfTheBlazeFluid();
        ModFluids.AcidOfTheChorus = new AcidOfTheChorusFluid();
        ModFluids.AcidOfTheDead = new AcidOfTheDeadFluid();
        ModFluids.AcidOfTheElsewhere = new AcidOfTheElsewhereFluid();
        ModFluids.AcidOfTheWise = new AcidOfTheWiseFluid();
        ModFluids.AcidOfTheWither = new AcidOfTheWitherFluid();
        ModFluids.AcidOfTheForrest = new AcidOfTheForrestFluid();
        ModFluids.AcidOfTheFortunate = new AcidOfTheFortunateFluid();
        ModFluids.AcidOfTheLiving = new AcidOfTheLivingFluid();
        ModFluids.AcidOfTheMessenger = new AcidOfTheMessengerFluid();
        ModFluids.AcidOfTheMolten = new AcidOfTheMoltenFluid();
        ModFluids.AcidOfTheNether = new AcidOfTheNetherFluid();
        ModFluids.AcidOfTheTearful = new AcidOfTheTearfulFluid();
        ModFluids.AcidOfTheTireless = new AcidOfTheTirelessFluid();
        ModFluids.AcidOfTheUnstable = new AcidOfTheUnstableFluid();
        ModFluids.AcidOfTheWinter = new AcidOfTheWinterFluid();
    }

    public static boolean rollBoolean() {
        return roll(random, 1, 1000) % 2 == 0;
    }

    public static int roll(int min, int max) {
        return roll(random, min, max);
    }

    public static int roll(Random rnd, int min, int max) {
        return rnd.nextInt(max - min + 1) + min;
    }

    @SubscribeEvent
    public void attachWorldCaps(AttachCapabilitiesEvent<World> event) {
        if (event.getObject().isRemote) return;
        final LazyOptional<IChunkLoaderList> inst = LazyOptional.of(() -> new ChunkLoaderList((ServerWorld)event.getObject()));
        final ICapabilitySerializable<INBT> provider = new ICapabilitySerializable<INBT>() {
            @Override
            public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
                return CAPABILITY.orEmpty(cap, inst);
            }

            @Override
            public INBT serializeNBT() {
                return CAPABILITY.writeNBT(inst.orElse(null), null);
            }

            @Override
            public void deserializeNBT(INBT nbt) {
                CAPABILITY.readNBT(inst.orElse(null), null, nbt);
            }
        };
        event.addCapability(new ResourceLocation(NetherEndingEnergy.MODID, NetherEndingEnergy.LOADERID), provider);
        event.addListener(() -> inst.invalidate());
    }

    public NetherEndingEnergy() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        // Register the setup method for modloading
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::setup);

        FLUIDS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("netherendingenergy-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("netherendingenergy-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
        proxy.init();
        NetherEndingEnergyNetworking.registerMessages();
        CapabilityVapor.register();
        CapabilityManager.INSTANCE.register(IChunkLoaderList.class, new ChunkLoaderList.Storage(), () -> new ChunkLoaderList(null));
        RegistryEvents.addWorldgen();
    }

/*    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }*/

/*
    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
*/

/*
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
*/

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

            /* CREATIVE */
            event.getRegistry().register(new CreativeEnergyStoreBlock());



            /* General */
            event.getRegistry().register(new CausticBellRootBlock());
            event.getRegistry().register(new CausticBellVineBlock());
            event.getRegistry().register(new TerraCausticPearlGrowthBlock());
            event.getRegistry().register(new CausticBellBlock());
            event.getRegistry().register(new CausticDirtBlock());
            event.getRegistry().register(new ImbuingMachineBlock());
            event.getRegistry().register(new BotanistsCodexBlock());
            event.getRegistry().register(new WirelessFluidTransferModuleBlock());
            event.getRegistry().register(new WirelessEnergyTransferModuleBlock());
            event.getRegistry().register(new WirelessItemTransferModuleBlock());
            event.getRegistry().register(new WirelessRedstoneTransferModuleBlock());

            // Terra
            event.getRegistry().register(new TerraLinkBlock());

            event.getRegistry().register(new TerraReactorCasingBlock());
            event.getRegistry().register(new TerraReactorCoreBlock());
            event.getRegistry().register(new TerraHeatSinkBlock());
            event.getRegistry().register(new TerraCollectingStationBlock());
            event.getRegistry().register(new TerraMixerBlock());
            event.getRegistry().register(new TerraReactorItemStabilizerBlock());


            // Chaotic
            event.getRegistry().register(new ChaoticHeatSinkBlock());

            // Abyssal
            event.getRegistry().register(new AbyssHeatSinkBlock());
            event.getRegistry().register(new AbyssalLinkBlock());

            /* Reactor General */
            event.getRegistry().register(new TerraReactorRedstoneInputPortBlock());
            event.getRegistry().register(new TerraReactorEnergyPortBlock());
            event.getRegistry().register(new TerraReactorItemPortBlock());
            event.getRegistry().register(new TerraReactorAcidPortBlock());
            event.getRegistry().register(new TerraReactorRedstoneOutputPortBlock());
            event.getRegistry().register(new TerraReactorInjectorBlock());

            event.getRegistry().register(new FurnaceGeneratorBlock());
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            // register a new item here
            Item.Properties properties = new Item.Properties().group(setup.itemGroup);

            // CREATIVE
            event.getRegistry().register(new BlockItem(ModBlocks.Creative.CREATIVE_ENERGY_STORE_BLOCK, properties).setRegistryName(RegistryNames.Creative.CREATIVE_ENERGY_STORE));



            event.getRegistry().register(new BlockItem(ModBlocks.CAUSTIC_ROOTS_BLOCK, properties).setRegistryName(RegistryNames.CAUSTIC_ROOTS));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_CAUSTIC_PEARL_GROWTH_BLOCK, properties).setRegistryName(RegistryNames.TERRA_CAUSTIC_PEARL_GROWTH));

            // Register Items
            event.getRegistry().register(new TerraCausticPearlItem());
            event.getRegistry().register(new CausticMashItem());
            event.getRegistry().register(new LinkingRemoteItem());
            event.getRegistry().register(new WirelessBotanistsCodexItem());

            // New Stuff
            event.getRegistry().register(new BlockItem(ModBlocks.CAUSTIC_VINES_BLOCK, properties).setRegistryName(RegistryNames.CAUSTIC_VINES));

            event.getRegistry().register(new BlockItem(ModBlocks.CAUSTIC_DIRT_BLOCK, properties).setRegistryName(RegistryNames.CAUSTIC_DIRT));

            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_LINK_BLOCK, properties).setRegistryName(RegistryNames.TERRA_LINK));

            event.getRegistry().register(new BlockItem(ModBlocks.WIRELESS_FLUID_TRANSFER_MODULE_BLOCK, properties).setRegistryName(RegistryNames.WIRELESS_FLUID_TRANSFER_MODULE));
            event.getRegistry().register(new BlockItem(ModBlocks.WIRELESS_ENERGY_TRANSFER_MODULE_BLOCK, properties).setRegistryName(RegistryNames.WIRELESS_ENERGY_TRANSFER_MODULE));
            event.getRegistry().register(new BlockItem(ModBlocks.WIRELESS_ITEM_TRANSFER_MODULE_BLOCK, properties).setRegistryName(RegistryNames.WIRELESS_ITEM_TRANSFER_MODULE));
            event.getRegistry().register(new BlockItem(ModBlocks.WIRELESS_REDSTONE_TRANSFER_MODULE_BLOCK, properties).setRegistryName(RegistryNames.WIRELESS_REDSTONE_TRANSFER_MODULE));

            event.getRegistry().register(new BlockItem(ModBlocks.IMBUING_MACHINE_BLOCK, properties).setRegistryName(RegistryNames.IMBUING_MACHINE));

            event.getRegistry().register(new BlockItem(ModBlocks.CAUSTIC_BELL_BLOCK, properties).setRegistryName(RegistryNames.CAUSTIC_BELL));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_REACTOR_CASING_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_CASING));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_HEAT_SINK_BLOCK, properties).setRegistryName(RegistryNames.TERRA_HEAT_SINK));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_REACTOR_CORE_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_CORE));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_COLLECTING_STATION_BLOCK, properties).setRegistryName(RegistryNames.TERRA_COLLECTING_STATION));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_MIXER_BLOCK, properties).setRegistryName(RegistryNames.TERRA_MIXER));
            event.getRegistry().register(new BlockItem(ModBlocks.BOTANISTS_CODEX_BLOCK, properties).setRegistryName(RegistryNames.BOTANISTS_CODEX));

            /* Abyss */
            event.getRegistry().register(new BlockItem(ModBlocks.ABYSS_HEAT_SINK_BLOCK, properties).setRegistryName(RegistryNames.ABYSS_HEAT_SINK));
            event.getRegistry().register(new BlockItem(ModBlocks.ABYSSAL_LINK_BLOCK, properties).setRegistryName(RegistryNames.ABYSSAL_LINK));

            /* Chaotic */
            event.getRegistry().register(new BlockItem(ModBlocks.CHAOTIC_HEAT_SINK_BLOCK, properties).setRegistryName(RegistryNames.CHAOTIC_HEAT_SINK));

            /* Reactor General */
            event.getRegistry().register(new BlockItem(ModBlocks.REACTOR_REDSTONE_PORT_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_REDSTONE_INPUT_PORT));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_REACTOR_ENERGY_PORT_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_ENERGY_PORT));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_REACTOR_ITEM_PORT_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_ITEM_PORT));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_REACTOR_ACID_PORT_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_ACID_PORT));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_REACTOR_REDSTONE_OUTPUT_PORT_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_REDSTONE_OUTPUT_PORT));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_REACTOR_INJECTOR_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_INJECTOR));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_REACTOR_ITEM_STABILIZER_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_ITEM_STABILIZER));

            // Items
            event.getRegistry().register(new BlockItem(ModBlocks.FURNACE_GENERATOR_BLOCK, properties).setRegistryName(RegistryNames.FURNACE_GENERATOR));
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {

            /* CREATIVE */
            event.getRegistry().register(TileEntityType.Builder.create(CreativeEnergyStoreTile::new, ModBlocks.Creative.CREATIVE_ENERGY_STORE_BLOCK).build(null).setRegistryName(RegistryNames.Creative.CREATIVE_ENERGY_STORE));

            /* ABYSSAL */
            event.getRegistry().register(TileEntityType.Builder.create(AbyssalLinkTile::new, ModBlocks.ABYSSAL_LINK_BLOCK).build(null).setRegistryName(RegistryNames.ABYSSAL_LINK));

            /* TERRA */
            event.getRegistry().register(TileEntityType.Builder.create(TerraLinkTile::new, ModBlocks.TERRA_LINK_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_LINK));

            // New Stuff
            event.getRegistry().register(TileEntityType.Builder.create(ImbuingMachineTile::new, ModBlocks.IMBUING_MACHINE_BLOCK).build(null).setRegistryName(RegistryNames.IMBUING_MACHINE));

            event.getRegistry().register(TileEntityType.Builder.create(WirelessFluidTransferModuleTile::new, ModBlocks.WIRELESS_FLUID_TRANSFER_MODULE_BLOCK).build(null).setRegistryName(RegistryNames.WIRELESS_FLUID_TRANSFER_MODULE));
            event.getRegistry().register(TileEntityType.Builder.create(WirelessEnergyTransferModuleTile::new, ModBlocks.WIRELESS_ENERGY_TRANSFER_MODULE_BLOCK).build(null).setRegistryName(RegistryNames.WIRELESS_ENERGY_TRANSFER_MODULE));
            event.getRegistry().register(TileEntityType.Builder.create(WirelessItemTransferModuleTile::new, ModBlocks.WIRELESS_ITEM_TRANSFER_MODULE_BLOCK).build(null).setRegistryName(RegistryNames.WIRELESS_ITEM_TRANSFER_MODULE));
            event.getRegistry().register(TileEntityType.Builder.create(WirelessRedstoneTransferModuleTile::new, ModBlocks.WIRELESS_REDSTONE_TRANSFER_MODULE_BLOCK).build(null).setRegistryName(RegistryNames.WIRELESS_REDSTONE_TRANSFER_MODULE));

            event.getRegistry().register(TileEntityType.Builder.create(CausticBellTile::new, ModBlocks.CAUSTIC_BELL_BLOCK).build(null).setRegistryName(RegistryNames.CAUSTIC_BELL));
            event.getRegistry().register(TileEntityType.Builder.create(TerraReactorCoreTile::new, ModBlocks.TERRA_REACTOR_CORE_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_REACTOR_CORE));
            event.getRegistry().register(TileEntityType.Builder.create(TerraCollectingStationTile::new, ModBlocks.TERRA_COLLECTING_STATION_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_COLLECTING_STATION));
            event.getRegistry().register(TileEntityType.Builder.create(TerraMixerTile::new, ModBlocks.TERRA_MIXER_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_MIXER));
            event.getRegistry().register(TileEntityType.Builder.create(BotanistsCodexTile::new, ModBlocks.BOTANISTS_CODEX_BLOCK).build(null).setRegistryName(RegistryNames.BOTANISTS_CODEX));

            event.getRegistry().register(TileEntityType.Builder.create(TerraReactorRedstoneInputPortTile::new, ModBlocks.REACTOR_REDSTONE_PORT_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_REACTOR_REDSTONE_INPUT_PORT));
            event.getRegistry().register(TileEntityType.Builder.create(TerraReactorEnergyPortTile::new, ModBlocks.TERRA_REACTOR_ENERGY_PORT_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_REACTOR_ENERGY_PORT));
            event.getRegistry().register(TileEntityType.Builder.create(TerraReactorItemPortTile::new, ModBlocks.TERRA_REACTOR_ITEM_PORT_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_REACTOR_ITEM_PORT));
            event.getRegistry().register(TileEntityType.Builder.create(TerraReactorAcidPortTile::new, ModBlocks.TERRA_REACTOR_ACID_PORT_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_REACTOR_ACID_PORT));
            event.getRegistry().register(TileEntityType.Builder.create(TerraReactorRedstoneOutputPortTile::new, ModBlocks.TERRA_REACTOR_REDSTONE_OUTPUT_PORT_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_REACTOR_REDSTONE_OUTPUT_PORT));
            event.getRegistry().register(TileEntityType.Builder.create(TerraReactorInjectorTile::new, ModBlocks.TERRA_REACTOR_INJECTOR_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_REACTOR_INJECTOR));

            event.getRegistry().register(TileEntityType.Builder.create(FurnaceGeneratorTile::new, ModBlocks.FURNACE_GENERATOR_BLOCK).build(null).setRegistryName(RegistryNames.FURNACE_GENERATOR));
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new TerraReactorCoreContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.TERRA_REACTOR_CORE));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new FurnaceGeneratorContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.FURNACE_GENERATOR));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new TerraCollectingStationContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.TERRA_COLLECTING_STATION));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new TerraMixerContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.TERRA_MIXER));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new ImbuingMachineContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.IMBUING_MACHINE));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new TerraLinkContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.TERRA_LINK));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new BotanistsCodexContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.BOTANISTS_CODEX));
        }

        public static final Set<BiomeDictionary.Type> TYPE_BLACKLIST = ImmutableSet.of(
                BiomeDictionary.Type.DEAD,
                BiomeDictionary.Type.NETHER,
                BiomeDictionary.Type.END,
                BiomeDictionary.Type.SNOWY,
                BiomeDictionary.Type.WASTELAND,
                BiomeDictionary.Type.VOID
        );

        public static void addWorldgen() {
            for(Biome biome : ForgeRegistries.BIOMES) {
                if (BiomeDictionary.getTypes(biome).stream().noneMatch(TYPE_BLACKLIST::contains)) {
                    biome.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, biome.createDecoratedFeature(CAUSTIC_BELLS, new CausticBellConfig(), Placement.TOP_SOLID_HEIGHTMAP, IPlacementConfig.NO_PLACEMENT_CONFIG));
                }
            }
        }
    }
}
