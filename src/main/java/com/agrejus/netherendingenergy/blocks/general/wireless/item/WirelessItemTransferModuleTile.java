package com.agrejus.netherendingenergy.blocks.general.wireless.item;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleTile;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleTileBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class WirelessItemTransferModuleTile extends ModuleTile<IItemHandler> {

    private int transferRate = 4;

    public WirelessItemTransferModuleTile() {
        super(ModBlocks.WIRELESS_ITEM_TRANSFER_MODULE_TILE, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
    }

    @Override
    protected int getTransferTickRate() {
        return 20;
    }

    @Override
    protected void onTransfer(TileEntity pullingTileEntity, TileEntity receivingTileEntity, ModuleTileBase linkedModule, IItemHandler pulling, IItemHandler receiving) {

        int sourceSlots = pulling.getSlots();
        int receiveSlots = receiving.getSlots();

        for (int i = 0; i < sourceSlots; i++) {

            ItemStack stack = pulling.extractItem(i, 4, true);

            if (stack.isEmpty()) {
                continue;
            }

            boolean anyItemsTransferred = false;

            for (int j = 0; j < receiveSlots; j++) {
                ItemStack receivedStack = receiving.insertItem(j, stack, true);

                // cannot insert
                if (receivedStack.getCount() == stack.getCount()) {
                    continue;
                }

                if (receivedStack.isEmpty() == false) {
                    int resolvedCount = stack.getCount() - receivedStack.getCount();
                    stack.setCount(resolvedCount);
                }

                pulling.extractItem(i, stack.getCount(), false);
                receiving.insertItem(j, stack, false);
                anyItemsTransferred = true;
                break;
            }

            if (anyItemsTransferred == true) {
                break;
            }
        }
    }
}
