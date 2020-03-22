package com.agrejus.netherendingenergy.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public abstract class DroppableInventoryBlock extends Block {
    public DroppableInventoryBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, ItemStack stack) {

        if (worldIn.isRemote) {
            return;
        }

        if (tileEntity != null) {
            LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);

            if (capability != null) {
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {

                    for (int i = 0; i < w.getSlots(); i++) {
                        ItemStack itemStack = w.getStackInSlot(i);
                        if (itemStack != null) {
                            w.extractItem(i, itemStack.getCount(), false);
                            Block.spawnAsEntity(worldIn, pos, itemStack);
                        }
                    }
                });
            }
        }

        super.harvestBlock(worldIn, player, pos, state, tileEntity, stack);
    }
}
