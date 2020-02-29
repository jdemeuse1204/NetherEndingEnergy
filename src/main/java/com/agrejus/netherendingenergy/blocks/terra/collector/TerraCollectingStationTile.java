package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.Capabilities;
import com.agrejus.netherendingenergy.Config;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorConfig;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorMultiBlock;
import com.agrejus.netherendingenergy.common.IntArraySupplierReferenceHolder;
import com.agrejus.netherendingenergy.common.Ratio;
import com.agrejus.netherendingenergy.common.helpers.RedstoneHelpers;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TerraCollectingStationTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public TerraCollectingStationTile() {
        super(ModBlocks.TERRA_COLLECTING_STATION_TILE);
    }

    private int tickCounter;
    private Item growthMedium;

    private IntArraySupplierReferenceHolder referenceHolder;

    private final int cycleProcessAmount = 10; // need at least 10 in the tank to start processing
    private final int ticksToProcess = 200;
    private final int energyUsePerTick = 25;
    private int resovledCycleProcessAmount = 0;
    private int tickProcessCount = 0;
    private boolean isProcessing = false;

    public static final String INPUT_TANK_NAME = "INPUT";
    public static final String OUTPUT_TANK_NAME = "OUTPUT";

    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> this.createHandler(this));
    private NEEFluidTank inputTank = new NEEFluidTank(INPUT_TANK_NAME, 1000) {
        @Override
        protected void onContentsChanged() {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    };
    private NEEFluidTank outputTank = new NEEFluidTank(OUTPUT_TANK_NAME, 4000) {
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

    private IItemHandler createHandler(TerraCollectingStationTile tile) {
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

                tile.setGrowthMedium(stack.getItem());

                BlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                markDirty();
                return super.insertItem(slot, stack, simulate);
            }

            @Nonnull
            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                tile.setGrowthMedium(null);

                BlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                markDirty();
                return super.extractItem(slot, amount, simulate);
            }
        };
    }

    public NEEFluidTank getInputTank() {
        return inputTank;
    }

    public NEEFluidTank getOutputTank() {
        return outputTank;
    }

    public boolean hasGrowthMedium() {
        return this.growthMedium != null;
    }

    public Block getGrowthMediumBlock() {

        if (this.growthMedium == null) {
            return null;
        }

        return Block.getBlockFromItem(this.growthMedium);
    }

    public void setGrowthMedium(Item item) {
        this.growthMedium = item;
    }

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        AtomicBoolean shouldMarkDirtyAndUpdateBlock = new AtomicBoolean(false);
        boolean hasTwentyTickProcessingStarted = false;

        // check to see if we can process input tank filling
        if (tickCounter > 0) {
            tickCounter--;

            if (tickCounter <= 0) {
                hasTwentyTickProcessingStarted = true;
            }
        }

        if (this.isProcessing == true && tickProcessCount > 0) {
            AtomicBoolean finalShouldMarkDirtyAndUpdateBlock = shouldMarkDirtyAndUpdateBlock;
            energy.ifPresent(w -> {

                if (w.getEnergyStored() >= this.energyUsePerTick) {
                    ((CustomEnergyStorage)w).consumeEnergy(energyUsePerTick);

                    tickProcessCount--;
                    finalShouldMarkDirtyAndUpdateBlock.set(true);
                }
            });
        }

        if (hasTwentyTickProcessingStarted && inputTank.canFill() == true) {
            BlockPos aboveBlock = this.pos.up();
            TileEntity aboveTileEntity = world.getTileEntity(aboveBlock);
            if (aboveTileEntity instanceof CausticBellTile) {
                CausticBellTile bell = (CausticBellTile) aboveTileEntity;

                int yield = bell.getYield(); // mB
                Ratio strength = bell.getStrengthRatio();
                Ratio purity = bell.getPurityRatio();
                Ratio burnTimeAugment = bell.getBurnTimeAugmentRatio();

                // we will need to handle if the flower changes

                Ratio amount = Ratio.addMany(strength, purity, burnTimeAugment);
                int fillAmount = Math.round(yield * amount.asPercent());
                int resolvedAmount = inputTank.resolveFillAmount(fillAmount);

                inputTank.fill(new FluidStack(Fluids.LAVA.getStillFluid(), resolvedAmount), IFluidHandler.FluidAction.EXECUTE);
                shouldMarkDirtyAndUpdateBlock.set(true);
            }
        }

        // keep checking if we have enough to process
        if (hasTwentyTickProcessingStarted && this.canProcessInputFluid(cycleProcessAmount) && this.isProcessing == false) {

            // Make sure we have enough to drain
            this.resovledCycleProcessAmount = inputTank.resolveDrainAmount(cycleProcessAmount);
            this.isProcessing = true; // start processing
            this.tickProcessCount = this.ticksToProcess;
        }

        // if we lose fluid, stop processing
        if (hasTwentyTickProcessingStarted && this.canProcessInputFluid(this.resovledCycleProcessAmount) == false && this.isProcessing == true) {
            this.resovledCycleProcessAmount = 0;
            this.isProcessing = false;
            this.tickProcessCount = 0;
        }

        if (tickCounter <= 0) {
            tickCounter = 20; // Every second
        }

        if (this.isProcessing == true && tickProcessCount <= 0) {

            // Stop Processing
            tickProcessCount = 0;
            this.isProcessing = false;

            inputTank.drain(this.resovledCycleProcessAmount, IFluidHandler.FluidAction.EXECUTE);
            outputTank.fill(new FluidStack(Fluids.LAVA.getStillFluid(), this.resovledCycleProcessAmount), IFluidHandler.FluidAction.EXECUTE);

            this.tickProcessCount = 0;
            this.isProcessing = false;
            shouldMarkDirtyAndUpdateBlock.set(true);
        }


        BlockPos acidPortPosition = TerraReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.TERRA_REACTOR_ACID_PORT_BLOCK, TerraReactorConfig.INSTANCE);

        if (acidPortPosition != null) {
            this.sendOutLiquid();
        }

        if (shouldMarkDirtyAndUpdateBlock.get() == true) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    }

    private void sendOutLiquid() {
        if (outputTank.getFluidAmount() > 0) {

            for(Direction direction: Direction.values()) {
                TileEntity te = world.getTileEntity(pos.offset(direction));

                if (te != null) {
                    boolean doContinue = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction).map(handler -> {
                        NEEFluidTank tank = (NEEFluidTank)handler;
                        if (tank.canFill() && outputTank.getFluidAmount() > 1) {
                            tank.fill(new FluidStack(Fluids.LAVA.getStillFluid(), 1), IFluidHandler.FluidAction.EXECUTE);
                            outputTank.drain(new FluidStack(Fluids.LAVA.getStillFluid(), 1), IFluidHandler.FluidAction.EXECUTE);


                            markDirty();

                            return outputTank.getFluidAmount() > 0;
                        } else {
                            return true;
                        }
                    }).orElse(true);

                    if (!doContinue) {
                        return;
                    }
                }
            }
        }
    }

    private boolean canProcessInputFluid(int amountNeeded) {

        // need to make wait until we have enough to process
        return inputTank.getFluidAmount() >= amountNeeded && outputTank.canFill() && outputTank.resolveFillAmount(amountNeeded) == amountNeeded;
    }

    @Override
    public void read(CompoundNBT tag) {

        inputTank.readFromNBT(tag.getCompound("input_tank"));
        outputTank.readFromNBT(tag.getCompound("output_tank"));

        CompoundNBT invTag = tag.getCompound("inv");

        itemHandler.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));

        CompoundNBT energyTag = tag.getCompound("energy");

        energy.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));

        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        CompoundNBT inputTankNBT = new CompoundNBT();
        tag.put("input_tank", inputTank.writeToNBT(inputTankNBT));

        CompoundNBT outputTankNBT = new CompoundNBT();
        tag.put("output_tank", outputTank.writeToNBT(outputTankNBT));

        itemHandler.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("inv", compound);
        });

        energy.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("energy", compound);
        });

        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        CompoundNBT inputTankNBT = new CompoundNBT();
        CompoundNBT outputTankNBT = new CompoundNBT();

        inputTank.writeToNBT(inputTankNBT);
        outputTank.writeToNBT(outputTankNBT);

        itemHandler.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("inv", compound);
        });

        energy.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("energy", compound);
        });

        tag.put("input_tank", inputTankNBT);
        tag.put("output_tank", outputTankNBT);

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
        inputTank.readFromNBT(nbt.getCompound("input_tank"));
        outputTank.readFromNBT(nbt.getCompound("output_tank"));

        CompoundNBT invTag = nbt.getCompound("inv");
        CompoundNBT energyTag = nbt.getCompound("energy");

        itemHandler.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));
        energy.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) outputTank);
        }

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
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
                () -> this.tickProcessCount,
                () -> this.ticksToProcess,
                () -> this.energy.map(w -> w.getEnergyStored()).orElse(0),
                () -> this.energy.map(w -> w.getMaxEnergyStored()).orElse(0));

        return new TerraCollectingStationContainer(worldId, world, pos, playerInventory, playerEntity, referenceHolder);
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     * FROM FURNACE
     */
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }
}
