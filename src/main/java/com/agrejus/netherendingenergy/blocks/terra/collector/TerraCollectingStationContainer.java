package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TerraCollectingStationContainer extends Container {

    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    private IIntArray tracking;

    // Exists on both server and client
    // Has slots of inventory and their links
    public TerraCollectingStationContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        this(id, world, pos, playerInventory, playerEntity, new IntArray(8));
    }

    public TerraCollectingStationContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity, IIntArray intArray) {
        super(ModBlocks.TERRA_COLLECTING_STATION_CONTAINER, id);

        // can we get around this call?
        // abstract furnace passes inventory in
        this.tileEntity = world.getTileEntity(pos);
        this.playerEntity = playerEntity;
        this.playerInventory = new InvWrapper(playerInventory);

        //addSlot(new SlotItemHandler(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, 0, 44, 39));

        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {
            addSlot(new SlotItemHandler(w, 0, 44, 39));
        });

        // where is the top left slot? This is the player inventory
        layoutPlayerInventorySlots(7, 84);

        tracking = intArray;

        trackIntArray(intArray);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, ModBlocks.TERRA_COLLECTING_STATION_BLOCK);
    }

    private void addSlot(IItemHandler hander, int index, int x, int y) {
        addSlot(new SlotItemHandler(hander, index, x, y));
    }

    private int addSlotRange(IItemHandler hander, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(hander, index, x, y);
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler hander, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(hander, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player Inventory
        addSlotBox(this.playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(this.playerInventory, 0, leftCol, topRow, 9, 18);
    }

    public int getOutputTankCapacity() {
        return this.tracking.get(0);
    }

    public int getOutputFluidAmount() {
        return this.tracking.get(1);
    }

    public int getInputTankCapacity() {
        return this.tracking.get(2);
    }

    public int getInputFluidAmount() {
        return this.tracking.get(3);
    }

    public int getTickCounter() {
        return this.tracking.get(4);
    }

    public int getTotalTicksToProcess() {
        return this.tracking.get(5);
    }

    public int getEnergyStored() { return this.tracking.get(6); }
    public int getMaxEnergyStored() { return this.tracking.get(7); }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        //  This is for when a player shift clicks on a certain index
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemStack = stack.copy();

            // index of 0 = diamond slot in the tile entity
            if (index == 0) {
                // Indicies 1-37 are player inventory indicies (all of them)
                if (!this.mergeItemStack(stack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (slot.isItemValid(stack)) {
                    // Try and merge diamond into burning slot
                    if (!this.mergeItemStack(stack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 28) {
                    // Merge into player inventory
                    if (!this.mergeItemStack(stack, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 37 && !this.mergeItemStack(stack, 1, 28, false)) {
                    // Merge into Hotbar
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
                slot.inventory.markDirty();
            }

            if (stack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }
        return itemStack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getProcessProgressionScaled() {

        int i = this.getTotalTicksToProcess() - this.getTickCounter();//this.field_217064_e.get(2); // cook Time
        int j = this.getTotalTicksToProcess();//this.field_217064_e.get(3); // cook Time Total

        // 24 is the width of the progression arrow for the GUI
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }
}
