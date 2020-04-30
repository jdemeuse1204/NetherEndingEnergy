package com.agrejus.netherendingenergy.common.factories;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ItemHandlerFactory {

    public static IItemHandler createEmptyBucketSlotInventory() {
        return ItemHandlerFactory.createEmptyBucketSlotInventory(1, null);
    }

    public static IItemHandler createEmptyBucketSlotInventory(Runnable onContentsChanged) {
        return ItemHandlerFactory.createEmptyBucketSlotInventory(1, onContentsChanged);
    }

    public static IItemHandler createEmptyBucketSlotInventory(int stackSize, Runnable onContentsChanged) {
        return new ItemStackHandler(stackSize) {

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == Items.BUCKET;
            }

            @Override
            protected void onContentsChanged(int slot) {
                if (onContentsChanged != null) {
                    onContentsChanged.run();
                }
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

    public static IItemHandler createResultSlotInventory() {
        return ItemHandlerFactory.createResultSlotInventory(1, null);
    }

    public static IItemHandler createResultSlotInventory(Runnable onContentsChanged) {
        return ItemHandlerFactory.createResultSlotInventory(1, onContentsChanged);
    }

    public static IItemHandler createResultSlotInventory(int stackSize, Runnable onContentsChanged) {
        return new ItemStackHandler(stackSize) {

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
            }

            @Override
            protected void onContentsChanged(int slot) {
                if (onContentsChanged != null) {
                    onContentsChanged.run();
                }
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
}
