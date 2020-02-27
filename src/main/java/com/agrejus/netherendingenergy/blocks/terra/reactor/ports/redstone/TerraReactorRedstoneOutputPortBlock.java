package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.base.reactor.DirectionalReactorPartBlock;
import com.agrejus.netherendingenergy.blocks.base.reactor.ReactorPartBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorMultiBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import java.util.List;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock.FORMED;

public class TerraReactorRedstoneOutputPortBlock extends DirectionalReactorPartBlock {
    public TerraReactorRedstoneOutputPortBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0f)
                .lightValue(0), RegistryNames.TERRA_REACTOR_REDSTONE_OUTPUT_PORT, TerraReactorMultiBlock.INSTANCE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TerraReactorRedstoneOutputPortTile();
    }

    @Override
    protected List<IProperty<?>> getFillStateProperties() {
        List<IProperty<?>> result = super.getFillStateProperties();
        result.add(BlockStateProperties.POWERED);
        result.add(BlockStateProperties.POWER_0_15);
        return result;
    }

    @Override
    protected BlockState getDefaultReactorState() {
        return super.getDefaultReactorState().with(BlockStateProperties.POWERED, Boolean.valueOf(false)).with(BlockStateProperties.POWER_0_15, 0);
    }

    // Change to signal strength
    protected int getActiveSignal(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return state.get(BlockStateProperties.POWER_0_15);
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
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        if (!blockState.get(BlockStateProperties.POWERED)) {
            return 0;
        } else {
            return blockState.get(BlockStateProperties.FACING) == side ? this.getActiveSignal(blockAccess, pos, blockState) : 0;
        }
    }

    // call when power changes
    protected void notifyNeighbors(World worldIn, BlockPos pos, BlockState state) {
        Direction direction = state.get(BlockStateProperties.FACING);
        BlockPos blockpos = pos.offset(direction.getOpposite());
        if (net.minecraftforge.event.ForgeEventFactory.onNeighborNotify(worldIn, pos, worldIn.getBlockState(pos), java.util.EnumSet.of(direction.getOpposite()), false).isCanceled())
            return;
        worldIn.neighborChanged(blockpos, this, pos);
        worldIn.notifyNeighborsOfStateExcept(blockpos, this, direction);
    }
}
