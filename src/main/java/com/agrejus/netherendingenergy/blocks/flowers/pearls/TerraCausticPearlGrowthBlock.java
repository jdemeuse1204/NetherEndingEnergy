package com.agrejus.netherendingenergy.blocks.flowers.pearls;

import com.agrejus.netherendingenergy.NetherEndingEnergyBlockStateProperties;
import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.interfaces.ICaustic;
import com.agrejus.netherendingenergy.items.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class TerraCausticPearlGrowthBlock extends Block implements IGrowable, ICaustic {
    public TerraCausticPearlGrowthBlock() {
        super(Properties.create(Material.PLANTS)
                .sound(SoundType.PLANT)
                .hardnessAndResistance(0.01f)
                .tickRandomly()
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_CAUSTIC_PEARL_GROWTH);
        setDefaultState(getStateContainer().getBaseState().with(NetherEndingEnergyBlockStateProperties.PEARL_GROWTH_0_6, 0));
    }

    @Override
    public boolean isSolid(BlockState p_200124_1_) {
        return false;
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState p_200123_1_, IBlockReader p_200123_2_, BlockPos p_200123_3_) {
        return true;
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity tileEntity, ItemStack stack) {
        if (world.isRemote) {
            return;
        }

        int stage = state.get(NetherEndingEnergyBlockStateProperties.PEARL_GROWTH_0_6);
        if (stage == 6) {
            spawnAsEntity(world, pos, new ItemStack(ModItems.TERRA_CAUSTIC_PEARL, 1));
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (world.isRemote || hand == Hand.OFF_HAND) {
            return false;
        }

        int stage = state.get(NetherEndingEnergyBlockStateProperties.PEARL_GROWTH_0_6);
        ++stage;
        if (stage == 7) {
            stage = 0;
        }

        world.setBlockState(pos, state.with(NetherEndingEnergyBlockStateProperties.PEARL_GROWTH_0_6, stage), 3);
        return false;
    }

    protected boolean isValidGround(BlockState state) {
        Block block = state.getBlock();
        return block == ModBlocks.CAUSTIC_ROOTS_BLOCK;
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.down();
        return this.isValidGround(worldIn.getBlockState(blockpos));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NetherEndingEnergyBlockStateProperties.PEARL_GROWTH_0_6);
    }

    public void tick(BlockState state, World world, BlockPos pos, Random random) {

        if (!world.isAreaLoaded(pos, 1) || world.isRemote) {
            return;
        }

        if (random.nextInt(21) == 0) {
            this.grow(world, random, pos, state);
        }
    }

    @Override
    public boolean canGrow(IBlockReader iBlockReader, BlockPos blockPos, BlockState blockState, boolean b) {
        return true;
    }

    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, BlockState blockState) {
        return false;
    }

    @Override
    public void grow(World world, Random random, BlockPos blockPos, BlockState blockState) {
        int stage = blockState.get(NetherEndingEnergyBlockStateProperties.PEARL_GROWTH_0_6);

        if (stage >= 6) {
            return;
        }

        world.setBlockState(blockPos, blockState.with(NetherEndingEnergyBlockStateProperties.PEARL_GROWTH_0_6, ++stage), 3);
    }
}
