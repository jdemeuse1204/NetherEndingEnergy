package com.agrejus.netherendingenergy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class BlockTileGui extends BlockTile implements IBlockGui {

    public BlockTileGui(Block.Properties properties, Supplier<CyclopsTileEntity> tileEntitySupplier) {
        super(properties, tileEntitySupplier);
    }

    @Override
    public boolean onBlockActivated(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (super.onBlockActivated(blockState, world, blockPos, player, hand, rayTraceResult)) {
            return true;
        }
        return IBlockGui.onBlockActivatedHook(this, this::getContainer, blockState, world, blockPos, player, hand, rayTraceResult);
    }

}
