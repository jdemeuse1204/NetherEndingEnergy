package com.agrejus.netherendingenergy.blocks.general.generator;

import com.agrejus.netherendingenergy.Config;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.helpers.EnergyHelper;
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

public class FurnaceGeneratorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private int tick;

    public FurnaceGeneratorTile() {
        super(ModBlocks.FURNACE_GENERATOR_TILE);
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

        if (tick > 0) {
            // Produce Power
            tick--;
            if (tick <= 0) {
                energy.ifPresent(w -> ((CustomEnergyStorage) w).addEnergy(Config.FIRSTBLOCK_GENERATE.get()));
            }
            markDirty();
        }

        if (tick <= 0) {
            // Extract 1 diamond
            handler.ifPresent((w -> {
                ItemStack stack = w.getStackInSlot(0);
                if (stack.getItem() == Items.DIAMOND) {
                    w.extractItem(0, 1, false);
                    tick = Config.FIRSTBLOCK_TICKS.get();
                    markDirty();
                }
            }));
        }

        // Generating Power when counter is greater than 0
        BlockState blockState = world.getBlockState(pos);
        if (blockState.get(BlockStateProperties.POWERED) != (tick > 0)) {
            world.setBlockState(pos, blockState.with(BlockStateProperties.POWERED, tick > 0), 3);
        }

        EnergyHelper.trySendEnergyAllDirections(this.energy, world, pos);
    }

    @Override
    public void read(CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");
        handler.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));

        CompoundNBT energyTag = tag.getCompound("energy");
        energy.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        handler.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("inv", compound);
        });

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
        return new FurnaceGeneratorContainer(worldId, world, pos, playerInventory, playerEntity);
    }
}
