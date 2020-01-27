package com.agrejus.netherendingenergy.blocks.terra.reactor;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.interfaces.IMultiBlockType;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TerraReactorMultiBlock implements IMultiBlockType {

    public static TerraReactorMultiBlock INSTANCE = new TerraReactorMultiBlock();

    private boolean isBlockPart(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.TERRA_REACTOR_CORE_BLOCK || state.getBlock() == ModBlocks.TERRA_MACHINE_CASING;
    }

    private boolean isValidFormedBlockPart(World world, BlockPos pos, int dx, int dy, int dz) {
        BlockPos p = pos;
        if (isFormedSuperchestController(world, p)) {
            TerraReactorPartIndex index = world.getBlockState(p).get(TerraReactorCoreBlock.FORMED);
            return index == TerraReactorPartIndex.getIndex(dx, dy, dz);
        } else if (isFormedSuperchestPart(world, p)) {
            TerraReactorPartIndex index = world.getBlockState(p).get(TerraReactorCoreBlock.FORMED);
            return index == TerraReactorPartIndex.getIndex(dx, dy, dz);
        } else {
            // We can already stop here
            return false;
        }
    }

    private boolean isValidUnformedBlockPart(World world, BlockPos pos, int dx, int dy, int dz) {
        if (isUnformedSuperchestController(world, pos)) {
            return true;
        } else if (isUnformedSuperchestPart(world, pos)) {
            // We can already stop here
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public BlockPos getBottomLowerLeft(World world, BlockPos pos) {
        if (isBlockPart(world, pos)) {
            BlockState state = world.getBlockState(pos);
            TerraReactorPartIndex index = state.get(TerraReactorCoreBlock.FORMED);
            return pos.add(-index.getDx(), -index.getDy(), -index.getDz());
        } else {
            return null;
        }
    }

    @Override
    public void unformBlock(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.with(TerraReactorCoreBlock.FORMED, TerraReactorPartIndex.UNFORMED), 3);
    }

    @Override
    public void formBlock(World world, BlockPos pos, int dx, int dy, int dz) {
        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.with(TerraReactorCoreBlock.FORMED, TerraReactorPartIndex.getIndex(dx, dy, dz)), 3);
    }

    @Override
    public boolean isValidUnformedMultiBlock(World world, BlockPos pos) {
        int cntSuper = 0;
        for (int dx = 0 ; dx < getWidth() ; dx++) {
            for (int dy = 0 ; dy < getHeight() ; dy++) {
                for (int dz = 0 ; dz < getDepth() ; dz++) {
                    BlockPos p = pos.add(dx, dy, dz);
                    if (!isValidUnformedBlockPart(world, p, dx, dy, dz)) {
                        return false;
                    }
                    if (world.getBlockState(p).getBlock() == ModBlocks.TERRA_REACTOR_CORE_BLOCK) {
                        cntSuper++;
                    }
                }
            }
        }
        return cntSuper == 1;
    }

    @Override
    public boolean isValidFormedMultiBlock(World world, BlockPos pos) {
        int cntSuper = 0;
        for (int dx = 0; dx < getWidth(); dx++) {
            for (int dy = 0; dy < getHeight(); dy++) {
                for (int dz = 0; dz < getDepth(); dz++) {
                    BlockPos p = pos.add(dx, dy, dz);
                    if (!isValidFormedBlockPart(world, p, dx, dy, dz)) {
                        return false;
                    }
                    if (world.getBlockState(p).getBlock() == ModBlocks.TERRA_REACTOR_CORE_BLOCK) {
                        cntSuper++;
                    }
                }
            }
        }
        return cntSuper == 1;
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Override
    public int getDepth() {
        return 2;
    }

    private static boolean isUnformedSuperchestController(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.TERRA_REACTOR_CORE_BLOCK && state.get(TerraReactorCoreBlock.FORMED) == TerraReactorPartIndex.UNFORMED;
    }

    public static boolean isFormedSuperchestController(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.TERRA_REACTOR_CORE_BLOCK && state.get(TerraReactorCoreBlock.FORMED) != TerraReactorPartIndex.UNFORMED;
    }

    private static boolean isUnformedSuperchestPart(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.TERRA_MACHINE_CASING && state.get(TerraReactorCoreBlock.FORMED) == TerraReactorPartIndex.UNFORMED;
    }

    private static boolean isFormedSuperchestPart(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.TERRA_MACHINE_CASING && state.get(TerraReactorCoreBlock.FORMED) != TerraReactorPartIndex.UNFORMED;
    }
}
