package com.agrejus.netherendingenergy;

import com.agrejus.netherendingenergy.blocks.*;
import com.agrejus.netherendingenergy.items.FirstItem;
import com.agrejus.netherendingenergy.setup.ClientProxy;
import com.agrejus.netherendingenergy.setup.IProxy;
import com.agrejus.netherendingenergy.setup.ModSetup;
import com.agrejus.netherendingenergy.setup.ServerProxy;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        // Register the enqueueIMC method for modloading
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        // FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        //MinecraftForge.EVENT_BUS.register(this);

        Config.loadConfig(Config.CLIENT_CONFIG, FMLPaths.CONFIGDIR.get().resolve("netherendingenergy-client.toml"));
        Config.loadConfig(Config.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("netherendingenergy-common.toml"));
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
        proxy.init();
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
            event.getRegistry().register(new CausticBellBlock());

            // Terra
            event.getRegistry().register(new TerraMachineCasingBlock());
            event.getRegistry().register(new TerraReactorCoreBlock());
            event.getRegistry().register(new TerraHeatSinkBlock());

            // Chaotic

            // Abyssal
        }

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            // register a new item here
            Item.Properties properties = new Item.Properties().group(setup.itemGroup);

            // Tutorial
            event.getRegistry().register(new BlockItem(ModBlocks.FIRSTBLOCK, properties).setRegistryName("firstblock"));
            event.getRegistry().register(new BlockItem(ModBlocks.FIRSTTILE, properties).setRegistryName("firsttile"));
            event.getRegistry().register(new FirstItem());

            // New Stuff
            event.getRegistry().register(new BlockItem(ModBlocks.CAUSTIC_BELL_BLOCK, properties).setRegistryName(RegistryNames.CAUSTIC_BELL));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_MACHINE_CASING, properties).setRegistryName(RegistryNames.TERRA_MACHINE_CASING));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_HEAT_SINK, properties).setRegistryName(RegistryNames.TERRA_HEAT_SINK));
            event.getRegistry().register(new BlockItem(ModBlocks.TERRA_REACTOR_CORE_BLOCK, properties).setRegistryName(RegistryNames.TERRA_REACTOR_CORE));
        }

        @SubscribeEvent
        public static void onTileEntityRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().register(TileEntityType.Builder.create(FirstBlockTile::new, ModBlocks.FIRSTTILE).build(null).setRegistryName("firsttile"));

            // New Stuff
            event.getRegistry().register(TileEntityType.Builder.create(CausticBellTile::new, ModBlocks.CAUSTIC_BELL_BLOCK).build(null).setRegistryName(RegistryNames.CAUSTIC_BELL));
            event.getRegistry().register(TileEntityType.Builder.create(TerraReactorCoreTile::new, ModBlocks.TERRA_REACTOR_CORE_BLOCK).build(null).setRegistryName(RegistryNames.TERRA_REACTOR_CORE));
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
        }
    }
}
