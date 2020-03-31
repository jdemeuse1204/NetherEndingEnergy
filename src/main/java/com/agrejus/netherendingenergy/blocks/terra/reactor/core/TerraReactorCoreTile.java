package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorConfig;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.injector.TerraReactorInjectorTile;
import com.agrejus.netherendingenergy.common.IntArraySupplierReferenceHolder;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import com.agrejus.netherendingenergy.common.handlers.NonExtractingItemUsageStackHandler;
import com.agrejus.netherendingenergy.common.handlers.ReactorInventoryStackHandler;
import com.agrejus.netherendingenergy.common.helpers.RedstoneHelpers;
import com.agrejus.netherendingenergy.common.models.BlockInformation;
import com.agrejus.netherendingenergy.common.reactor.InjectorPackage;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseType;
import com.agrejus.netherendingenergy.common.attributes.AcidAttributes;
import com.agrejus.netherendingenergy.common.attributes.PotionAttributes;
import com.agrejus.netherendingenergy.common.tank.MixableAcidFluidTank;
import com.agrejus.netherendingenergy.fluids.ModFluids;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TerraReactorCoreTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    // Need a double inventory, one for the item being reacted with for display (1 stack of 1 item) and another for holding the other 63.
    // We can get away with 1 inventory, but with 2 slots,  just set the stack size

    private int tick;
    private int generatedEnergyPerTick;
    private int currentBurnItemTicks;
    private int currentBurnItemTotalTicks;

    private int heatTickRate = 20; // add every 20 ticks,but really every tick, divide amount to add by 20 so its not jumpy
    private float heat;
    private final int maxHeat = 3000;
    private final int fatalHeat = 2000;
    private float heatAbsorptionRate = .9f;

    private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createInventory);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private MixableAcidFluidTank acidTank = new MixableAcidFluidTank(5000) {
        @Override
        protected void onContentsChanged() {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    };

    // Properties
    private IntArraySupplierReferenceHolder referenceHolder;
    private int heatSinkAbsorptionAmount = 112;

    private @Nullable
    BlockPos redstoneInputPortPosition;
    private @Nullable
    BlockPos energyPortPosition;
    private @Nullable
    BlockPos redstoneOutputPortPosition;
    private @Nullable
    BlockState redstoneOutputPortState;
    private static Map<Item, Integer> burnTimes = AbstractFurnaceTileEntity.getBurnTimes();
    private static List<Fluid> allowedFluids;

    public static Map<Item, Integer> getBurnTimes() {
        return burnTimes;
    }

    public static List<Fluid> getAllowedFluids() {

        if (allowedFluids == null) {
            allowedFluids = new ArrayList<>();
            allowedFluids.add(ModFluids.ACID_OF_THE_BLAZE.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_CHORUS.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_DEAD.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_ELSEWHERE.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_FORREST.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_FORTUNATE.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_LIVING.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_MESSENGER.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_MOLTEN.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_NETHER.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_ORDINARY.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_TEARFUL.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_TIRELESS.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_UNSTABLE.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_WINTER.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_WISE.getStillFluid());
            allowedFluids.add(ModFluids.ACID_OF_THE_WITHER.getStillFluid());
        }

        return allowedFluids;
    }

    public TerraReactorCoreTile() {
        super(ModBlocks.TERRA_REACTOR_CORE_TILE);
    }

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(1000000, 0, 20000);
    }

    private IItemHandler createInventory() {
        return new ReactorInventoryStackHandler() {

            @Override
            protected void onContentsChanged(int slot) {
                BlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return burnTimes.containsKey(stack.getItem());
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

                // Burnable Items Only when reactor is formed
                BlockState state = world.getBlockState(pos);
                if (isItemValid(slot, stack) == false || state.get(TerraReactorCoreBlock.FORMED) == TerraReactorPartIndex.UNFORMED) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private void resolveCache() {
        if (this.redstoneInputPortPosition == null) {
            this.redstoneInputPortPosition = TerraReactorReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.REACTOR_REDSTONE_PORT_BLOCK, TerraReactorConfig.INSTANCE);
        }

        if (this.energyPortPosition == null) {
            this.energyPortPosition = TerraReactorReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.TERRA_REACTOR_ENERGY_PORT_BLOCK, TerraReactorConfig.INSTANCE);
        }

        if (this.redstoneOutputPortPosition == null) {
            this.redstoneOutputPortPosition = TerraReactorReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.TERRA_REACTOR_REDSTONE_OUTPUT_PORT_BLOCK, TerraReactorConfig.INSTANCE);
        }

        if (this.redstoneOutputPortState == null && this.redstoneOutputPortPosition != null) {
            this.redstoneOutputPortState = world.getBlockState(this.redstoneOutputPortPosition);
        }
    }

    private boolean isReactorFormed() {
        TerraReactorPartIndex result = world.getBlockState(pos).get(TerraReactorCoreBlock.FORMED);

        if (result == TerraReactorPartIndex.UNFORMED) {
            this.redstoneInputPortPosition = null;
            this.energyPortPosition = null;
            this.redstoneOutputPortPosition = null;
            this.redstoneOutputPortState = null;
            return false;
        }

        return true;
    }

    private List<TerraReactorInjectorTile> getInjectors() {
        return TerraReactorReactorMultiBlock.INSTANCE.getInjectorsFromControllerPosition(world, pos, TerraReactorConfig.INSTANCE);
    }

    private InjectorPackage getInjectorPotions() {
        List<TerraReactorInjectorTile> injectors = getInjectors();
        ArrayList<PotionAttributes> attributes = new ArrayList<>();
        ArrayList<Runnable> deferredUsages = new ArrayList<>();

        int size = injectors.size(); // Small optimization

        for (int i = 0; i < size; i++) {
            TerraReactorInjectorTile injector = injectors.get(i);
            injector.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {
                NonExtractingItemUsageStackHandler handler = (NonExtractingItemUsageStackHandler) w;
                ItemStack stack = handler.getStackInSlot(0);

                if (stack.isEmpty() == false) {

                    Item item = stack.getItem();

                    deferredUsages.add(() -> handler.useOne(0));

                    if (item instanceof PotionItem) {
                        Potion potion = PotionUtils.getPotionFromItem(stack);
                        PotionAttributes potionAttributes = ReactorBaseConfig.INSTANCE.getPotionAttributes(potion);

                        attributes.add(potionAttributes);
                    } else {
                        PotionAttributes itemAttributes = ReactorBaseConfig.INSTANCE.getItemAttributes(item);
                        attributes.add(itemAttributes);
                    }
                }
            });
        }

        return new InjectorPackage(attributes, deferredUsages);
    }

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        // Set generated per tick to 0, we will set this later
        this.generatedEnergyPerTick = 0;

        if (tick <= 0) {
            tick = 20; // Every second
        }

        if (isReactorFormed() == false) {
            return;
        }

        this.resolveCache();

        if (this.isReactorOn() == true) {

            FluidStack fluidStack = acidTank.getFluid();

            // Make sure we have acid
            if (fluidStack.isEmpty() == false && fluidStack.getAmount() >= 1) {

                // get the acid attributes
                AcidAttributes attributes = FluidHelpers.deserializeAttributes(fluidStack.getTag());

                // Get injector augment package with deferred handlers
                InjectorPackage injectorPackage = getInjectorPotions();

                // make sure we have an item to react with
                if (this.currentBurnItemTicks == 0) {

                    // reset total ticks
                    this.currentBurnItemTotalTicks = 0;

                    inventory.ifPresent(w -> {
                        ReactorInventoryStackHandler reactorInventory = (ReactorInventoryStackHandler) w;
                        ItemStack burningStack = reactorInventory.getStackInBurningSlot();

                        // If we have an item that was burning, lets extract it
                        if (burningStack.isEmpty() == false) {
                            // consume the item from the burning inventory because burn time ticks are 0
                            reactorInventory.extractBurningSlot(1, false);
                        }

                        ItemStack backlogStack = reactorInventory.getStackInBacklogSlot();

                        // Do we have items in the backlog
                        if (backlogStack.isEmpty() == false) {

                            int burnTime = getBurnTimes().get(backlogStack.getItem());

                            if (burnTime > 0) {

                                // Consume the item
                                ItemStack extractedStack = reactorInventory.extractBacklogSlot(1, false);
                                reactorInventory.insertBurningSlot(extractedStack, false);
                                List<PotionAttributes> injectedPotions = injectorPackage.getPotionAttributes();
                                float efficiency = TerraReactorEnergyMatrix.getEfficiency(ReactorBaseType.Terra, attributes, injectedPotions);
                                this.currentBurnItemTicks = TerraReactorEnergyMatrix.computeBurnTime(burnTime, efficiency, ReactorBaseType.Terra, attributes, injectedPotions);
                                this.currentBurnItemTotalTicks = this.currentBurnItemTicks;

                                // set block state?
                                markDirty();
                            }
                        }
                    });
                }

                // this will be zero if we don't have any items to react with
                if (this.currentBurnItemTicks > 0) {

                    // can we add energy?
                    energy.ifPresent(x -> {
                        int energyStored = x.getEnergyStored();
                        int energyMaxStored = x.getMaxEnergyStored();
                        int delta = energyMaxStored - energyStored;

                        // Try to consume the acid and make sure it drained properly
                        if (acidTank.drain(1, IFluidHandler.FluidAction.EXECUTE).getAmount() == 1) {

                            // Make sure we can add energy
                            if (delta > 0) {
                                //usages
                                int energyToAdd = TerraReactorEnergyMatrix.getEnergyPerTick(ReactorBaseType.Terra, attributes, injectorPackage.getPotionAttributes());
                                this.addHeat(attributes, injectorPackage);

                                // Use the potions/items
                                List<Runnable> usages = injectorPackage.getDeferredUsages();
                                int usageSize = usages.size();
                                for (int i = 0; i < usageSize; i++) {
                                    usages.get(i).run();
                                }

                                // Decrement the current items burn time
                                --this.currentBurnItemTicks;

                                // Make sure we don't overfill
                                if (energyToAdd > delta) {
                                    energyToAdd = delta;
                                }

                                this.generatedEnergyPerTick = energyToAdd;

                                ((CustomEnergyStorage) x).addEnergy(energyToAdd);
                                int signalStrength = RedstoneHelpers.computeSignalStrength(energyStored, energyMaxStored);
                                boolean hasEnergyStored = x.getEnergyStored() > 0;

                                if (this.redstoneOutputPortState != null && this.redstoneOutputPortPosition != null) {
                                    BlockState currentState = world.getBlockState(this.redstoneOutputPortPosition);

                                    if (currentState.get(BlockStateProperties.POWER_0_15) != signalStrength) {
                                        BlockState newState = this.redstoneOutputPortState.with(BlockStateProperties.POWERED, Boolean.valueOf(hasEnergyStored)).with(BlockStateProperties.POWER_0_15, signalStrength);
                                        world.setBlockState(this.redstoneOutputPortPosition, newState, 3);
                                        world.notifyNeighbors(pos, newState.getBlock());
                                    }
                                }
                            } else {
                                // Still add heat, still reacting
                                // Still on, have active and a burnable item
                                this.addHeat(attributes, injectorPackage);
                            }
                        }
                    });
                } else {
                    // lose heat
                }
            } else {
                // lose heat
            }
        } else {
            // lose heat
        }

        if (this.energyPortPosition != null) {
            this.sendOutPower(this.energyPortPosition);
        }

        --this.tick;
    }

    private void addHeat(AcidAttributes attributes, InjectorPackage injectorPackage) {
        this.heat = TerraReactorEnergyMatrix.computeHeat(this.heat, this.heatAbsorptionRate, this.heatTickRate, ReactorBaseType.Terra, attributes, injectorPackage.getPotionAttributes());
    }

    private boolean isReactorOn() {
        if (this.redstoneInputPortPosition != null) {
            BlockState redstonePortState = world.getBlockState(this.redstoneInputPortPosition);

            // If the block is broken, it wont exist here
            if (redstonePortState == null || redstonePortState.getBlock() != ModBlocks.REACTOR_REDSTONE_PORT_BLOCK) {
                return false;
            }

            return redstonePortState != null && redstonePortState.get(BlockStateProperties.POWERED) == true;

        }
        return false;
    }

    private void sendOutPower(BlockPos energyPortPosition) {
        energy.ifPresent(w -> {

            CustomEnergyStorage energyStore = (CustomEnergyStorage) w;

            if (energyStore.getEnergyStored() > 0) {

                Direction direction = world.getBlockState(energyPortPosition).get(BlockStateProperties.FACING);
                TileEntity tileEntity = world.getTileEntity(energyPortPosition.offset(direction));

                if (tileEntity != null) {
                    tileEntity.getCapability(CapabilityEnergy.ENERGY, direction).ifPresent(x -> {
                        if (x.canReceive() == false) {
                            return;
                        }


                    });
                }
            }
        });
    }

    @Override
    public void read(CompoundNBT tag) {

        this.currentBurnItemTotalTicks = tag.getInt("burntimetotal");
        this.currentBurnItemTicks = tag.getInt("burntime");
        this.heat = tag.getFloat("heat");

        acidTank.readFromNBT(tag.getCompound("acid"));

        CompoundNBT invTag = tag.getCompound("inv");

        inventory.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));

        CompoundNBT energyTag = tag.getCompound("energy");
        // Save energy when block is broken
        energy.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        inventory.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("inv", compound);
        });


        // Write energy when block is placed
        energy.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("energy", compound);
        });

        CompoundNBT acidTankNBT = new CompoundNBT();
        tag.put("acid", acidTank.writeToNBT(acidTankNBT));

        tag.putInt("burntime", this.currentBurnItemTicks);
        tag.putInt("burntimetotal", this.currentBurnItemTotalTicks);
        tag.putFloat("heat", this.heat);

        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        CompoundNBT acidTankNBT = new CompoundNBT();

        acidTank.writeToNBT(acidTankNBT);

        inventory.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("inv", compound);
        });

        energy.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("energy", compound);
        });

        tag.put("acid", acidTankNBT);

        tag.putInt("burntime", this.currentBurnItemTicks);
        tag.putInt("burntimetotal", this.currentBurnItemTotalTicks);
        tag.putFloat("heat", this.heat);

        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        CompoundNBT nbt = packet.getNbtCompound();

        acidTank.readFromNBT(nbt.getCompound("acid"));
        CompoundNBT invTag = nbt.getCompound("inv");
        CompoundNBT energyTag = nbt.getCompound("energy");

        inventory.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));
        energy.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));

        this.currentBurnItemTicks = nbt.getInt("burntime");
        this.currentBurnItemTotalTicks = nbt.getInt("burntimetotal");
        this.heat = nbt.getFloat("heat");
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventory.cast();
        }

        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) acidTank);
        }

        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int worldId, PlayerInventory playerInventory, PlayerEntity playerEntity) {

        List<TerraReactorPartIndex> locations = TerraReactorConfig.INSTANCE.getInjectorLocations();

        // This is all of the data we want to pass to the container which the screen will use!
        this.referenceHolder = new IntArraySupplierReferenceHolder(
                () -> this.acidTank.getCapacity(),
                () -> this.acidTank.getFluidAmount(),
                () -> this.energy.map(w -> w.getEnergyStored()).orElse(0),
                () -> this.energy.map(w -> w.getMaxEnergyStored()).orElse(0),
                () -> this.generatedEnergyPerTick,
                () -> this.currentBurnItemTicks,
                () -> this.currentBurnItemTotalTicks,
                () -> this.getUsagesLeftForInjector(locations.get(0)),
                () -> this.getUsagesLeftForInjector(locations.get(1)),
                () -> this.getUsagesLeftForInjector(locations.get(2)),
                () -> this.getUsagesLeftForInjector(locations.get(3)),
                () -> Math.round(this.heat),
                () -> this.maxHeat);

        return new TerraReactorCoreContainer(worldId, world, pos, playerInventory, playerEntity, this.referenceHolder);
    }

    private int getUsagesLeftForInjector(TerraReactorPartIndex part) {
        BlockInformation information = TerraReactorReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, part);
        TileEntity tileEntity = world.getTileEntity(information.getPos());

        if (tileEntity == null || (tileEntity instanceof TerraReactorInjectorTile) == false) {
            return -1;
        }

        TerraReactorInjectorTile injector = (TerraReactorInjectorTile) tileEntity;

        return injector.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(w -> ((NonExtractingItemUsageStackHandler) w).getUsagesLeftForSlot(0)).orElse(-1);
    }
}