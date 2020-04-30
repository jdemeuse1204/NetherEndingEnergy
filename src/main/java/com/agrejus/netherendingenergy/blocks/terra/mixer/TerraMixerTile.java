package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.NetherEndingEnergyConfig;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.IntArraySupplierReferenceHolder;
import com.agrejus.netherendingenergy.common.attributes.CustomFluidAttributes;
import com.agrejus.netherendingenergy.common.blocks.RedstoneEnergyTile;
import com.agrejus.netherendingenergy.common.enumeration.LevelType;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import com.agrejus.netherendingenergy.common.factories.EnergyStoreFactory;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import com.agrejus.netherendingenergy.common.handlers.ReactorInventoryStackHandler;
import com.agrejus.netherendingenergy.common.interfaces.IProcessingUnit;
import com.agrejus.netherendingenergy.common.models.InvertedProcessingUnit;
import com.agrejus.netherendingenergy.common.models.MixerRecipe;
import com.agrejus.netherendingenergy.common.models.ProcessingUnit;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.agrejus.netherendingenergy.common.tank.CapabilityTank;
import com.agrejus.netherendingenergy.common.tank.MixableAcidFluidTank;
import com.agrejus.netherendingenergy.network.NetherEndingEnergyNetworking;
import com.agrejus.netherendingenergy.network.PacketEmptyTank;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Map;

public class TerraMixerTile extends RedstoneEnergyTile implements INamedContainerProvider {

    // Export Config
    public static class ExportConfig {
        public static int drainAmountPerTick = 4000;
    }

    protected float efficiency = .65f;

    protected IProcessingUnit destructibleItemProcessingUnit = new InvertedProcessingUnit(0);
    protected IProcessingUnit processingUnit = new ProcessingUnit(0);

    private MixerRecipe currentRecipe;
    private IntArraySupplierReferenceHolder referenceHolder;
    private boolean isChopping = false;

    private IItemHandler acidInputSlotInventory = this.createAcidInputSlotInventory();
    private IItemHandler acidResultSlotInventory = this.createAcidResultSlotInventory();
    private IItemHandler outputSlotInventory = this.createOutputInventory();
    private IItemHandler destructibleItemInventory = this.createInventory();
    private MixableAcidFluidTank inputTank = this.createInputTank();
    private MixableAcidFluidTank outputTank = this.createOutputTank();

    public LazyOptional<IItemHandler> getAcidInputSlotInventory() {
        return LazyOptional.of(() -> this.acidInputSlotInventory);
    }

    public LazyOptional<IItemHandler> getAcidResultSlotInventory() {
        return LazyOptional.of(() -> this.acidResultSlotInventory);
    }

    public LazyOptional<IItemHandler> getOutputSlotInventory() {
        return LazyOptional.of(() -> this.outputSlotInventory);
    }

    public LazyOptional<MixableAcidFluidTank> getOutputTank() {
        return LazyOptional.of(() -> this.outputTank);
    }

    public LazyOptional<MixableAcidFluidTank> getInputTank() {
        return LazyOptional.of(() -> this.inputTank);
    }

    private static Map<Item, Integer> destructibleItems = NetherEndingEnergyConfig.Mixer().destructibleItems;
    private static ArrayList<MixerRecipe> recipes = NetherEndingEnergyConfig.Mixer().recipes;
    private static ArrayList<Direction> horizontalDirections = NetherEndingEnergyConfig.General().horizontalDirections;

    @Override
    protected CustomEnergyStorage createEnergyStore() {
        return EnergyStoreFactory.createEnergyStore(LevelType.TERRA);
    }

    private IItemHandler createAcidInputSlotInventory() {
        return new ItemStackHandler(1) {

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

                if (stack.getItem() instanceof BucketItem) {
                    BucketItem bucketItem = (BucketItem) stack.getItem();
                    int size = recipes.size();

                    for (int i = 0; i < size; i++) {
                        MixerRecipe recipe = recipes.get(i);
                        if (recipe.getIngredientFluid() == bucketItem.getFluid()) {
                            return true;
                        }
                    }
                }

                return false;
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
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

    private IItemHandler createAcidResultSlotInventory() {
        return new ItemStackHandler(1) {

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == Items.BUCKET;
            }

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
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

    private IItemHandler createOutputInventory() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private IItemHandler createInventory() {
        return new ReactorInventoryStackHandler() {

            @Override
            protected void onContentsChanged(int slot) {
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

                Item item = stack.getItem();
                FluidStack fluidStack = inputTank.getFluid();

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
                update();
                markDirty();
            }
        };
    }

    private MixableAcidFluidTank createInputTank() {
        return new MixableAcidFluidTank(32000) {
            @Override
            protected void onContentsChanged() {
                update();
                markDirty();
            }

            @Override
            public boolean isFluidValid(FluidStack stack) {

                boolean isValid = super.isFluidValid(stack);

                // Only check inventory if fluid is valid
                if (isValid == true && stack.getAmount() > 0) {
                    Fluid fluid = stack.getFluid();
                    ItemStack burningItem = ((ReactorInventoryStackHandler) destructibleItemInventory).getStackInBurningSlot();

                    if (burningItem.isEmpty() == false) {
                        return isItemValidForFluid(fluid, burningItem.getItem());
                    }
                }

                return isValid;
            }
        };
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

        redstoneActivationType = RedstoneActivationType.ALWAYS_ACTIVE;
    }

    public void voidInputTank() {
        int amount = inputTank.getFluidAmount();
        inputTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
        markDirty();

        if (world.isRemote) {
            NetherEndingEnergyNetworking.sendToServer(new PacketEmptyTank(pos, Direction.DOWN));
        }
    }

    public void voidOutputTank() {
        int amount = outputTank.getFluidAmount();
        outputTank.drain(amount, IFluidHandler.FluidAction.EXECUTE);
        markDirty();

        if (world.isRemote) {
            NetherEndingEnergyNetworking.sendToServer(new PacketEmptyTank(pos, Direction.EAST));
        }
    }

    public MixerRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }

    public void setCurrentRecipe(MixerRecipe recipe) {
        this.currentRecipe = recipe;
    }

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
    public void serverTick() {

        if (this.isEnabled() == false) {
            this.isChopping = false;
            return;
        }

        // Get our fluid
        FluidStack inputFluidStack = inputTank.getFluid();
        Fluid inputFluid = inputFluidStack.getFluid();

        this.resolveRecipe(inputFluidStack, inputFluid);
        this.tryConsumeItem(inputFluidStack, inputFluid);
        this.processRecipe(inputFluidStack);

        this.tryFillFromInputAcidSlot();
        this.tryFillFromOutputTank();
    }

    private void processRecipe(FluidStack inputFluidStack) {
        if (this.currentRecipe != null) {

            // Make sure we have energy
            if (this.energyStore.hasEnoughEnergyStored() == true) {

                // Start Processing
                if (this.processingUnit.getValue() == 0) {

                    int itemUsagePerProcessingUnit = this.currentRecipe.getItemUsagePerProcessingUnit();
                    int acidUsagePerProcessingUnit = this.currentRecipe.getAcidUsagePerProcessingUnit();

                    if (this.destructibleItemProcessingUnit.getValue() >= itemUsagePerProcessingUnit && inputFluidStack.getAmount() >= acidUsagePerProcessingUnit) {

                        // Consume Material
                        this.processingUnit.setTotal(this.currentRecipe.getProcessingUnitTickLength());
                        this.destructibleItemProcessingUnit.setNext(itemUsagePerProcessingUnit);
                        this.isChopping = true;

                        // Make sure we have enough input acid and energy
                        if (acidUsagePerProcessingUnit == this.inputTank.drain(acidUsagePerProcessingUnit, IFluidHandler.FluidAction.SIMULATE).getAmount()) {

                            this.inputTank.drain(acidUsagePerProcessingUnit, IFluidHandler.FluidAction.EXECUTE);
                            this.energyStore.consumeEnergyPerTick();
                            markUpdateAndDirty();
                            this.processingUnit.setNext();
                        }
                    }
                } else {
                    this.energyStore.consumeEnergyPerTick();
                    markUpdateAndDirty();
                    this.processingUnit.setNext();
                }

                if (this.processingUnit.getValue() == this.currentRecipe.getProcessingUnitTickLength()) {

                    // Make unit of acid
                    int amountToFill = Math.round((float) this.currentRecipe.getResultFluidProcessingUnitAmount() * this.efficiency);
                    FluidStack result = getFillStack(amountToFill, this.currentRecipe.getResultFluid());

                    int amount = this.outputTank.fill(result, IFluidHandler.FluidAction.SIMULATE);

                    if (amount == amountToFill) {
                        this.outputTank.fill(result, IFluidHandler.FluidAction.EXECUTE);
                        markUpdateAndDirty();
                    }

                    this.processingUnit.reset();
                }
            }
        }
    }

    private void resolveRecipe(FluidStack inputFluidStack, Fluid inputFluid) {

        if (inputFluidStack.getAmount() == 0) {
            this.processingUnit.reset();
            markUpdateAndDirty();
        }

        if ((inputFluidStack.getAmount() == 0 || this.destructibleItemProcessingUnit.getValue() == 0)) {
            this.isChopping = false;
            if (this.currentRecipe != null) {
                this.currentRecipe = null;
                markUpdateAndDirty();
            }
        }

        if (inputFluidStack.getAmount() > 0 && this.currentRecipe == null) {
            ReactorInventoryStackHandler reactorInventory = (ReactorInventoryStackHandler) destructibleItemInventory;
            ItemStack burningStack = reactorInventory.getStackInBurningSlot();

            if (burningStack.isEmpty() == false) {
                this.currentRecipe = getRecipe(inputFluid, burningStack.getItem());
                markUpdateAndDirty();
            }
        }
    }

    private void tryConsumeItem(FluidStack inputFluidStack, Fluid inputFluid) {
        if (this.destructibleItemProcessingUnit.getValue() == 0) {

            // Make sure we have fluid and items
            // Do nothing until we have an acid in the input tank
            if (inputFluidStack.getAmount() > 0) {

                // get another item to destroy
                ReactorInventoryStackHandler reactorInventory = (ReactorInventoryStackHandler) destructibleItemInventory;
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

                        this.destructibleItemProcessingUnit.setCurrent(destructibleTime);
                        this.destructibleItemProcessingUnit.setTotal(destructibleTime);
                        markUpdateAndDirty();
                    }
                }
            }
        }
    }

    private void tryFillFromOutputTank() {
        ItemStack stack = acidResultSlotInventory.getStackInSlot(0);

        if (stack.isEmpty() == false) {
            Item item = stack.getItem();
            if (item == Items.BUCKET) {
                FluidStack fluidInTank = outputTank.getFluidInTank(0);
                if (fluidInTank.getAmount() >= 1000) {
                    FluidStack drained = outputTank.drain(1000, IFluidHandler.FluidAction.SIMULATE);

                    if (drained.getAmount() == 1000) {

                        ItemStack outputItemStack = FluidUtil.getFilledBucket(drained);

                        ItemStack insertResult = outputSlotInventory.insertItem(0, outputItemStack, true);

                        if (insertResult.isEmpty() == true) {
                            outputTank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                            outputSlotInventory.insertItem(0, outputItemStack, false);
                            acidResultSlotInventory.extractItem(0, 1, false);
                            markUpdateAndDirty();
                        }
                    }
                }
            }
        }
    }

    private void tryFillFromInputAcidSlot() {
        ItemStack stack = this.acidInputSlotInventory.getStackInSlot(0);
        if (stack.isEmpty() == false) {
            Item item = stack.getItem();
            if (item instanceof BucketItem) {
                BucketItem bucketItem = (BucketItem) item;
                FluidStack fillStack = new FluidStack(bucketItem.getFluid(), 1000);
                int fillAmount = inputTank.fill(fillStack, IFluidHandler.FluidAction.SIMULATE);

                if (fillAmount == 1000) {

                    // Can we put item into output item stack?
                    ItemStack bucketItemStack = new ItemStack(Items.BUCKET, 1);
                    ItemStack insertStackResult = this.outputSlotInventory.insertItem(0, bucketItemStack, true);

                    // insertStackResult is the remaining items that were not inserted
                    if (insertStackResult.isEmpty() == true) {
                        inputTank.fill(fillStack, IFluidHandler.FluidAction.EXECUTE);

                        this.acidInputSlotInventory.extractItem(0, 1, false);
                        this.outputSlotInventory.insertItem(0, bucketItemStack, false);
                        markUpdateAndDirty();
                    }
                }
            }
        }
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
    protected void readNBT(CompoundNBT tag) {
        inputTank.readFromNBT(tag.getCompound("input_tank"));
        outputTank.readFromNBT(tag.getCompound("output_tank"));

        CompoundNBT acidInputSlotInventoryTag = tag.getCompound("acid_input_slot_inventory");
        CompoundNBT acidResultSlotInventoryTag = tag.getCompound("acid_result_slot_inventory");
        CompoundNBT outputSlotInventoryTag = tag.getCompound("output_slot_inventory");
        CompoundNBT invTag = tag.getCompound("inventory");

        ((INBTSerializable<CompoundNBT>) destructibleItemInventory).deserializeNBT(invTag);
        ((INBTSerializable<CompoundNBT>) acidInputSlotInventory).deserializeNBT(acidInputSlotInventoryTag);
        ((INBTSerializable<CompoundNBT>) acidResultSlotInventory).deserializeNBT(acidResultSlotInventoryTag);
        ((INBTSerializable<CompoundNBT>) outputSlotInventory).deserializeNBT(outputSlotInventoryTag);

        CompoundNBT energyTag = tag.getCompound("energy");

        ((INBTSerializable<CompoundNBT>) this.energyStore).deserializeNBT(energyTag);

        CompoundNBT currentRecipeTag = tag.getCompound("current_recipe");
        this.currentRecipe = null;
        if (currentRecipeTag.isEmpty() == false) {
            MixerRecipe recipe = new MixerRecipe();
            recipe.deserializeNBT(currentRecipeTag);
            this.currentRecipe = recipe;
        }

        this.redstoneActivationType = RedstoneActivationType.get(tag.getString("activation"));

        CompoundNBT destructibleItemTag = (CompoundNBT) tag.get("destructible_item_ticks");
        this.destructibleItemProcessingUnit.deserializeNBT(destructibleItemTag);

        CompoundNBT processingUnitTag = (CompoundNBT) tag.get("processing_unit_ticks");
        this.processingUnit.deserializeNBT(processingUnitTag);
    }

    @Override
    protected void writeNBT(CompoundNBT tag) {
        CompoundNBT inputTankNBT = new CompoundNBT();
        CompoundNBT outputTankNBT = new CompoundNBT();

        inputTank.writeToNBT(inputTankNBT);
        outputTank.writeToNBT(outputTankNBT);

        tag.put("input_tank", inputTankNBT);
        tag.put("output_tank", outputTankNBT);

        CompoundNBT acidInputSlotInventoryCompound = ((INBTSerializable<CompoundNBT>) acidInputSlotInventory).serializeNBT();
        tag.put("acid_input_slot_inventory", acidInputSlotInventoryCompound);

        CompoundNBT acdiResultSlotInventoryCompound = ((INBTSerializable<CompoundNBT>) acidResultSlotInventory).serializeNBT();
        tag.put("acid_result_slot_inventory", acdiResultSlotInventoryCompound);

        CompoundNBT outputSlotInventoryCompound = ((INBTSerializable<CompoundNBT>) outputSlotInventory).serializeNBT();
        tag.put("output_slot_inventory", outputSlotInventoryCompound);

        CompoundNBT destructibleItemInventoryCompound = ((INBTSerializable<CompoundNBT>) destructibleItemInventory).serializeNBT();
        tag.put("inventory", destructibleItemInventoryCompound);

        CompoundNBT energyCompound = ((INBTSerializable<CompoundNBT>) this.energyStore).serializeNBT();
        tag.put("energy", energyCompound);

        tag.put("destructible_item_ticks", this.destructibleItemProcessingUnit.serializeNBT());
        tag.put("processing_unit_ticks", this.processingUnit.serializeNBT());
        tag.putString("activation", this.redstoneActivationType.getName());

        if (this.currentRecipe != null) {
            CompoundNBT currentRecipeCompound = this.currentRecipe.serializeNBT();
            tag.put("current_recipe", currentRecipeCompound);
        } else {
            tag.put("current_recipe", new CompoundNBT());
        }
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        //
        /*if (cap == CapabilityAnimation.ANIMATION_CAPABILITY) {
            return asmCap.cast();
            //return CapabilityAnimation.ANIMATION_CAPABILITY.orEmpty(cap, asmCap);
        }*/

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) new CapabilityTank(this.outputTank, this.inputTank));
        }

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) this.destructibleItemInventory);
        }

        if (cap == CapabilityEnergy.ENERGY) {
            return LazyOptional.of(() -> (T) this.energyStore);
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
                () -> this.outputTank.getCapacity(),
                () -> this.outputTank.getFluidAmount(),
                () -> this.inputTank.getCapacity(),
                () -> this.inputTank.getFluidAmount(),
                () -> this.energyStore.getEnergyStored(),
                () -> this.energyStore.getMaxEnergyStored(),
                () -> this.inputTank.getFluid().getFluid().getAttributes().getColor(),
                () -> this.destructibleItemProcessingUnit.getValue(),
                () -> this.destructibleItemProcessingUnit.getTotal(),
                () -> this.outputTank.getFluid().getFluid().getAttributes().getColor(),
                () -> this.getEnergyUsedPerTick(),
                () -> this.destructibleItemInventory.getStackInSlot(1).getCount(),
                () -> this.processingUnit.getValue(),
                () -> this.processingUnit.getTotal(),
                () -> this.isChopping == true ? 1 : 0);

        return new TerraMixerContainer(worldId, world, pos, playerInventory, playerEntity, this.referenceHolder);
    }
}
