package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.Config;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorConfig;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.IntArraySupplierReferenceHolder;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import com.agrejus.netherendingenergy.common.fluids.attributes.AcidAttributes;
import com.agrejus.netherendingenergy.common.helpers.RedstoneHelpers;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseType;
import com.agrejus.netherendingenergy.common.tank.AcidFluidTank;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
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
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
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
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TerraReactorCoreTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    // Need a double inventory, one for the item being reacted with for display (1 stack of 1 item) and another for holding the other 63.
    // We can get away with 1 inventory, but with 2 slots,  just set the stack size

    private int tick;
    private int generatedEnergyPerTick;
    private int currentBurnTimeTicks;
    private int heat;
    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private AcidFluidTank acidTank = new AcidFluidTank(5000) {

        // on add, mix attributes in case the are different

        @Override
        protected void onContentsChanged() {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    };

    // Properties
    private IntArraySupplierReferenceHolder referenceHolder;

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
        return new CustomEnergyStorage(1000000, 0, 1000);
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

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        if (tick <= 0) {
            tick = 20; // Every second
        }

        if (isReactorFormed() == false) {
            return;
        }

        this.resolveCache();

        if (this.isReactorOn() == true) {

            // Set generated per tick to 0, we will set this later
            this.generatedEnergyPerTick = 0;

            FluidStack fluidStack = acidTank.getFluid();

            // Make sure we have acid
            if (fluidStack.isEmpty() == false && fluidStack.getAmount() >= 1) {

                // get the acid attributes
                AcidAttributes attributes = FluidHelpers.deserializeAttributes(fluidStack.getTag());

                // make sure we have an item to react with
                if (this.currentBurnTimeTicks == 0) {
                    handler.ifPresent((w -> {
                        ItemStack stack = w.getStackInSlot(0);
                        if (stack.isEmpty() == false) {
                            int burnTime = getBurnTimes().get(stack.getItem());

                            if (burnTime > 0) {

                                // Consume the item
                                w.extractItem(0, 1, false);
                                float efficiency = TerraReactorEnergyMatrix.getEfficiency(ReactorBaseType.Terra, attributes);
                                this.currentBurnTimeTicks = (int)(burnTime * efficiency);

                                // set block state?
                                markDirty();
                            }
                        }
                    }));
                }

                // this will be zero if we don't have any items to react with
                if (this.currentBurnTimeTicks > 0) {

                    // can we add energy?
                    energy.ifPresent(x -> {
                        int energyStored = x.getEnergyStored();
                        int energyMaxStored = x.getMaxEnergyStored();
                        int delta = energyMaxStored - energyStored;

                        // Make sure we can add energy
                        if (delta > 0) {

                            // Try to consume the acid and make sure it drained properly
                            if (acidTank.drain(1, IFluidHandler.FluidAction.EXECUTE).getAmount() == 1) {

                                int energyToAdd = TerraReactorEnergyMatrix.getEnergyPerTick(ReactorBaseType.Terra, attributes);

                                // Decrement the current items burn time
                                --this.currentBurnTimeTicks;

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
                            }
                        }
                    });
                }
            }
        }

        if (this.energyPortPosition != null) {
            this.sendOutPower(this.energyPortPosition);
        }

        --this.tick;
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
        // THIS IS NOT ACCURATE, CLIENT IS OUT OF SYNC!!!!
        energy.ifPresent(w -> {

            CustomEnergyStorage energyStore = (CustomEnergyStorage)w;

            if (energyStore.getEnergyStored() > 0) {

                Direction direction = world.getBlockState(energyPortPosition).get(BlockStateProperties.FACING);
                TileEntity tileEntity = world.getTileEntity(energyPortPosition.offset(direction));

                /*if (tileEntity != null) {

                   tileEntity.getCapability(CapabilityEnergy.ENERGY, direction).ifPresent(x -> {
                        if (x.canReceive()) {
                            int received = x.receiveEnergy(Math.min(capacity.get(), Config.FIRSTBLOCK_SEND.get()), false);
                            capacity.addAndGet(-received);

                            // Extract from our own energy
                            energyStore.consumeEnergy(received);

                            int energyMaxStored = x.getMaxEnergyStored();
                            int energyStored = capacity.get();
                            int signalStrength = RedstoneHelpers.computeSignalStrength(energyStored, energyMaxStored);
                            boolean hasEnergyStored = energyStored > 0;

                            if (this.redstoneOutputPortState != null && this.redstoneOutputPortPosition != null) {
                                BlockState newState = this.redstoneOutputPortState.with(BlockStateProperties.POWERED, Boolean.valueOf(hasEnergyStored)).with(BlockStateProperties.POWER_0_15, signalStrength);
                                world.setBlockState(this.redstoneOutputPortPosition, newState, 3);
                                world.notifyNeighbors(pos, newState.getBlock());
                            }

                       *//*     BlockState state = world.getBlockState(pos);
                            world.notifyBlockUpdate(pos, state, state, 3);*//*
                            markDirty();
                        }
                    });
                }*/
            }
        });
    }

    @Override
    public void read(CompoundNBT tag) {

        this.currentBurnTimeTicks = tag.getInt("burntime");

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

        tag.putInt("burntime", this.currentBurnTimeTicks);

        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        CompoundNBT acidTankNBT = new CompoundNBT();

        acidTank.writeToNBT(acidTankNBT);

        handler.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("inv", compound);
        });

        energy.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("energy", compound);
        });

        tag.put("acid", acidTankNBT);

        tag.putInt("burntime", this.currentBurnTimeTicks);

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

        handler.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));
        energy.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));

        this.currentBurnTimeTicks = nbt.getInt("burntime");
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

        // This is all of the data we want to pass to the container which the screen will use!
        this.referenceHolder = new IntArraySupplierReferenceHolder(
                () -> this.acidTank.getCapacity(),
                () -> this.acidTank.getFluidAmount(),
                () -> this.energy.map(w -> w.getEnergyStored()).orElse(0),
                () -> this.energy.map(w -> w.getMaxEnergyStored()).orElse(0),
                () -> this.generatedEnergyPerTick);

        return new TerraReactorCoreContainer(worldId, world, pos, playerInventory, playerEntity, this.referenceHolder);
    }
}