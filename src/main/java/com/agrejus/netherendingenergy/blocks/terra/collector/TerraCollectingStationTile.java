package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import com.agrejus.netherendingenergy.common.IntArraySupplierReferenceHolder;
import com.agrejus.netherendingenergy.common.attributes.CustomFluidAttributes;
import com.agrejus.netherendingenergy.common.blocks.RedstoneEnergyTile;
import com.agrejus.netherendingenergy.common.enumeration.LevelType;
import com.agrejus.netherendingenergy.common.factories.EnergyStoreFactory;
import com.agrejus.netherendingenergy.common.factories.ItemHandlerFactory;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IProcessingUnit;
import com.agrejus.netherendingenergy.common.interfaces.IRedstoneActivatable;
import com.agrejus.netherendingenergy.common.models.InvertedProcessingUnit;
import com.agrejus.netherendingenergy.common.models.ProcessingUnit;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.agrejus.netherendingenergy.common.tank.MixableAcidFluidTank;
import com.agrejus.netherendingenergy.common.tank.PartialNumberTank;
import com.agrejus.netherendingenergy.fluids.ModFluids;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TerraCollectingStationTile extends RedstoneEnergyTile implements INamedContainerProvider, IRedstoneActivatable {

    private IntArraySupplierReferenceHolder referenceHolder;

    // Processing
    private IProcessingUnit collectionProcessingUnit = new ProcessingUnit(20);
    private IProcessingUnit refineProcessingUnit = new InvertedProcessingUnit(120);

    // Machine Settings
    private final int acidAmountToProcess = 10;
    private final float efficiency = .6f;

    // Fields
    private boolean hasBellPlanted = false;
    private float bellPurity = 0;
    private float bellStrength = 0;
    private float bellYield = 0;
    private float cycleCollectionAmount = 0;

    public TerraCollectingStationTile() {
        super(ModBlocks.TERRA_COLLECTING_STATION_TILE);
    }

    @Override
    protected CustomEnergyStorage createEnergyStore() {
        return EnergyStoreFactory.createEnergyStore(LevelType.TERRA);
    }

    private IItemHandler outputSlotInventory = ItemHandlerFactory.createEmptyBucketSlotInventory(() -> markDirty());
    private IItemHandler outputResultSlotInventory = ItemHandlerFactory.createResultSlotInventory(() -> markDirty());

    public LazyOptional<IItemHandler> getOutputSlotInventory() {
        return LazyOptional.of(() -> this.outputSlotInventory);
    }

    public LazyOptional<IItemHandler> getOutputResultSlotInventory() {
        return LazyOptional.of(() -> this.outputResultSlotInventory);
    }

    private IItemHandler inventory = new ItemStackHandler(1) {

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            super.setStackInSlot(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            // Marks the tile entity as changed so the system knows it needs to be saved
            update();
            markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            Item item = stack.getItem();
            return item == Items.DIRT;
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

            if (isItemValid(slot, stack) == false) {
                return stack;
            }

            update();
            markDirty();
            return super.insertItem(slot, stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            update();
            markDirty();
            return super.extractItem(slot, amount, simulate);
        }
    };

    private PartialNumberTank inputTank = new PartialNumberTank(1000) {
        @Override
        protected void onContentsChanged() {
            update();
            markDirty();
        }
    };

    private MixableAcidFluidTank outputTank = new MixableAcidFluidTank(4000) {
        @Override
        protected void onContentsChanged() {
            update();
            markDirty();
        }
    };

    public FluidTank getInputTank() {
        return inputTank;
    }

    public MixableAcidFluidTank getOutputTank() {
        return outputTank;
    }


    @Override
    protected void serverTick() {

        this.tryFillFromOutputTank();
        TileEntity aboveTileEntity = world.getTileEntity(this.pos.up());

        this.hasBellPlanted = true;
        if (aboveTileEntity == null || (aboveTileEntity instanceof CausticBellTile) == false) {
            this.hasBellPlanted = false;
        }

        this.cycleCollectionAmount = 0;

        if (this.hasBellPlanted == true && this.hasGrowthMedium() == false) {
            // remove planted bell
            world.destroyBlock(pos.up(), true);

            // Update above tile entity
            aboveTileEntity = world.getTileEntity(pos.up());
            this.hasBellPlanted = false;
        }

        if (this.isEnabled() == false) {
            return;
        }

        this.bellStrength = 0;
        this.bellPurity = 0;
        this.bellYield = 0;

        if (hasBellPlanted == true) {
            this.refineRawAcid();
            this.processBellAcidCollection(aboveTileEntity);
        } else {
            this.collectionProcessingUnit.reset();
        }
    }

    private void refineRawAcid() {
        boolean hasEnoughFluid = this.inputTank.getFluidInTank(0).getAmount() >= this.acidAmountToProcess;

        // Try and process
        int value = this.refineProcessingUnit.getValue();
        if (value == 0 && hasEnoughFluid) {
            this.refineProcessingUnit.reset();
        }

        if (value > 0 && hasEnoughFluid) {
            if (energyStore.hasEnoughEnergyStored()) {
                energyStore.consumeEnergyPerTick();

                this.refineProcessingUnit.setNext();

                // processing finished
                if (this.refineProcessingUnit.canProcess()) {

                    // Drain Raw Acid
                    inputTank.drain(this.acidAmountToProcess, IFluidHandler.FluidAction.EXECUTE);

                    // Fill Acid of the Ordinary
                    int fillAmount = Math.round(this.acidAmountToProcess * this.efficiency);
                    FluidStack stackToFill = getFillStack(fillAmount);
                    outputTank.fill(stackToFill, IFluidHandler.FluidAction.EXECUTE);
                }

                markUpdateAndDirty();
            }
        }
    }

    private boolean hasGrowthMedium() {
        return this.inventory.getStackInSlot(0).isEmpty() == false;
    }

    private void tryFillFromOutputTank() {
        FluidHelpers.fillBucketFromTankAndPutInInventory(outputTank, outputSlotInventory, outputResultSlotInventory, () -> markUpdateAndDirty());
    }

    private float getBellCollectionAmount(CausticBellTile bell) {

        this.bellStrength = bell.getStrength().getCurrent();
        this.bellPurity = bell.getPurity().getCurrent();
        this.bellYield = bell.getYield().getCurrent();

        return bellYield * bellStrength * bellPurity;
    }

    private void processBellAcidCollection(TileEntity aboveTileEntity) {
        // Collect every second
        this.cycleCollectionAmount = getBellCollectionAmount((CausticBellTile) aboveTileEntity);

        if (this.collectionProcessingUnit.canProcess() == true) {

            if (this.inventory.getStackInSlot(0).getItem() == Items.DIRT) {

                inputTank.fill(new FluidStack(ModFluids.RAW_ACID_FLUID, (int) this.cycleCollectionAmount), this.cycleCollectionAmount, IFluidHandler.FluidAction.EXECUTE);
                markUpdateAndDirty();
            }
        }

        this.collectionProcessingUnit.setNext();
    }

    private FluidStack getFillStack(int amount) {

        FluidStack stackToFill = new FluidStack(ModFluids.ACID_OF_THE_ORDINARY, amount);
        CustomFluidAttributes attributes = (CustomFluidAttributes) stackToFill.getFluid().getAttributes();

        CompoundNBT fluidTag = new CompoundNBT();

        // need to do sea level minus max divided by 16.  ex: (62-254)/16 = 12 is the max
        int spatialAmount = ReactorBaseConfig.INSTANCE.ComputeSpatial(world, pos);

        FluidHelpers.serializeCustomFluidAttributes(fluidTag, attributes, spatialAmount);

        stackToFill.setTag(fluidTag);

        return stackToFill;
    }

    @Override
    protected void readNBT(CompoundNBT tag) {
        inputTank.readFromNBT(tag.getCompound("input_tank"));
        outputTank.readFromNBT(tag.getCompound("output_tank"));

        CompoundNBT invTag = tag.getCompound("inv");

        ((INBTSerializable<CompoundNBT>) inventory).deserializeNBT(invTag);

        CompoundNBT energyTag = tag.getCompound("energy");

        ((INBTSerializable<CompoundNBT>) energyStore).deserializeNBT(energyTag);
    }

    @Override
    protected void writeNBT(CompoundNBT tag) {
        CompoundNBT inputTankNBT = new CompoundNBT();
        tag.put("input_tank", inputTank.writeToNBT(inputTankNBT));

        CompoundNBT outputTankNBT = new CompoundNBT();
        tag.put("output_tank", outputTank.writeToNBT(outputTankNBT));

        CompoundNBT invCompound = ((INBTSerializable<CompoundNBT>) inventory).serializeNBT();
        tag.put("inv", invCompound);

        CompoundNBT energyCompound = ((INBTSerializable<CompoundNBT>) energyStore).serializeNBT();
        tag.put("energy", energyCompound);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) outputTank);
        }

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) inventory);
        }

        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> (T) energyStore);
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

        // This is all of the data we want to pass to the container which the screen will use!
        this.referenceHolder = new IntArraySupplierReferenceHolder(
                () -> this.outputTank.getCapacity(),
                () -> this.outputTank.getFluidAmount(),
                () -> this.inputTank.getCapacity(),
                () -> this.inputTank.getFluidAmount(),
                () -> this.refineProcessingUnit.getValue(),
                () -> this.refineProcessingUnit.getTotal(),
                () -> this.energyStore.getEnergyStored(),
                () -> this.energyStore.getMaxEnergyStored(),
                () -> this.outputTank.getFluid().getFluid().getAttributes().getColor(),
                () -> this.inputTank.getFluid().getFluid().getAttributes().getColor(),
                () -> this.collectionProcessingUnit.getValue(),
                () -> this.collectionProcessingUnit.getTotal(),
                () -> (int) (this.cycleCollectionAmount * 10000),
                () -> this.getEnergyUsedPerTick(),
                () -> (int) (this.bellYield * 10000),
                () -> (int) (this.bellStrength * 10000),
                () -> (int) (this.bellPurity * 10000),
                () -> this.hasBellPlanted ? 1 : 0);

        return new TerraCollectingStationContainer(worldId, world, pos, playerInventory, playerEntity, referenceHolder);
    }
}
