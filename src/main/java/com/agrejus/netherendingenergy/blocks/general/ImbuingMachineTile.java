package com.agrejus.netherendingenergy.blocks.general;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraAcidCollectorContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class ImbuingMachineTile  extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public ImbuingMachineTile() {
        super(ModBlocks.IMBUING_MACHINE_TILE);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int worldId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ImbuingMachineContainer(worldId, world, pos, playerInventory, playerEntity);
    }

    @Override
    public void tick() {

    }
}
