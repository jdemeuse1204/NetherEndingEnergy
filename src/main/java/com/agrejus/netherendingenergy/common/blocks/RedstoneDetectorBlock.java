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

    public void onRedstoneSignalChanged(@Nullable Direction directionChanged, boolean isPowered, int power, BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {

        if (worldIn.isRemote) {
            return;
        }

        Direction changingBlockDirection = getDirectionOfChangingBlock(pos, fromPos);
        int power = worldIn.getRedstonePower(fromPos, changingBlockDirection);

        this.onRedstoneSignalChanged(changingBlockDirection, power > 0, power, state, worldIn, pos, blockIn, fromPos);
    }

    private @Nullable Direction getDirectionOfChangingBlock(BlockPos pos, BlockPos fromPos) {
        int x = fromPos.getX();
        int y = fromPos.getY();
        int z = fromPos.getZ();

        for (Direction direction : Direction.values()) {
            BlockPos offset = pos.offset(direction);

            if (x == offset.getX() && y == offset.getY() && z == offset.getZ()) {
                return direction;
            }
        }

        return null;
    }
}
