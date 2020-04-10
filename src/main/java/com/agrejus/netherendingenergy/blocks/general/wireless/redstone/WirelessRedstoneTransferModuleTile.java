package com.agrejus.netherendingenergy.blocks.general.wireless.redstone;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleTileBase;

public class WirelessRedstoneTransferModuleTile  extends ModuleTileBase {

    public WirelessRedstoneTransferModuleTile() {
        super(ModBlocks.WIRELESS_REDSTONE_TRANSFER_MODULE_TILE);
    }

    @Override
    protected int getTransferTickRate() {
        return 0; // 0 = disabled
    }
}
