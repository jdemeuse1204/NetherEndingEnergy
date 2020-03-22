package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import com.agrejus.netherendingenergy.common.IntArraySupplierReferenceHolder;
import com.agrejus.netherendingenergy.common.Ratio;
import com.agrejus.netherendingenergy.common.attributes.CustomFluidAttributes;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseType;
import com.agrejus.netherendingenergy.common.tank.MixableAcidFluidTank;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
import com.agrejus.netherendingenergy.fluids.ModFluids;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
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
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;

public class TerraCollectingStationTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public TerraCollectingStationTile() {
        super(ModBlocks.TERRA_COLLECTING_STATION_TILE);
    }

    private IntArraySupplierReferenceHolder referenceHolder;

    private int tick;
    private int progress;
    private int energyUsePerTick = 1;
    private final int ticksToCollect = 60; // 5 seconds
    private final int ticksToProcess = 120;
    private final int acidAmountToProcess = 100;
    private final float efficiency = .6f;

    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);
    private NEEFluidTank inputTank = new NEEFluidTank(1000) {
        @Override
        protected void onContentsChanged() {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    };

    private MixableAcidFluidTank outputTank = new MixableAcidFluidTank(4000) {
        @Override
        protected void onContentsChanged() {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    };

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(60000, 200, 0);
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(1) {

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
                BlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
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

                // Remove this?
                BlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                markDirty();
                return super.insertItem(slot, stack, simulate);
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                // Remove this?
                BlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                markDirty();
                return super.extractItem(slot, amount, simulate);
            }
        };
    }

    public FluidTank getInputTank() {
        return inputTank;
    }

    public MixableAcidFluidTank getOutputTank() {
        return outputTank;
    }

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        if (tick <= 0) {
            tick = 20; // Every second
        }

        AtomicBoolean shouldMarkDirtyAndUpdateBlock = new AtomicBoolean(false);
        TileEntity aboveTileEntity = world.getTileEntity(this.pos.up());

        // Collect every second
        if (this.tick == 20) {

            // Try and collect acid, doesn't require power
            if (aboveTileEntity != null) {
                inventory.ifPresent(x -> {

                    // Make sure there is a piece of dirt
                    if (x.getStackInSlot(0).getItem() == Items.DIRT) {

                        if (aboveTileEntity instanceof CausticBellTile) {
                            CausticBellTile bell = (CausticBellTile) aboveTileEntity;

                            float yield = bell.getYield(); // mB
                            float strength = bell.getStrengthRatio();
                            float purity = bell.getPurityRatio();


                            int fillAmount = Math.round(yield * strength * purity);
                            int resolvedAmount = inputTank.simulateFillAmount(fillAmount);

                            inputTank.fill(new FluidStack(ModFluids.RAW_ACID_FLUID, resolvedAmount), IFluidHandler.FluidAction.EXECUTE);
                            shouldMarkDirtyAndUpdateBlock.set(true);
                        }
                    }
                });
            }
        }

        boolean hasEnoughFluid = this.inputTank.getFluidInTank(0).getAmount() >= this.acidAmountToProcess;

        // Try and process
        if (this.progress == 0 && hasEnoughFluid) {
            this.progress = this.ticksToCollect;
        }

        if (this.progress > 0 && hasEnoughFluid) {
            energy.ifPresent(w -> {

                // Make sure we have enough energy
                if (w.getEnergyStored() >= this.energyUsePerTick) {
                    ((CustomEnergyStorage) w).consumeEnergy(energyUsePerTick);

                    --this.progress;

                    // processing finished
                    if (this.progress == 0) {
                        // Drain Raw Acid
                        inputTank.drain(this.acidAmountToProcess, IFluidHandler.FluidAction.EXECUTE);

                        // Fill Acid of the Ordinary
                        int fillAmount = Math.round(this.acidAmountToProcess * this.efficiency);
                        FluidStack stackToFill = getFillStack(fillAmount);
                        outputTank.fill(stackToFill, IFluidHandler.FluidAction.EXECUTE);
                    }

                    shouldMarkDirtyAndUpdateBlock.set(true);
                }
            });
        }

        // Try send out liquid
        this.sendOutLiquid();

        if (shouldMarkDirtyAndUpdateBlock.get() == true) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }

        --this.tick;
    }

    private FluidStack getFillStack(int amount) {

        FluidStack stackToFill = new FluidStack(ModFluids.ACID_OF_THE_ORDINARY, amount);
        CustomFluidAttributes attributes = (CustomFluidAttributes) stackToFill.getFluid().getAttributes();

        CompoundNBT fluidTag = new CompoundNBT();

        // need to do sea level minus max divided by 16.  ex: (62-254)/16 = 12 is the max
        int spatialAmount = ReactorBaseConfig.INSTANCE.ComputeSpatial(ReactorBaseType.Terra, pos.getY());

        FluidHelpers.serializeCustomFluidAttributes(fluidTag, attributes, spatialAmount);

        stackToFill.setTag(fluidTag);

        return stackToFill;
    }

    private void sendOutLiquid() {
        if (outputTank.getFluidAmount() > 0) {

            for (Direction direction : Direction.values()) {
                TileEntity tileEntity = world.getTileEntity(pos.offset(direction));

                if (tileEntity != null) {
                    tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).ifPresent(handler -> {

                        if (outputTank.getFluidAmount() > 1) {

                            int drainAmount = 1000;

                            if (outputTank.getFluidAmount() < 1000) {
                                drainAmount = outputTank.getFluidAmount();
                            }

                            FluidStack drained = outputTank.drain(new FluidStack(ModFluids.ACID_OF_THE_ORDINARY, drainAmount), IFluidHandler.FluidAction.EXECUTE);;

                            handler.fill(drained, IFluidHandler.FluidAction.EXECUTE);

                            markDirty();
                        }
                    });
                }
            }
        }
    }

    private void readNBT(CompoundNBT tag) {
        inputTank.readFromNBT(tag.getCompound("input_tank"));
        outputTank.readFromNBT(tag.getCompound("output_tank"));

        CompoundNBT invTag = tag.getCompound("inv");

        inventory.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));

        CompoundNBT energyTag = tag.getCompound("energy");

        energy.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));
    }

    private void writeNBT(CompoundNBT tag) {
        CompoundNBT inputTankNBT = new CompoundNBT();
        tag.put("input_tank", inputTank.writeToNBT(inputTankNBT));

        CompoundNBT outputTankNBT = new CompoundNBT();
        tag.put("output_tank", outputTank.writeToNBT(outputTankNBT));

        inventory.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("inv", compound);
        });

        energy.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("energy", compound);
        });
    }

    @Override
    public void read(CompoundNBT tag) {
        readNBT(tag);
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        writeNBT(tag);
        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        writeNBT(tag);
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {

        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        CompoundNBT tag = packet.getNbtCompound();
        readNBT(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) outputTank);
        }

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventory.cast();
        }

        //if (cap == CapabilityEnergy.ENERGY && side != null && side == Direction.DOWN) {
        if (cap == CapabilityEnergy.ENERGY) {
            // receive energy in the bottom only
            return energy.cast();
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
                () -> this.progress,
                () -> this.ticksToProcess,
                () -> this.energy.map(w -> w.getEnergyStored()).orElse(0),
                () -> this.energy.map(w -> w.getMaxEnergyStored()).orElse(0));

        return new TerraCollectingStationContainer(worldId, world, pos, playerInventory, playerEntity, referenceHolder);
    }
}
