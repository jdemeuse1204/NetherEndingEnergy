package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.Capabilities;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.IntArrayReferenceHolder;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TerraCollectingStationContainer extends Container {

    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;

    // Exists on both server and client
    // Has slots of inventory and their links
    public TerraCollectingStationContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ModBlocks.TERRA_COLLECTING_STATION_CONTAINER, id);

        this.tileEntity = world.getTileEntity(pos);
        this.playerEntity = playerEntity;
        this.playerInventory = new InvWrapper(playerInventory);

        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {
            addSlot(new SlotItemHandler(w, 0, 44, 39));
        });

        // where is the top left slot? This is the player inventory
        layoutPlayerInventorySlots(7, 84);

        trackIntArray(new IntArrayReferenceHolder(getTankAmount(TerraCollectingStationTile.OUTPUT_TANK_NAME), getTankAmount(TerraCollectingStationTile.INPUT_TANK_NAME)));
    }

    public TileEntity getTileEntity() {
        return this.tileEntity;
    }

    public int getTankAmount(String name) {
        LazyOptional<IFluidHandler[]> capability = tileEntity.getCapability(Capabilities.MULTI_FLUID_HANDLER_CAPABILITY);

        return capability.map(w -> {

            for (IFluidHandler tank : w) {
                if (((NEEFluidTank) tank).getName() == name) {
                    return tank.getFluidInTank(0).getAmount();
                }
            }

            return -1;
        }).orElse(0);
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

    public int getEnergy() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(w -> w.getEnergyStored()).orElse(0);
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
}
