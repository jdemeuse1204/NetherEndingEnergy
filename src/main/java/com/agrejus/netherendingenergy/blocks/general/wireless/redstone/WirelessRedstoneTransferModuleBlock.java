package com.agrejus.netherendingenergy.blocks.general.wireless.redstone;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class WirelessRedstoneTransferModuleBlock extends ModuleBlock {

    public WirelessRedstoneTransferModuleBlock() {
        super(RegistryNames.WIRELESS_REDSTONE_TRANSFER_MODULE);
        setDefaultState(getStateContainer().getBaseState().with(BlockStateProperties.FACING, Direction.NORTH).with(BlockStateProperties.POWERED, Boolean.valueOf(false)).with(BlockStateProperties.POWER_0_15, 0));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WirelessRedstoneTransferModuleTile();
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return side != null && side == state.get(BlockStateProperties.FACING);
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return this.getWeakPower(blockState, blockAccess, pos, side);
    }

    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.get(BlockStateProperties.POWER_0_15);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED, BlockStateProperties.POWER_0_15);
    }
}
