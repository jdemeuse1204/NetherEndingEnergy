package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.NetherEndingEnergyConfig;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.IntArraySupplierReferenceHolder;
import com.agrejus.netherendingenergy.common.attributes.CustomFluidAttributes;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import com.agrejus.netherendingenergy.common.handlers.ReactorInventoryStackHandler;
import com.agrejus.netherendingenergy.common.models.MixerRecipe;
import com.agrejus.netherendingenergy.common.models.ProcessingUnit;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseType;
import com.agrejus.netherendingenergy.common.tank.MixableAcidFluidTank;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TerraMixerTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    // Export Config
    private static class ExportConfig {
        public static int drainAmountPerTick = 4000;
    }

    protected int energyUsePerTick = 160;
    protected float efficiency = .65f;

    protected ProcessingUnit destructibleItemProcessingUnit;

    protected int destructibleItemTotalTicks;
    protected int destructibleItemTicks;

    protected int processingUnitTotalTicks;
    protected int processingUnitTicks;

    private MixerRecipe currentRecipe;
    private IntArraySupplierReferenceHolder referenceHolder;

    private LazyOptional<IItemHandler> destructibleItemInventory = LazyOptional.of(this::createInventory);
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private LazyOptional<NEEFluidTank> inputTank = LazyOptional.of(this::createInputTank);
    private LazyOptional<MixableAcidFluidTank> outputTank = LazyOptional.of(this::createOutputTank);

    private static Map<Item, Integer> destructibleItems = NetherEndingEnergyConfig.Mixer().destructibleItems;
    private static ArrayList<MixerRecipe> recipes = NetherEndingEnergyConfig.Mixer().recipes;
    private static ArrayList<Direction> horizontalDirections = NetherEndingEnergyConfig.General().horizontalDirections;

    private IItemHandler createInventory() {
        return new ReactorInventoryStackHandler() {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

                Item item = stack.getItem();
                FluidStack fluidStack = inputTank.map(w -> w.getFluid()).orElse(FluidStack.EMPTY);

                // Don't let them put the wrong items in
                if (fluidStack.getAmount() > 0) {
                    return isItemValidForFluid(fluidStack.getFluid(), item);
                }

                return destructibleItems.containsKey(stack.getItem());
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

                if (isItemValid(slot, stack) == false) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private MixableAcidFluidTank createOutputTank() {
        return new MixableAcidFluidTank(32000) {
            @Override
            protected void onContentsChanged() {
                markDirty();
            }
        };
    }

    private NEEFluidTank createInputTank() {
        return new NEEFluidTank(32000) {
            @Override
            protected void onContentsChanged() {
                markDirty();
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {

                boolean isValid = super.isFluidValid(stack);

                // Only check inventory if fluid is valid
                if (isValid == true && stack.getAmount() > 0) {
                    Fluid fluid = stack.getFluid();
                    ItemStack burningItem = destructibleItemInventory.map(w -> ((ReactorInventoryStackHandler) w).getStackInBurningSlot()).orElse(ItemStack.EMPTY);

                    if (burningItem.isEmpty() == false) {
                        return isItemValidForFluid(fluid, burningItem.getItem());
                    }
                }

                return isValid;
            }
        };
    }

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(100000, 10000, this.energyUsePerTick);
    }

/*    private final IAnimationStateMachine asm;
    private final LazyOptional<IAnimationStateMachine> asmCap;*/

    public TerraMixerTile() {
        super(ModBlocks.TERRA_MIXER_TILE);

/*        if (FMLEnvironment.dist == Dist.CLIENT) {
            asm = ModelLoaderRegistry.loadASM(new ResourceLocation(NetherEndingEnergy.MODID, "asms/block/terra_mixer.json"), ImmutableMap.of());
            asmCap = LazyOptional.of(() -> asm);
        } else {
            asm = null;
            asmCap = LazyOptional.empty();
        }*/
    }

/*    @Override
    public boolean hasFastRenderer() {
        return true;
    }*/

    private boolean isItemValidForFluid(Fluid fluid, Item item) {
        ArrayList<MixerRecipe> availableRecipes = getRecipeByIngredientFluid(fluid);
        int size = availableRecipes.size();
        for (int i = 0; i < size; i++) {
            MixerRecipe availableRecipe = availableRecipes.get(i);
            if (availableRecipe.getItems().contains(item)) {
                return true;
            }
        }

        return false;
    }

    private ArrayList<MixerRecipe> getRecipeByIngredientFluid(Fluid fluid) {
        ArrayList<MixerRecipe> result = new ArrayList<>();
        int size = recipes.size();
        for (int i = 0; i < size; i++) {
            MixerRecipe recipe = recipes.get(i);
            if (recipe.getIngredientFluid() == fluid) {
                result.add(recipe);
            }
        }

        return result;
    }

    private MixerRecipe getRecipe(Fluid fluid, Item item) {
        int recipesSize = recipes.size();
        for (int i = 0; i < recipesSize; i++) {
            MixerRecipe recipe = recipes.get(i);
            if (recipe.getIngredientFluid() == fluid && recipe.getItems().contains(item)) {
                return recipe;
            }
        }

        return null;
    }

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        // Get our fluid
        FluidStack inputFluidStack = inputTank.map(w -> w.getFluid()).orElse(FluidStack.EMPTY);
        Fluid inputFluid = inputFluidStack.getFluid();

        // No acid, remove the recipe
        if (inputFluidStack.getAmount() == 0 || this.destructibleItemTicks == 0) {
            this.currentRecipe = null;
        }


        if (inputFluidStack.getAmount() > 0){
            this.destructibleItemInventory.ifPresent(w -> {
                ReactorInventoryStackHandler reactorInventory = (ReactorInventoryStackHandler) w;
                ItemStack burningStack = reactorInventory.getStackInBurningSlot();

                if (burningStack.isEmpty() == false) {
                    this.currentRecipe = getRecipe(inputFluid, burningStack.getItem());
                }
            });
        }

        if (this.destructibleItemTicks == 0) {

            // Make sure we have fluid and items
            // Do nothing until we have an acid in the input tank
            if (inputFluidStack.getAmount() > 0) {

                // get another item to destroy
                this.destructibleItemInventory.ifPresent(w -> {
                    ReactorInventoryStackHandler reactorInventory = (ReactorInventoryStackHandler) w;
                    ItemStack burningStack = reactorInventory.getStackInBurningSlot();

                    // If we have an item that was burning, lets extract it
                    if (burningStack.isEmpty() == false) {
                        // consume the item from the burning inventory because burn time ticks are 0
                        reactorInventory.extractBurningSlot(1, false);
                    }

                    ItemStack backlogStack = reactorInventory.getStackInBacklogSlot();

                    // Do we have items in the backlog
                    if (backlogStack.isEmpty() == false) {

                        int destructibleTime = destructibleItems.get(backlogStack.getItem());

                        if (destructibleTime > 0) {

                            // Consume the item
                            ItemStack extractedStack = reactorInventory.extractBacklogSlot(1, false);
                            reactorInventory.insertBurningSlot(extractedStack, false);

                            // Set the recipe
                            this.currentRecipe = getRecipe(inputFluid, extractedStack.getItem());

                            this.destructibleItemTicks = destructibleTime;
                            this.destructibleItemTotalTicks = destructibleTime;

                            markDirty();
                        }
                    }
                });
            }
        }

        if (this.currentRecipe != null) {

            // Make sure we have energy
            this.energy.ifPresent(w -> {
                if (this.energyUsePerTick == w.extractEnergy(this.energyUsePerTick, true)) {

                    // Start Processing
                    if (processingUnitTicks == 0) {

                        int itemUsagePerProcessingUnit = this.currentRecipe.getItemUsagePerProcessingUnit();
                        int acidUsagePerProcessingUnit = this.currentRecipe.getAcidUsagePerProcessingUnit();

                        if (this.destructibleItemTicks >= itemUsagePerProcessingUnit && inputFluidStack.getAmount() >= acidUsagePerProcessingUnit) {

                            // Consume Material
                            this.processingUnitTotalTicks = this.currentRecipe.getProcessingUnitTickLength();
                            this.destructibleItemTicks -= itemUsagePerProcessingUnit;
                            this.inputTank.ifPresent(x -> {

                                // Make sure we have enough input acid and energy
                                if(acidUsagePerProcessingUnit == x.drain(acidUsagePerProcessingUnit, IFluidHandler.FluidAction.SIMULATE).getAmount()) {

                                    x.drain(acidUsagePerProcessingUnit, IFluidHandler.FluidAction.EXECUTE);
                                    w.extractEnergy(this.energyUsePerTick, false);
                                    this.updateBlock();
                                    ++processingUnitTicks;
                                }
                            });
                        }
                    } else {
                        w.extractEnergy(this.energyUsePerTick, false);
                        this.updateBlock();
                        ++processingUnitTicks;
                    }

                    if (processingUnitTicks == this.currentRecipe.getProcessingUnitTickLength()) {

                        // Make unit of acid
                        int amountToFill = Math.round((float) this.currentRecipe.getResultFluidProcessingUnitAmount() * this.efficiency);
                        FluidStack result = getFillStack(amountToFill, this.currentRecipe.getResultFluid());

                        this.outputTank.ifPresent(x -> {

                            int amount = x.fill(result, IFluidHandler.FluidAction.SIMULATE);

                            if (amount == amountToFill) {
                                x.fill(result, IFluidHandler.FluidAction.EXECUTE);
                                //w.extractEnergy(this.energyUsePerTick, false);
                                this.updateBlock();
                            }
                        });

                        this.processingUnitTicks = 0;
                    }
                }
            });
        }

        this.sendOutFluid();
    }

    public void updateBlock() {
        BlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 3);
    }


    private void sendOutFluid() {
        this.outputTank.ifPresent(w -> {
            if (w.getFluidAmount() > 0) {

                TileEntity tileEntity = world.getTileEntity(pos.offset(Direction.DOWN));

                if (tileEntity != null) {
                    tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.UP).ifPresent(x -> {
                        FluidStack fluidStack = w.drain(ExportConfig.drainAmountPerTick, IFluidHandler.FluidAction.SIMULATE);
                        int amountToFill = x.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE);

                        if (amountToFill > 0) {
                            FluidStack fillFluidStack = w.drain(amountToFill, IFluidHandler.FluidAction.EXECUTE);
                            x.fill(fillFluidStack, IFluidHandler.FluidAction.EXECUTE);
                        }
                    });
                }
            }
        });
    }

    private FluidStack getFillStack(int amount, Fluid fluid) {

        FluidStack stackToFill = new FluidStack(fluid, amount);
        CustomFluidAttributes attributes = (CustomFluidAttributes) stackToFill.getFluid().getAttributes();

        CompoundNBT fluidTag = new CompoundNBT();

        // need to do sea level minus max divided by 16.  ex: (62-254)/16 = 12 is the max
        int spatialAmount = ReactorBaseConfig.INSTANCE.ComputeSpatial(world, pos);

        FluidHelpers.serializeCustomFluidAttributes(fluidTag, attributes, spatialAmount);

        stackToFill.setTag(fluidTag);

        return stackToFill;
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
        CompoundNBT nbt = packet.getNbtCompound();

        readNBT(nbt);
    }

    private void readNBT(CompoundNBT tag) {
        inputTank.ifPresent(w -> w.readFromNBT(tag.getCompound("input_tank")));
        outputTank.ifPresent(w -> w.readFromNBT(tag.getCompound("output_tank")));

        CompoundNBT invTag = tag.getCompound("inventory");

        destructibleItemInventory.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));

        CompoundNBT energyTag = tag.getCompound("energy");

        energy.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));

        this.destructibleItemTicks = tag.getInt("destructible_item_ticks");
        this.destructibleItemTotalTicks = tag.getInt("destructible_item_total_ticks");
        this.processingUnitTicks = tag.getInt("processing_unit_ticks");
        this.processingUnitTotalTicks = tag.getInt("processing_unit_total_ticks");
    }

    private void writeNBT(CompoundNBT tag) {
        CompoundNBT inputTankNBT = new CompoundNBT();
        CompoundNBT outputTankNBT = new CompoundNBT();

        inputTank.ifPresent(w -> w.writeToNBT(inputTankNBT));
        outputTank.ifPresent(w -> w.writeToNBT(outputTankNBT));

        tag.put("input_tank", inputTankNBT);
        tag.put("output_tank", outputTankNBT);

        destructibleItemInventory.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("inventory", compound);
        });

        energy.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("energy", compound);
        });

        tag.putInt("destructible_item_ticks", this.destructibleItemTicks);
        tag.putInt("destructible_item_total_ticks", this.destructibleItemTotalTicks);
        tag.putInt("processing_unit_ticks", this.processingUnitTicks);
        tag.putInt("processing_unit_total_ticks", this.processingUnitTotalTicks);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        //
        /*if (cap == CapabilityAnimation.ANIMATION_CAPABILITY) {
            return asmCap.cast();
            //return CapabilityAnimation.ANIMATION_CAPABILITY.orEmpty(cap, asmCap);
        }*/

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side == Direction.DOWN) {
            return this.inputTank.cast();
        }

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side != null && horizontalDirections.contains(side)) {
            return this.outputTank.cast();
        }

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return this.destructibleItemInventory.cast();
        }

        if (cap == CapabilityEnergy.ENERGY && side != null && side == Direction.DOWN) {
            return this.energy.cast();
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
        // World inside of the screen is the client world, we need data from the server world,
        // this accomplishes that
        this.referenceHolder = new IntArraySupplierReferenceHolder(
                () -> this.outputTank.map(w -> w.getCapacity()).orElse(0),
                () -> this.outputTank.map(w -> w.getFluidAmount()).orElse(0),
                () -> this.inputTank.map(w -> w.getCapacity()).orElse(0),
                () -> this.inputTank.map(w -> w.getFluidAmount()).orElse(0),
                () -> this.energy.map(w -> w.getEnergyStored()).orElse(0),
                () -> this.energy.map(w -> w.getMaxEnergyStored()).orElse(0),
                () -> this.inputTank.map(w -> w.getFluid().getFluid().getAttributes().getColor()).orElse(0),
                () -> this.destructibleItemTicks,
                () -> this.destructibleItemTotalTicks,
                () -> this.outputTank.map(w -> w.getFluid().getFluid().getAttributes().getColor()).orElse(0),
                () -> 1);

        return new TerraMixerContainer(worldId, world, pos, playerInventory, playerEntity, this.referenceHolder);
    }
}
