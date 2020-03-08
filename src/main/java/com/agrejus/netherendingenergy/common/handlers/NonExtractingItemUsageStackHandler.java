package com.agrejus.netherendingenergy.common.handlers;

import javafx.util.Pair;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

public class NonExtractingItemUsageStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<CompoundNBT> {

    private NonNullList<Pair<ItemStack, Integer>> stacks;

    public NonExtractingItemUsageStackHandler() {
        this(1);
    }

    public NonExtractingItemUsageStackHandler(int size) {
        stacks = NonNullList.withSize(size, new Pair<>(ItemStack.EMPTY, 0));
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        validateSlotIndex(slot);
        int usages = onGetUsagesForSlotWhenSet(slot, stack);
        this.stacks.set(slot, new Pair<>(stack, usages));
        onContentsChanged(slot);
    }

    public int onGetUsagesForSlotWhenSet(int slot, @Nonnull ItemStack stack) {
        return 0;
    }

    public void useOne(int slot) {
        validateSlotIndex(slot);
        Pair<ItemStack, Integer> slotStack = this.stacks.get(slot);

        if (slotStack.getValue() <= 1) {
            // destroy the stack
            this.stacks.set(slot, new Pair<>(ItemStack.EMPTY, 0));
            return;
        }

        int newUsageAmount = slotStack.getValue() - 1;
        this.stacks.set(slot, new Pair<>(slotStack.getKey(), newUsageAmount));
    }

    @Override
    public int getSlots() {
        return stacks.size();
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks.get(slot).getKey();
    }

    public Pair<ItemStack, Integer> getSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks.get(slot);
    }

    public int getUsagesLeftForSlot(int slot) {
        validateSlotIndex(slot);
        return this.stacks.get(slot).getValue();
    }

    @Override
    @Nonnull
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty())
            return ItemStack.EMPTY;

        if (!isItemValid(slot, stack))
            return stack;

        validateSlotIndex(slot);

        ItemStack existing = this.stacks.get(slot).getKey();

        int limit = getStackLimit(slot, stack);

        if (!existing.isEmpty()) {
            if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
                return stack;

            limit -= existing.getCount();
        }

        if (limit <= 0)
            return stack;

        boolean reachedLimit = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                ItemStack setStack = reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack;
                int usages = onGetUsagesForSlotWhenSet(slot, setStack);
                this.stacks.set(slot, new Pair<>(setStack, usages));
            } else {
                existing.grow(reachedLimit ? limit : stack.getCount());
            }
            onContentsChanged(slot);
        }

        return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY; // cannot extract
    }

    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public CompoundNBT serializeNBT() {
        ListNBT nbtTagList = new ListNBT();
        for (int i = 0; i < stacks.size(); i++) {
            if (!stacks.get(i).getKey().isEmpty()) {
                CompoundNBT itemTag = new CompoundNBT();
                itemTag.putInt("Slot", i);
                itemTag.putInt("Usages", stacks.get(i).getValue());
                stacks.get(i).getKey().write(itemTag);
                nbtTagList.add(itemTag);
            }
        }
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("Items", nbtTagList);
        nbt.putInt("Size", stacks.size());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setSize(nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : stacks.size());
        ListNBT tagList = nbt.getList("Items", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.size(); i++) {
            CompoundNBT itemTags = tagList.getCompound(i);
            int slot = itemTags.getInt("Slot");
            int usages = itemTags.getInt("Usages");

            if (slot >= 0 && slot < stacks.size()) {
                stacks.set(slot, new Pair<>(ItemStack.read(itemTags), usages));
            }
        }
        onLoad();
    }

    public void setSize(int size) {
        stacks = NonNullList.withSize(size, new Pair<>(ItemStack.EMPTY, 0));
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= stacks.size())
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
    }

    protected void onLoad() {

    }

    protected void onContentsChanged(int slot) {

    }

}
