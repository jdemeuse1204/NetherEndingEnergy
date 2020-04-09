package com.agrejus.netherendingenergy.client.gui.container;

import com.agrejus.netherendingenergy.common.container.InventoryContainerBase;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class RedstoneActivatableContainer<T extends TileEntity> extends InventoryContainerBase<T> {
    protected RedstoneActivatableContainer(@Nullable ContainerType<?> type, int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(type, id, world, pos, playerInventory, playerEntity);
    }

    public abstract RedstoneActivationType getRedstoneActivationType();

    public abstract void changeRedstoneActivationType(RedstoneActivationType type);
}
