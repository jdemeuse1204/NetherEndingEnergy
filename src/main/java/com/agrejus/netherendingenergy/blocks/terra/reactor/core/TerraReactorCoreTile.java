package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorConfig;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.injector.TerraReactorInjectorTile;
import com.agrejus.netherendingenergy.common.IntArraySupplierReferenceHolder;
import com.agrejus.netherendingenergy.common.attributes.AcidAttributes;
import com.agrejus.netherendingenergy.common.attributes.PotionAttributes;
import com.agrejus.netherendingenergy.common.blocks.RedstoneEnergyTile;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import com.agrejus.netherendingenergy.common.handlers.NonExtractingItemUsageStackHandler;
import com.agrejus.netherendingenergy.common.handlers.ReactorInventoryStackHandler;
import com.agrejus.netherendingenergy.common.helpers.RedstoneHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IProcessingUnit;
import com.agrejus.netherendingenergy.common.models.BlockInformation;
import com.agrejus.netherendingenergy.common.models.InvertedProcessingUnit;
import com.agrejus.netherendingenergy.common.models.ProcessingUnit;
import com.agrejus.netherendingenergy.common.reactor.Injector;
import com.agrejus.netherendingenergy.common.reactor.InjectorPackage;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseType;
import com.agrejus.netherendingenergy.common.tank.MixableAcidFluidTank;
import com.agrejus.netherendingenergy.fluids.ModFluids;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.AbstractFurnaceTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TerraReactorCoreTile extends RedstoneEnergyTile implements INamedContainerProvider {

    private IProcessingUnit dissolutionProcessingUnit = new InvertedProcessingUnit(0);
    private IProcessingUnit fatalHeatProcessingUnit = new ProcessingUnit(100);

    private int heatTickRate = 20;
    private float heat = 0;
    private final int maxHeat = 3000;
    private final int fatalHeat = 2000;
    private float heatAbsorptionRate = .9f;

    private RedstoneActivationType redstoneActivationType;
    private ReactorInventoryStackHandler inventory = this.createInventory();

    @Override
    protected CustomEnergyStorage createEnergyStore() {
        return this.createEnergy();
    }

    private MixableAcidFluidTank acidTank = new MixableAcidFluidTank(5000) {
        @Override
        protected void onContentsChanged() {
            update();
            markDirty();
        }
    };

    // Properties
    private IntArraySupplierReferenceHolder referenceHolder;
    private int heatSinkAbsorptionAmount = 112;

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

    private CustomEnergyStorage createEnergy() {
        return new CustomEnergyStorage(1000000, 0, 20000);
    }

    private ReactorInventoryStackHandler createInventory() {
        return new ReactorInventoryStackHandler() {

            @Override
            protected void onContentsChanged(int slot) {
                update();
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                Item item = stack.getItem();
               if (item instanceof BlockItem) {
                   BlockItem blockItem = (BlockItem)item;
                   Block block = blockItem.getBlock();
                   Material material = block.getDefaultState().getMaterial();
                   return material != null;
               }

               return false;
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

    private BlockPos getRedstoneOutputPortPosition() {
        return TerraReactorReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.TERRA_REACTOR_REDSTONE_OUTPUT_PORT_BLOCK, TerraReactorConfig.INSTANCE);
    }

    private BlockPos getEnergyPortPosition() {
        return TerraReactorReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.TERRA_REACTOR_ENERGY_PORT_BLOCK, TerraReactorConfig.INSTANCE);
    }

    private BlockPos getRedstoneInputPortPosition() {
        return TerraReactorReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.REACTOR_REDSTONE_PORT_BLOCK, TerraReactorConfig.INSTANCE);
    }

    private boolean isReactorFormed() {
        TerraReactorPartIndex result = world.getBlockState(pos).get(TerraReactorCoreBlock.FORMED);

        if (result == TerraReactorPartIndex.UNFORMED) {
            return false;
        }

        return true;
    }

    public List<Injector> getInjectors() {
        return TerraReactorReactorMultiBlock.INSTANCE.getInjectorsFromControllerPosition(world, pos, TerraReactorConfig.INSTANCE);
    }

    private InjectorPackage getInjectorPotions() {
        List<Injector> injectors = getInjectors();
        ArrayList<PotionAttributes> attributes = new ArrayList<>();
        ArrayList<Runnable> deferredUsages = new ArrayList<>();

        int size = injectors.size(); // Small optimization

        for (int i = 0; i < size; i++) {
            Injector injector = injectors.get(i);

            if (injector.isActive() == false) {
                continue;
            }

            injector.getTileEntity().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {
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

    private void processFatalHeat() {
        if (this.heat > this.fatalHeat) {
            this.fatalHeatProcessingUnit.setNext();

            if (this.fatalHeatProcessingUnit.canProcess() == true) {
                // need to make our own explosion, MC's is trash
                world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 50, Explosion.Mode.DESTROY);
                this.fatalHeatProcessingUnit.reset();
            }
        } else {
            this.fatalHeatProcessingUnit.reset();
        }
    }

    @Override
    protected void serverTick() {

        if (isReactorFormed() == false) {
            return;
        }

        this.processFatalHeat();

        if (this.isEnabled() == true) {

            FluidStack fluidStack = acidTank.getFluid();

            // Make sure we have acid
            if (fluidStack.isEmpty() == false && fluidStack.getAmount() >= 1) {

                // get the acid attributes
                AcidAttributes attributes = FluidHelpers.deserializeAttributes(fluidStack.getTag());

                // Get injector augment package with deferred handlers
                InjectorPackage injectorPackage = getInjectorPotions();

                // make sure we have an item to react with
                if (this.dissolutionProcessingUnit.getValue() == 0) {

                    // reset total ticks
                    this.dissolutionProcessingUnit.setTotal(0);

                    ItemStack burningStack = inventory.getStackInBurningSlot();

                    // If we have an item that was burning, lets extract it
                    if (burningStack.isEmpty() == false) {
                        // consume the item from the burning inventory because burn time ticks are 0
                        inventory.extractBurningSlot(1, false);
                    }

                    ItemStack backlogStack = inventory.getStackInBacklogSlot();

                    // Do we have items in the backlog
                    if (backlogStack.isEmpty() == false) {

                        int burnTime = getBurnTimes().get(backlogStack.getItem());

                        if (burnTime > 0) {

                            // Consume the item
                            ItemStack extractedStack = inventory.extractBacklogSlot(1, false);
                            inventory.insertBurningSlot(extractedStack, false);
                            List<PotionAttributes> injectedPotions = injectorPackage.getPotionAttributes();
                            float efficiency = TerraReactorEnergyMatrix.getEfficiency(ReactorBaseType.Terra, attributes, injectedPotions);

                            int dissolutionTicks = TerraReactorEnergyMatrix.computeBurnTime(burnTime, efficiency, ReactorBaseType.Terra, attributes, injectedPotions);
                            this.dissolutionProcessingUnit.setTotal(dissolutionTicks);
                            this.dissolutionProcessingUnit.setCurrent(dissolutionTicks);

                            markDirty();
                        }
                    }
                }

                // this will be zero if we don't have any items to react with
                if (this.dissolutionProcessingUnit.getValue() > 0) {

                    // can we add energy?
                    int energyStored = this.energyStore.getEnergyStored();
                    int energyMaxStored = this.energyStore.getMaxEnergyStored();
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
                            this.dissolutionProcessingUnit.setNext();

                            // Make sure we don't overfill
                            if (energyToAdd > delta) {
                                energyToAdd = delta;
                            }

                            this.energyStore.addEnergy(energyToAdd);
                            this.updateRedstoneOutputLevel(energyStored, energyMaxStored, this.energyStore);

                        } else {
                            // Still add heat, still reacting
                            // Still on, have active and a burnable item
                            this.addHeat(attributes, injectorPackage);
                        }
                    }
                } else {
                    // lose heat
                    this.loseHeat();
                }
            } else {
                // lose heat
                this.loseHeat();
            }
        } else {
            // lose heat
            this.loseHeat();
        }
    }

    private void updateRedstoneOutputLevel(int energyStored, int energyMaxStored, CustomEnergyStorage energyStorage) {
        BlockPos redstoneOutputPortPosition = this.getRedstoneOutputPortPosition();

        if (redstoneOutputPortPosition != null) {

            int signalStrength = RedstoneHelpers.computeSignalStrength(energyStored, energyMaxStored);
            boolean hasEnergyStored = energyStorage.getEnergyStored() > 0;
            BlockState redstoneOutputPortState = world.getBlockState(redstoneOutputPortPosition);

            if (redstoneOutputPortState != null && redstoneOutputPortPosition != null) {
                BlockState currentState = world.getBlockState(redstoneOutputPortPosition);

                if (currentState.get(BlockStateProperties.POWER_0_15) != signalStrength) {
                    BlockState newState = redstoneOutputPortState.with(BlockStateProperties.POWERED, Boolean.valueOf(hasEnergyStored)).with(BlockStateProperties.POWER_0_15, signalStrength);
                    world.setBlockState(redstoneOutputPortPosition, newState, 3);
                    world.notifyNeighbors(pos, newState.getBlock());
                }
            }
        }
    }

    private void loseHeat() {

        if (this.heat > 0) {
            this.heat -= .1f;
        }
    }

    private void addHeat(AcidAttributes attributes, InjectorPackage injectorPackage) {
        this.heat = TerraReactorEnergyMatrix.computeHeat(this.heat, this.heatAbsorptionRate, this.heatTickRate, ReactorBaseType.Terra, attributes, injectorPackage.getPotionAttributes());
    }

    @Override
    protected boolean isEnabled() {
        BlockPos redstoneInputPortPosition = this.getRedstoneInputPortPosition();
        if (redstoneInputPortPosition != null) {
            BlockState redstonePortState = world.getBlockState(redstoneInputPortPosition);

            // If the block is broken, it wont exist here
            if (redstonePortState == null || redstonePortState.getBlock() != ModBlocks.REACTOR_REDSTONE_PORT_BLOCK) {
                return false;
            }

            return world.getRedstonePowerFromNeighbors(redstoneInputPortPosition) > 0;

        }
        return false;
    }

    @Override
    protected void readNBT(CompoundNBT tag) {
        CompoundNBT dissolutionTag = tag.getCompound("dissolution_time");
        this.dissolutionProcessingUnit.deserializeNBT(dissolutionTag);

        CompoundNBT fatalHeatTag = tag.getCompound("fatal_heat_time");
        this.fatalHeatProcessingUnit.deserializeNBT(fatalHeatTag);

        this.heat = tag.getFloat("heat");

        acidTank.readFromNBT(tag.getCompound("acid"));

        CompoundNBT invTag = tag.getCompound("inv");

        ((INBTSerializable<CompoundNBT>) inventory).deserializeNBT(invTag);

        CompoundNBT energyTag = tag.getCompound("energy");
        ((INBTSerializable<CompoundNBT>) this.energyStore).deserializeNBT(energyTag);
    }

    @Override
    protected void writeNBT(CompoundNBT tag) {
        CompoundNBT inventoryTag = ((INBTSerializable<CompoundNBT>) inventory).serializeNBT();
        tag.put("inv", inventoryTag);

        CompoundNBT energyTag = ((INBTSerializable<CompoundNBT>) this.energyStore).serializeNBT();
        tag.put("energy", energyTag);

        CompoundNBT acidTankNBT = new CompoundNBT();
        tag.put("acid", acidTank.writeToNBT(acidTankNBT));

        CompoundNBT dissolutionTag = this.dissolutionProcessingUnit.serializeNBT();
        tag.put("dissolution_time", dissolutionTag);
        tag.putFloat("heat", this.heat);

        CompoundNBT fatalHeatTag = this.fatalHeatProcessingUnit.serializeNBT();
        tag.put("fatal_heat_time", fatalHeatTag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return  LazyOptional.of(() -> (T)this.inventory);
        }

        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> (T)this.energyStore);
        }

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) this.acidTank);
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
                () -> this.energyStore.getEnergyStored(),
                () -> this.energyStore.getMaxEnergyStored(),
                () -> this.getEnergyUsedPerTick(),
                () -> this.dissolutionProcessingUnit.getValue(),
                () -> this.dissolutionProcessingUnit.getTotal(),
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

    @Override
    public RedstoneActivationType getRedstoneActivationType() {
        return this.redstoneActivationType;
    }

    @Override
    public void setRedstoneActivationType(RedstoneActivationType type) {
        this.redstoneActivationType = type;
    }
}