package com.agrejus.netherendingenergy.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class RedstoneDetectorBlock extends Block {

    public RedstoneDetectorBlock(Block.Properties properties) {
        super(properties);
    }

    public void onRedstoneSignalChanged(@Nullable Direction poweredDirection, boolean isPowered, BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {

        if (worldIn.isRemote) {
            return;
        }

        Direction direction = state.get(BlockStateProperties.FACING);
        Direction redstoneSignalDirection = getRedstoneSignalDirection(worldIn, pos, direction);
        boolean isPowered = redstoneSignalDirection != null;

        this.onRedstoneSignalChanged(redstoneSignalDirection, isPowered, state, worldIn, pos, blockIn, fromPos);
    }

    private @Nullable
    Direction getRedstoneSignalDirection(World worldIn, BlockPos pos, Direction facing) {
        for (Direction direction : Direction.values()) {
            if (direction != facing && worldIn.isSidePowered(pos.offset(direction), direction)) {
                return direction;
            }
        }

        if (worldIn.isSidePowered(pos, Direction.DOWN)) {
            return Direction.DOWN;
        }

        BlockPos blockpos = pos.up();

        for (Direction direction1 : Direction.values()) {
            if (direction1 != Direction.DOWN && worldIn.isSidePowered(blockpos.offset(direction1), direction1)) {
                return direction1;
            }
        }

        return null;
    }
}
