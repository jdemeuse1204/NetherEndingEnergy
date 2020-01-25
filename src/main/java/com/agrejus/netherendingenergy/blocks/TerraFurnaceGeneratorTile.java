package com.agrejus.netherendingenergy.blocks;

import com.agrejus.netherendingenergy.Config;
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

public class TerraFurnaceGeneratorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private int counter;
// Follow AbstractFurnaceTileEntity
    public TerraFurnaceGeneratorTile() {
        super(ModBlocks.TERRA_FURNACE_GENERATOR_TILE);
    }

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(Config.FIRSTBLOCK_MAXPOWER.get(), 0);
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

        if (counter > 0) {
            // Produce Power
            counter--;
            if (counter <= 0) {
                energy.ifPresent(w -> ((CustomEnergyStorage) w).addEnergy(Config.FIRSTBLOCK_GENERATE.get()));
            }
            markDirty();
        }

        if (counter <= 0){
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

        // Generating Power when counter is greater than 0
        BlockState blockState = world.getBlockState(pos);
        if (blockState.get(BlockStateProperties.POWERED) != (counter > 0)) {
            world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, counter > 0), 3);
        }

        sendOutPower();
    }

    private void sendOutPower() {
        //Config.FIRSTBLOCK_SEND.get()
        energy.ifPresent(energy -> {
            // Atomic Integer is a value that you can change inside of a lambda
            AtomicInteger capacity = new AtomicInteger(energy.getEnergyStored());

            if(capacity.get() > 0) {

                // Check each direction and see if we can send out power
                for(Direction direction: Direction.values()) {
                    TileEntity te = world.getTileEntity(pos.offset(direction));

                    if (te != null) {
                        boolean doContinue = te.getCapability(CapabilityEnergy.ENERGY, direction).map(handler -> {
                            if (handler.canReceive()) {
                                int received = handler.receiveEnergy(Math.min(capacity.get(), Config.FIRSTBLOCK_SEND.get()), false);
                                capacity.addAndGet(-received);

                                // Extract from our own energy
                                ((CustomEnergyStorage)energy).consumeEnergy(received);

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
        return new TerraFurnaceGeneratorContainer(worldId, world, pos, playerInventory, playerEntity);
    }
}
