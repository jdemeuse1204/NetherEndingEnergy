package com.agrejus.netherendingenergy.superchest;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.blocks.CausticBellTile;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.multiblock.MultiBlockTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSuperchest extends Block {

    public static final EnumProperty<SuperchestPartIndex> FORMED = EnumProperty.<SuperchestPartIndex>create("formed", SuperchestPartIndex.class);

    public static final ResourceLocation SUPERCHEST = new ResourceLocation(NetherEndingEnergy.MODID, "superchest");

    public BlockSuperchest() {
        super(Properties.create(Material.IRON).hardnessAndResistance(2.0f));
        setRegistryName(SUPERCHEST);
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
        return new TileSuperchest();
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    //@Override
    public boolean isFullCube(BlockState state) { return false; }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (player.getHeldItem(hand).getItem() == Items.STICK) {
            toggleMultiBlock(world, pos, state, player);
            return true;
        }
        // Only work if the block is formed
        if (state.getBlock() == ModBlocks.blockSuperchest && state.get(FORMED) != SuperchestPartIndex.UNFORMED) {
            return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
        } else {
            return false;
        }
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
        if (!world.isRemote) {
            MultiBlockTools.breakMultiblock(SuperchestMultiBlock.INSTANCE, world, pos);
        }
        super.harvestBlock(world, player, pos, state, te, stack);
    }



    public static void toggleMultiBlock(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // Form or break the multiblock
        if (!world.isRemote) {
            SuperchestPartIndex formed = state.get(FORMED);
            if (formed == SuperchestPartIndex.UNFORMED) {
                if (MultiBlockTools.formMultiblock(SuperchestMultiBlock.INSTANCE, world, pos)) {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Made a superchest!"), false);
                } else {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Could not form superchest!"), false);
                }
            } else {
                if (!MultiBlockTools.breakMultiblock(SuperchestMultiBlock.INSTANCE, world, pos)) {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Not a valid superchest!"), false);
                }
            }
        }
    }

    public static boolean isFormedSuperchestController(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.blockSuperchest && state.get(FORMED) != SuperchestPartIndex.UNFORMED;
    }

    @Nullable
    public static BlockPos getControllerPos(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == ModBlocks.blockSuperchest && state.get(BlockSuperchest.FORMED) != SuperchestPartIndex.UNFORMED) {
            return pos;
        }
        if (state.getBlock() == ModBlocks.blockSuperchestPart && state.get(BlockSuperchest.FORMED) != SuperchestPartIndex.UNFORMED) {
            SuperchestPartIndex index = state.get(BlockSuperchest.FORMED);
            // This index indicates where in the superblock this part is located. From this we can find the location of the bottom-left coordinate
            BlockPos bottomLeft = pos.add(-index.getDx(), -index.getDy(), -index.getDz());
            for (SuperchestPartIndex idx : SuperchestPartIndex.VALUES) {
                if (idx != SuperchestPartIndex.UNFORMED) {
                    BlockPos p = bottomLeft.add(idx.getDx(), idx.getDy(), idx.getDz());
                    if (isFormedSuperchestController(world, p)) {
                        return p;
                    }
                }
            }

        }
        return null;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
    }
}
