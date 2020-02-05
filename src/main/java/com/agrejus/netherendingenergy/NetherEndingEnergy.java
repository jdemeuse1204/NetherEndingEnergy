package com.agrejus.netherendingenergy;

import com.agrejus.netherendingenergy.blocks.*;
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
import com.agrejus.netherendingenergy.items.FirstItem;
import com.agrejus.netherendingenergy.setup.ClientProxy;
import com.agrejus.netherendingenergy.setup.IProxy;
import com.agrejus.netherendingenergy.setup.ModSetup;
import com.agrejus.netherendingenergy.setup.ServerProxy;
import com.agrejus.netherendingenergy.tools.CapabilityVapor;
import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("netherendingenergy")
public class NetherEndingEnergy {

    public static final String MODID = "netherendingenergy";

    public static IProxy proxy = DistExecutor.runForDist(() -> () -> new ClientProxy(), () -> () -> new ServerProxy());

    public static ModSetup setup = new ModSetup();

    private static final Logger LOGGER = LogManager.getLogger();

    public NetherEndingEnergy() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_CONFIG);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("netherendingenergy-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("netherendingenergy-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
        proxy.init();
        CapabilityVapor.register();
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

            // register a new block here
            event.getRegistry().register(new FirstBlock());
            event.getRegistry().register(new FirstTile());
            event.getRegistry().register(new BlockTank());




            /* General */
            event.getRegistry().register(new CausticBellBlock());
            event.getRegistry().register(new CausticImbuedSoil());
            event.getRegistry().register(new ImbuingMachineBlock());

            // Terra
            event.getRegistry().register(new TerraMachineCasingBlock());
            event.getRegistry().register(new TerraReactorCoreBlock());
            event.getRegistry().register(new TerraHeatSinkBlock());
            event.getRegistry().register(new TerraFurnaceGeneratorBlock());
            event.getRegistry().register(new TerraAcidCollectorBlock());
            event.getRegistry().register(new TerraMixerBlock());


            // Chaotic
            event.getRegistry().register(new ChaoticHeatSinkBlock());

            // Abyssal
            event.getRegistry().register(new AbyssHeatSinkBlock());

            /* Reactor General */
            event.getRegistry().register(new ReactorRedstonePortBlock());
        }

        @SubscribeEvent
        public static void onFluidsRegistry(final RegistryEvent.Register<Fluid> event) {
            //event.getRegistry().register(new BlockItem(ModBlocks.Test.FIRSTBLOCK, properties).setRegistryName("firstblock"));

/*            ForgeFlowingFluid.Properties prop = new ForgeFlowingFluid.Properties(() -> ModFluids.MOLTEN_COPPER, () -> ModFluids.FLOWING_MOLTEN_COPPER,
                    FluidAttributes.builder(ModFluids.FLUID_MOLTEN_COPPER_STILL, ModFluids.FLUID_MOLTEN_COPPER_FLOWING))
                    .bucket(() -> ModFluids.MOLTEN_COPPER_BUCKET).block(() -> ModFluids.MOLTEN_COPPER_BLOCK);
            ForgeFlowingFluid fluid = new ForgeFlowingFluid.Source(prop).setRegistryName("raw_acid");
            */
/*            event.getRegistry().register(new FluidItem).content(Fluid.class, flowingFluid);
            event.content(Fluid.class, sourceFluid);
            event.content(Block.class, blockFluid);
            if (bucketFluid != null) registry.content(Item.class, bucketFluid);*/


/*            public static final List<Fluid> FLUIDS = new ArrayList<>();

            public static final ResourceLocation FLUID_MOLTEN_COPPER_STILL = new ResourceLocation(TutorialMod.MODID, "block/molten_copper_still");
            public static final ResourceLocation FLUID_MOLTEN_COPPER_FLOWING = new ResourceLocation(TutorialMod.MODID, "block/molten_copper_flow");



            public static final FlowingFluid MOLTEN_COPPER = (FlowingFluid) addAndGet(new ForgeFlowingFluid.Source(prop).setRegistryName("molten_copper"), FLUIDS);
            public static final FlowingFluid FLOWING_MOLTEN_COPPER = (FlowingFluid) addAndGet(new ForgeFlowingFluid.Flowing(prop).setRegistryName("molten_copper_flowing"), FLUIDS);

            public static final FlowingFluidBlock MOLTEN_COPPER_BLOCK = (FlowingFluidBlock) addAndGet(new FlowingFluidBlock(() -> MOLTEN_COPPER, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()).setRegistryName("molten_copper_block"), ModBlocks.BLOCKS);
            public static final Item MOLTEN_COPPER_BUCKET = addAndGet(new BucketItem(() -> MOLTEN_COPPER, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(TutorialMod.groups.itemGroup)).setRegistryName("molten_copper_bucket"), ModItems.ITEMS);

            private static <T> T addAndGet(T obj, List<T> list) {
                list.add(obj);
                return obj;
            }

            public static void init() {}*/
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            // register a new item here
            Item.Properties properties = new Item.Properties().group(setup.itemGroup);

            // Tutorial
            event.getRegistry().register(new BlockItem(ModBlocks.Test.FIRSTBLOCK, properties).setRegistryName("firstblock"));
            event.getRegistry().register(new BlockItem(ModBlocks.Test.FIRSTTILE, properties).setRegistryName("firsttile"));
            event.getRegistry().register(new BlockItem(ModBlocks.Test.TANK_BLOCK, properties).setRegistryName("tank"));
            event.getRegistry().register(new FirstItem());

            // New Stuff
            event.getRegistry().register(new BlockItem(ModBlocks.CAUSTIC_IMBUED_SOIL_BLOCK, properties).setRegistryName(RegistryNames.CAUSTIC_IMBUED_SOIL));
            event.getRegistry().register(new BlockItem(ModBlocks.IMBUING_MACHINE_BLOCK, properties).setRegistryName(RegistryNames.IMBUING_MACHINE));

            event.getRegistry().register(new BlockItem(ModBlocks.CAUSTIC_BELL_BLOCK, properties).setRegistryName(RegistryNames.CAUSTIC_BELL));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_MACHINE_CASING_BLOCK, properties).setRegistryName(RegistryNames.TERRA_MACHINE_CASING));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_HEAT_SINK_BLOCK, properties).setRegistryName(RegistryNames.TERRA_HEAT_SINK));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_REACTOR_CORE_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_CORE));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_FURNACE_GENERATOR_BLOCK, properties).setRegistryName(RegistryNames.TERRA_FURNACE_GENERATOR));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_ACID_COLLECTOR_BLOCK, properties).setRegistryName(RegistryNames.TERRA_ACID_COLLECTOR));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_MIXER_BLOCK, properties).setRegistryName(RegistryNames.TERRA_MIXER));

            /* Abyss */
            event.getRegistry().register(new BlockItem(ModBlocks.ABYSS_HEAT_SINK_BLOCK, properties).setRegistryName(RegistryNames.ABYSS_HEAT_SINK));

            /* Chaotic */
            event.getRegistry().register(new BlockItem(ModBlocks.CHAOTIC_HEAT_SINK_BLOCK, properties).setRegistryName(RegistryNames.CHAOTIC_HEAT_SINK));

            /* Reactor General */
            event.getRegistry().register(new BlockItem(ModBlocks.REACTOR_REDSTONE_PORT_BLOCK, properties).setRegistryName(RegistryNames.REACTOR_REDSTONE_PORT));

            // Items

        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(TileEntityType.Builder.create(FirstBlockTile::new, ModBlocks.Test.FIRSTTILE).build(null).setRegistryName("firsttile"));
            event.getRegistry().register(TileEntityType.Builder.create(TileTank::new, ModBlocks.Test.TANK_BLOCK).build(null).setRegistryName("tank"));

            // New Stuff
            event.getRegistry().register(TileEntityType.Builder.create(ImbuingMachineTile::new, ModBlocks.IMBUING_MACHINE_BLOCK).build(null).setRegistryName(RegistryNames.IMBUING_MACHINE));

            event.getRegistry().register(TileEntityType.Builder.create(CausticBellTile::new, ModBlocks.CAUSTIC_BELL_BLOCK).build(null).setRegistryName(RegistryNames.CAUSTIC_BELL));
            event.getRegistry().register(TileEntityType.Builder.create(TerraReactorCoreTile::new, ModBlocks.TERRA_REACTOR_CORE_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_REACTOR_CORE));
            event.getRegistry().register(TileEntityType.Builder.create(TerraFurnaceGeneratorTile::new, ModBlocks.TERRA_FURNACE_GENERATOR_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_FURNACE_GENERATOR));
            event.getRegistry().register(TileEntityType.Builder.create(TerraAcidCollectorTile::new, ModBlocks.TERRA_ACID_COLLECTOR_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_ACID_COLLECTOR));
            event.getRegistry().register(TileEntityType.Builder.create(TerraMixerTile::new, ModBlocks.TERRA_MIXER_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_MIXER));

            event.getRegistry().register(TileEntityType.Builder.create(ReactorRedstonePortTile::new, ModBlocks.REACTOR_REDSTONE_PORT_BLOCK).build(null).setRegistryName(RegistryNames.REACTOR_REDSTONE_PORT));
        }

        @SubscribeEvent
        public static void onContainerRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
            // this is client side registry
            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new FirstTileContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName("firsttile"));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new TerraReactorCoreContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.TERRA_REACTOR_CORE));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new TerraFurnaceGeneratorContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.TERRA_FURNACE_GENERATOR));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new TerraAcidCollectorContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.TERRA_ACID_COLLECTOR));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new TerraMixerContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.TERRA_MIXER));

            event.getRegistry().register(IForgeContainerType.create((windowId, playerInventory, data) -> {
                BlockPos pos = data.readBlockPos();
                return new ImbuingMachineContainer(windowId, proxy.getClientWorld(), pos, playerInventory, proxy.getClientPlayer());
            }).setRegistryName(RegistryNames.IMBUING_MACHINE));
        }
    }
}
