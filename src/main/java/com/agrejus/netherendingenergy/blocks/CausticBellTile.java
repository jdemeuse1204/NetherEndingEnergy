package com.agrejus.netherendingenergy.blocks;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

public class CausticBellTile extends TileEntity implements ITickableTileEntity {

    public CausticBellTile() {
        super(ModBlocks.CAUSTIC_BELL_TILE);
    }

    @Override
    public void tick() {

    }
}
