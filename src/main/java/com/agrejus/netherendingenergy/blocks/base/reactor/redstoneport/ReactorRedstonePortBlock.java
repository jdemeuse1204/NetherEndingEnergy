package com.agrejus.netherendingenergy.blocks.base.reactor.redstoneport;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.blocks.RedstoneDetectorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

// RENAME TO INPUT PORT, WE ARE HAVING AN OUTPUT PORT AS WELL
public class ReactorRedstonePortBlock extends RedstoneDetectorBlock {

    public static final EnumProperty<TerraReactorPartIndex> FORMED = EnumProperty.<TerraReactorPartIndex>create("formed", TerraReactorPartIndex.class);
    public static final BooleanProperty ON = BooleanProperty.create("on");

    public ReactorRedstonePortBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(.01f)
                .lightValue(0));
        setRegistryName(RegistryNames.REACTOR_REDSTONE_PORT);
        setDefaultState(getStateContainer().getBaseState().with(FORMED, TerraReactorPartIndex.UNFORMED).with(ON, Boolean.valueOf(false)));
    }

    @Override
    public void onRedstoneSignalChanged(@Nullable Direction poweredDirection, boolean isPowered, BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (isPowered == true) {
            world.setBlockState(pos, state.with(ON, Boolean.valueOf(true)), 2);
            return;
        }
        world.setBlockState(pos, state.with(ON, Boolean.valueOf(false)), 2);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ReactorRedstonePortTile();
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
        builder.add(BlockStateProperties.FACING, ON, FORMED);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return false;
    }
}
