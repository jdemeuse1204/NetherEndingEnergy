package com.agrejus.netherendingenergy.common.container;

import com.agrejus.netherendingenergy.client.gui.container.DestructibleContainer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;

public abstract class InventoryContainerBase<T extends TileEntity> extends Container {

    protected T tileEntity;
    protected PlayerEntity playerEntity;
    protected IItemHandler playerInventory;
    protected BlockPos pos;
    protected World world;
    private ArrayList<Integer> playerInventorySlotIndices = new ArrayList<>();

    protected InventoryContainerBase(@Nullable ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(type, id);
        this.tileEntity = (T) world.getTileEntity(pos);
        this.playerEntity = playerEntity;
        this.playerInventory = new InvWrapper(playerInventory);
        this.pos = pos;
        this.world = world;
    }

    public void removePlayerInventory() {
        this.inventorySlots.clear();
    }

    protected void addSlot(IItemHandler handler, int index, int x, int y) {
        addSlot(new SlotItemHandler(handler, index, x, y));
    }

    protected int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(handler, index, x, y);
            x += dx;
            index++;
        }
        return index;
    }

    protected int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected int addPlayerInventorySlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            this.playerInventorySlotIndices.add(j);
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    protected void layoutPlayerInventory(int leftCol, int topRow) {
        // Player Inventory
        addPlayerInventorySlotBox(this.playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);
    }

    protected void layoutPlayerHotbar(int leftCol, int topRow) {
        // Hotbar
        topRow += 58;
        addSlotRange(this.playerInventory, 0, leftCol, topRow, 9, 18);
    }

    protected void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Hotbar
        layoutPlayerHotbar(leftCol, topRow);

        // Player Inventory
        layoutPlayerInventory(leftCol, topRow);
    }

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

    protected int getProcessProgressionScaled(int totalTicksToProcess, int currentTicks, int imageSliceWidth) {

        int i = totalTicksToProcess - currentTicks;
        int j = totalTicksToProcess;

        // 24 is the width of the progression arrow for the GUI
        return j != 0 && i != 0 ? i * imageSliceWidth / j : 0;
    }

    public boolean canInteractWith(Block block) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, block);
    }
}
