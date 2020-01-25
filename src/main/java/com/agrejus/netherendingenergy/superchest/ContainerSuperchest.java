package com.agrejus.netherendingenergy.superchest;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerSuperchest extends Container {

    private TileEntity te;
    private PlayerEntity playerEntity;

    public ContainerSuperchest(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ModBlocks.TERRA_VAPOR_COLLECTOR_CONTAINER, id);

        this.te = world.getTileEntity(pos);
        this.playerEntity = playerEntity;

/*        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return getVapor();
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityVapor.VAPOR).ifPresent(w -> ((CustomVaporStorage)w).setVapor(value));
            }
        });*/
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // Slots for the main inventory
/*        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 10 + col * 18;
                int y = row * 18 + 70;
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }*/

        // Slots for the hotbar
/*        for (int row = 0; row < 9; ++row) {
            int x = 10 + row * 18;
            int y = 58 + 70;
            this.addSlot(new Slot(playerInventory, row, x, y));
        }*/
    }

    private void addOwnSlots() {
/*        this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(itemHandler -> {

            int slotIndex = 0;
            for (int row = 0; row < 3; ++row) {
                for (int col = 0; col < 9; ++col) {
                    int x = 10 + col * 18;
                    int y = row * 18 + 8;
                    this.addSlot(new SlotItemHandler(itemHandler, slotIndex++, x, y));
                }
            }
        });*/
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
/*        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < TileFastFurnace.SIZE) {
                if (!this.mergeItemStack(itemstack1, TileFastFurnace.SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, TileFastFurnace.SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }*/

        return itemstack;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(te.getWorld(), te.getPos()), playerEntity, ModBlocks.blockSuperchest);
    }
}
