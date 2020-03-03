package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.Config;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorConfig;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.helpers.RedstoneHelpers;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
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
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TerraReactorCoreTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    // Internal Storage
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private NEEFluidTank acidTank = new NEEFluidTank("ACID", 5000) {
        @Override
        protected void onContentsChanged() {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    };

    // Properties
    private @Nullable
    BlockPos redstoneInputPortPosition;
    private @Nullable
    BlockPos energyPortPosition;
    private @Nullable
    BlockPos redstoneOutputPortPosition;
    private @Nullable
    BlockState redstoneOutputPortState;
    private int counter;
    private static Map<Item, Integer> burnTimes = AbstractFurnaceTileEntity.getBurnTimes();

    public TerraReactorCoreTile() {
        super(ModBlocks.TERRA_REACTOR_CORE_TILE);
    }

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(Config.FIRSTBLOCK_MAXPOWER.get(), 0, 0);
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(1) {

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

                // Burnable Items Only
                if (isItemValid(slot, stack) == false) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public void tick() {
        if (world.isRemote) {
            return;
        }

        if (world.getBlockState(pos).get(TerraReactorCoreBlock.FORMED) == TerraReactorPartIndex.UNFORMED) {
            this.redstoneInputPortPosition = null;
            this.energyPortPosition = null;
            this.redstoneOutputPortPosition = null;
            this.redstoneOutputPortState = null;
            return;
        }

        // store block positions in NBT so we dont need to keep looking them up?
        if (this.redstoneInputPortPosition == null) {
            this.redstoneInputPortPosition = TerraReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.REACTOR_REDSTONE_PORT_BLOCK, TerraReactorConfig.INSTANCE);
        }

        if (this.energyPortPosition == null) {
            this.energyPortPosition = TerraReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.TERRA_REACTOR_ENERGY_PORT_BLOCK, TerraReactorConfig.INSTANCE);
        }

        if (this.redstoneOutputPortPosition == null) {
            this.redstoneOutputPortPosition = TerraReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.TERRA_REACTOR_REDSTONE_OUTPUT_PORT_BLOCK, TerraReactorConfig.INSTANCE);
        }

        if (this.redstoneOutputPortState == null && this.redstoneOutputPortPosition != null) {
            this.redstoneOutputPortState = world.getBlockState(this.redstoneOutputPortPosition);
        }

        if (this.redstoneInputPortPosition != null) {
            BlockState redstonePortState = world.getBlockState(this.redstoneInputPortPosition);

            // If the block is broken, it wont exist here
            if (redstonePortState != null && redstonePortState.getBlock() != ModBlocks.REACTOR_REDSTONE_PORT_BLOCK) {
                this.redstoneInputPortPosition = null;
                redstonePortState = null;
            }

            if (redstonePortState != null && redstonePortState.get(BlockStateProperties.POWERED) == true) {
                // Reactor On
                energy.ifPresent(w -> {

                    ((CustomEnergyStorage) w).addEnergy(100);
                    int energyStored = w.getEnergyStored();
                    int energyMaxStored = w.getMaxEnergyStored();
                    int signalStrength = RedstoneHelpers.computeSignalStrength(energyStored, energyMaxStored);
                    boolean hasEnergyStored = w.getEnergyStored() > 0;

                    if (this.redstoneOutputPortState != null && this.redstoneOutputPortPosition != null) {
                        BlockState newState = this.redstoneOutputPortState.with(BlockStateProperties.POWERED, Boolean.valueOf(hasEnergyStored)).with(BlockStateProperties.POWER_0_15, signalStrength);
                        world.setBlockState(this.redstoneOutputPortPosition, newState, 3);
                        world.notifyNeighbors(pos, newState.getBlock());
                    }
                });

                if (counter <= 0) {
                    // Extract 1 diamond
                    handler.ifPresent((w -> {
                        ItemStack stack = w.getStackInSlot(0);
                        if (stack.getItem() == Items.DIAMOND) {
                            w.extractItem(0, 1, false);
                            counter = Config.FIRSTBLOCK_TICKS.get();
                            markDirty();
                        }
                    }));
                }
            }
        }

        if (this.energyPortPosition != null) {
            this.sendOutPower(this.energyPortPosition);
        }

        if (counter <= 0) {
            counter = 20; // Every second
        }
    }

    private void sendOutPower(BlockPos energyPortPosition) {
        // THIS IS NOT ACCURATE, CLIENT IS OUT OF SYNC!!!!
        energy.ifPresent(energy -> {
            // Atomic Integer is a value that you can change inside of a lambda
            AtomicInteger capacity = new AtomicInteger(energy.getEnergyStored());

            if (capacity.get() > 0) {

                Direction direction = world.getBlockState(energyPortPosition).get(BlockStateProperties.FACING);
                TileEntity te = world.getTileEntity(energyPortPosition.offset(direction));

                if (te != null) {
                    boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                        if (handler.canReceive()) {
                            int received = handler.receiveEnergy(Math.min(capacity.get(), Config.FIRSTBLOCK_SEND.get()), false);
                            capacity.addAndGet(-received);

                            // Extract from our own energy
                            ((CustomEnergyStorage) energy).consumeEnergy(received);

                            int energyMaxStored = handler.getMaxEnergyStored();
                            int energyStored = capacity.get();
                            int signalStrength = RedstoneHelpers.computeSignalStrength(energyStored, energyMaxStored);
                            boolean hasEnergyStored = energyStored > 0;

                            if (this.redstoneOutputPortState != null && this.redstoneOutputPortPosition != null) {
                                BlockState newState = this.redstoneOutputPortState.with(BlockStateProperties.POWERED, Boolean.valueOf(hasEnergyStored)).with(BlockStateProperties.POWER_0_15, signalStrength);
                                world.setBlockState(this.redstoneOutputPortPosition, newState, 3);
                                world.notifyNeighbors(pos, newState.getBlock());
                            }

                            markDirty();

                            return capacity.get() > 0;
                        } else {
                            return true;
                        }
                    }).orElse(true);

                    if (!doContinue) {
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void read(CompoundNBT tag) {

        acidTank.readFromNBT(tag.getCompound("acid"));

        CompoundNBT invTag = tag.getCompound("inv");

        handler.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));

        CompoundNBT energyTag = tag.getCompound("energy");
        // Save energy when block is broken
        energy.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        // when block is placed?
        handler.ifPresent(w -> {
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

        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
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
        return new TerraReactorCoreContainer(worldId, world, pos, playerInventory, playerEntity);
    }
}