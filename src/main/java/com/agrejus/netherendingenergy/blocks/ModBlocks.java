package com.agrejus.netherendingenergy.blocks;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

public class ModBlocks {

    @ObjectHolder("netherendingenergy:firstblock")
    public static FirstBlock FIRSTBLOCK;

    @ObjectHolder("netherendingenergy:firsttile")
    public static FirstTile FIRSTTILE;

    @ObjectHolder("netherendingenergy:firsttile")
    public static TileEntityType<FirstBlockTile> FIRSTBLOCK_TILE;
}
