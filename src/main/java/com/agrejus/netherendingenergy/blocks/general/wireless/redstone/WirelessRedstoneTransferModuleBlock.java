package com.agrejus.netherendingenergy.blocks.general.wireless.redstone;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WirelessRedstoneTransferModuleBlock  extends ModuleBlock {

    public WirelessRedstoneTransferModuleBlock() {
        super(RegistryNames.WIRELESS_REDSTONE_TRANSFER_MODULE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WirelessRedstoneTransferModuleTile();
    }

    public void onRedstoneSignalChanged(@Nullable Direction directionChanged, boolean isPowered, int power, BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return super.getStrongPower(blockState, blockAccess, pos, side);
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return super.getWeakPower(blockState, blockAccess, pos, side);
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {

        if (worldIn.isRemote) {
            return;
        }

        Direction changingBlockDirection = getDirectionOfChangingBlock(pos, fromPos);
        int power = worldIn.getRedstonePower(fromPos, changingBlockDirection);

        this.onRedstoneSignalChanged(changingBlockDirection, power > 0, power, state, worldIn, pos, blockIn, fromPos);
    }

    private @Nullable
    Direction getDirectionOfChangingBlock(BlockPos pos, BlockPos fromPos) {
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
