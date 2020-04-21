package com.agrejus.netherendingenergy.common.handlers;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class DroppableItemStackHandler extends ItemStackHandler {
    public void dropItems(final World world, final double x, final double y, final double z) {
        for (int i = 0; i < this.stacks.size(); i++) {
            if (this.stacks.get(i).isEmpty()) {
                continue;
            }

            final ItemEntity itemEntity = new ItemEntity(world, x, y, z, this.stacks.get(i));
            itemEntity.setDefaultPickupDelay();
            //world..spawnEntity(itemEntity);
            //ItemStack.

            this.stacks.set(i, ItemStack.EMPTY);
        }
    }
}
