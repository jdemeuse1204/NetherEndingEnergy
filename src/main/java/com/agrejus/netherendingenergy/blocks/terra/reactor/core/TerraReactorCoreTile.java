package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.Config;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicInteger;

public class TerraReactorCoreTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private @Nullable
    BlockPos redstoneInputPortPosition;
    private @Nullable
    BlockPos energyPortPosition;
    private @Nullable
    BlockPos redstoneOutputPortPosition;
    private int counter;

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
                // Marks the tile entity as changed so the system knows it needs to be saved
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == Items.DIAMOND;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

                if (stack.getItem() != Items.DIAMOND) {
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

        // store block positions in NBT so we dont need to keep looking them up?
        if (this.redstoneInputPortPosition == null) {
            this.redstoneInputPortPosition = TerraReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.REACTOR_REDSTONE_PORT_BLOCK);
        }

        if (this.energyPortPosition == null) {
            this.energyPortPosition = TerraReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.REACTOR_ENERGY_PORT_BLOCK);
        }

        if (this.redstoneOutputPortPosition == null) {
            this.redstoneOutputPortPosition = TerraReactorMultiBlock.INSTANCE.getBlockFromControllerPosition(world, pos, ModBlocks.TERRA_REACTOR_REDSTONE_OUTPUT_PORT_BLOCK);
        }

        if (this.redstoneInputPortPosition != null) {
            BlockState redstonePortState = world.getBlockState(this.redstoneInputPortPosition);

            if (redstonePortState != null && redstonePortState.get(BlockStateProperties.POWERED) == true) {
                // Reactor On
                energy.ifPresent(w -> {

                    ((CustomEnergyStorage) w).addEnergy(Config.FIRSTBLOCK_GENERATE.get());

                    // OPTIMIZE THIS!!!!
                    if (((CustomEnergyStorage) w).getEnergyStored() > 0) {
                        if (this.redstoneOutputPortPosition != null) {
                            BlockState redstoneOutputPortState = world.getBlockState(this.redstoneOutputPortPosition);
                            world.setBlockState(this.redstoneOutputPortPosition, redstoneOutputPortState.with(BlockStateProperties.POWERED, Boolean.valueOf(true)).with(BlockStateProperties.POWER_0_15, 2));
                        }
                    } else {
                        if (this.redstoneOutputPortPosition != null) {
                            BlockState redstoneOutputPortState = world.getBlockState(this.redstoneOutputPortPosition);
                            world.setBlockState(this.redstoneOutputPortPosition, redstoneOutputPortState.with(BlockStateProperties.POWERED, Boolean.valueOf(false)).with(BlockStateProperties.POWER_0_15, 0));
                        }
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

/*        // Generating Power when counter is greater than 0
        BlockState blockState = world.getBlockState(pos);
        if (blockState.get(BlockStateProperties.POWERED) != (counter > 0)) {
            world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, counter > 0), 3);
        }*/

        if (counter <= 0) {
            counter = 20; // Every second
        }
    }

    private void sendOutPower(BlockPos energyPortPosition) {
        //Config.FIRSTBLOCK_SEND.get()
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
        // When block is broken?

        // TAGS MUST BE IN LOOT TABLE AS WELL!!!
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
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        BlockState state = world.getBlockState(pos);
        // Not formed
        if (state.getBlock() != ModBlocks.TERRA_REACTOR_CORE_BLOCK || state.get(TerraReactorCoreBlock.FORMED) == TerraReactorPartIndex.UNFORMED) {
            return null;
        }

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        if (cap == CapabilityEnergy.ENERGY) {
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
        return new TerraReactorCoreContainer(worldId, world, pos, playerInventory, playerEntity);
    }
}