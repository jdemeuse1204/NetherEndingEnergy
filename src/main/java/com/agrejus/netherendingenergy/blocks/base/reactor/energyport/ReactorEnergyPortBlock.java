package com.agrejus.netherendingenergy.blocks.base.reactor.energyport;

import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ReactorEnergyPortBlock extends Block {

    public static final BooleanProperty ON = BooleanProperty.create("on");

    public ReactorEnergyPortBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(.01f)
                .lightValue(0));
        setRegistryName(RegistryNames.REACTOR_REDSTONE_PORT);
        setDefaultState(this.stateContainer.getBaseState().with(ON, Boolean.valueOf(false)));
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {

        if (worldIn.isRemote) {
            return;
        }

        Direction direction = state.get(BlockStateProperties.FACING);
        if (this.hasRedstoneSignal(worldIn, pos, direction)) {
            worldIn.setBlockState(pos, state.with(ON, Boolean.valueOf(true)), 2);
            return;
        }

        worldIn.setBlockState(pos, state.with(ON, Boolean.valueOf(false)), 2);
    }

    private boolean hasRedstoneSignal(World worldIn, BlockPos pos, Direction facing) {
        for (Direction direction : Direction.values()) {
            if (direction != facing && worldIn.isSidePowered(pos.offset(direction), direction)) {
                return true;
            }
        }

        if (worldIn.isSidePowered(pos, Direction.DOWN)) {
            return true;
        }

        BlockPos blockpos = pos.up();

        for (Direction direction1 : Direction.values()) {
            if (direction1 != Direction.DOWN && worldIn.isSidePowered(blockpos.offset(direction1), direction1)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ReactorEnergyPortTile();
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer != null) {
            world.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, placer)), 2);
        }
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity placer) {
        return Direction.getFacingFromVector((float) (placer.posX - clickedBlock.getX()), (float) (placer.posY - clickedBlock.getY()), (float) (placer.posZ - clickedBlock.getZ()));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, ON);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }
}
