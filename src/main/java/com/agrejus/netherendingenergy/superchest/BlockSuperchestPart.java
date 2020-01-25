package com.agrejus.netherendingenergy.superchest;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.multiblock.MultiBlockTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.agrejus.netherendingenergy.superchest.BlockSuperchest.FORMED;

public class BlockSuperchestPart extends Block {

    public static final ResourceLocation SUPERCHEST_PART = new ResourceLocation(NetherEndingEnergy.MODID, "superchest_part");

    public BlockSuperchestPart() {
        super(Properties.create(Material.IRON).hardnessAndResistance(2.0f));
        setRegistryName(SUPERCHEST_PART);
        // @todo 1.13
//        setHarvestLevel("axe", 1);

        setDefaultState(getStateContainer().getBaseState().with(FORMED, SuperchestPartIndex.UNFORMED));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileSuperchestPart();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (player.getHeldItem(hand).getItem() == Items.STICK) {
            BlockSuperchest.toggleMultiBlock(world, pos, state, player);
            return true;
        }
        // Only work if the block is formed
        if (state.getBlock() == ModBlocks.blockSuperchestPart && state.get(FORMED) != SuperchestPartIndex.UNFORMED) {
            // Find the controller
            BlockPos controllerPos = BlockSuperchest.getControllerPos(world, pos);
            if (controllerPos != null) {
                BlockState controllerState = world.getBlockState(controllerPos);
                return controllerState.getBlock().onBlockActivated(controllerState, world, controllerPos, player, hand, rayTraceResult);
            }
        }
        return false;
    }


    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isRemote) {
            MultiBlockTools.breakMultiblock(SuperchestMultiBlock.INSTANCE, world, pos);
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    public boolean isFullCube(BlockState state) {
        if (state.get(FORMED) == SuperchestPartIndex.UNFORMED) {
            return true;//super.isFullCube(state);
        } else {
            return false;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
    }
}
