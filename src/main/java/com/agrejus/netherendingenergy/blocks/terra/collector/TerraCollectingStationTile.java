package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.Capabilities;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import com.agrejus.netherendingenergy.common.Ratio;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
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
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TerraCollectingStationTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public TerraCollectingStationTile() {

        super(ModBlocks.TERRA_COLLECTING_STATION_TILE);
        bufferSize = 0;
    }

    private int tickCounter;
    private int tickProcessCounter;
    private Item growthMedium;

    public static final String INPUT_TANK_NAME = "INPUT";
    public static final String OUTPUT_TANK_NAME = "OUTPUT";

    // how much input produces 1 output?  Ratio = 6:1
    private int bufferSize;

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

        boolean shouldMarkDirtyAndUpdateBlock = false;
        boolean canProcessInputTankFill = false;
        boolean canProcessOutputTankFill = false;

        // check to see if we can process input tank filling
        if (tickCounter > 0) {
            tickCounter--;

            if (tickCounter <= 0) {
                canProcessInputTankFill = true;
            }
        }

        // check to see if we can process fluid from input tank
        if (tickProcessCounter > 0) {
            tickProcessCounter--;

            if (tickProcessCounter <= 0) {
                canProcessOutputTankFill = true;
            }
        }

        if (canProcessInputTankFill && inputTank.canFill() == true) {
            BlockPos aboveBlock = this.pos.up();
            TileEntity aboveTileEntity = world.getTileEntity(aboveBlock);
            if (aboveTileEntity instanceof CausticBellTile) {
                CausticBellTile bell = (CausticBellTile) aboveTileEntity;

                // This is a problem if the flower ever changes, introduce mixing?
                this.bufferSize = bell.getYield(); // mB
                Ratio strength = bell.getStrengthRatio();
                Ratio purity = bell.getPurityRatio();
                Ratio burnTimeAugment = bell.getBurnTimeAugmentRatio();

                Ratio amount = Ratio.addMany(strength, purity, burnTimeAugment);

                int resolvedAmount = inputTank.resolveFillAmount(1);

                inputTank.fill(new FluidStack(Fluids.LAVA.getStillFluid(), resolvedAmount), IFluidHandler.FluidAction.EXECUTE);
                shouldMarkDirtyAndUpdateBlock = true;
            }
        }

        if (canProcessOutputTankFill && bufferSize > 0 && this.canProcessInputFluid(bufferSize) && outputTank.canFill() == true) {

            // Make sure we have enough to drain
            int resultingProcessAmount = 1;
            int resolvedInputDrainAmount = inputTank.resolveDrainAmount(bufferSize);

            if (resolvedInputDrainAmount > 0) {
                inputTank.drain(resolvedInputDrainAmount, IFluidHandler.FluidAction.EXECUTE);

                outputTank.fill(new FluidStack(Fluids.LAVA.getStillFluid(), resultingProcessAmount), IFluidHandler.FluidAction.EXECUTE);
                shouldMarkDirtyAndUpdateBlock = true;
            }
        }

        if (shouldMarkDirtyAndUpdateBlock == true) {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }

        if (tickCounter <= 0) {
            tickCounter = 20; // Every second
        }

        if (tickProcessCounter <= 0) {
            tickProcessCounter = 60; // Every 3 seconds
        }
    }

    private boolean canProcessInputFluid(int amountNeeded) {

        // need to make wait until we have enough to process
        return inputTank.getFluidAmount() >= amountNeeded && outputTank.canFill();
    }

    @Override
    public void read(CompoundNBT tag) {

        inputTank.readFromNBT(tag.getCompound("input_tank"));
        outputTank.readFromNBT(tag.getCompound("output_tank"));

        CompoundNBT invTag = tag.getCompound("inv");

        itemHandler.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));

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

        itemHandler.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) outputTank);
        }

        if (cap == Capabilities.MULTI_FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) new NEEFluidTank[]{outputTank, inputTank});
        }

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandler.cast();
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
        return new TerraCollectingStationContainer(worldId, world, pos, playerInventory, playerEntity);
    }
}
