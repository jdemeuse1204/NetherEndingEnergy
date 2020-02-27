package com.agrejus.netherendingenergy.blocks.terra.ports.item;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class TerraReactorItemPortTile extends TileEntity {
    public TerraReactorItemPortTile() {
        super(ModBlocks.TERRA_REACTOR_ITEM_PORT_TILE);
    }
}
