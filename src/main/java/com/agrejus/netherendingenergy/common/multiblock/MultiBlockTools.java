package com.agrejus.netherendingenergy.common.multiblock;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.interfaces.IMultiBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class MultiBlockTools {

    // Return true on success;
    public static boolean breakMultiblock(IMultiBlockType type, World world, BlockPos pos) {
        // First find the bottom left position of our multiblock
        BlockPos bottomLeft = type.getBottomLowerLeft(world, pos);
        if (bottomLeft != null) {
            if (!type.isValidFormedMultiBlock(world, bottomLeft)) {
                // Not a valid multiblock!
                return false;
            }

            for (int dx = 0; dx < type.getWidth(); dx++) {
                for (int dy = 0; dy < type.getHeight(); dy++) {
                    for (int dz = 0; dz < type.getDepth(); dz++) {
                        BlockPos p = bottomLeft.add(dx, dy, dz);
                        type.unformBlock(world, p);
                    }
                }
            }
            return true;
        }
        return false;
    }

    private int getStart(BlockPos pos, Direction direction) {

        //facing south
        //
        //	start x + 2
        //	subtract x
        //	add z
        //
        //facing west
        //
        //	start z + 2
        //	x becomes z subtract
        //	z becomes x add
        //
        //facing north
        //
        //	start x - 2
        //	add x
        //	subtract z
        //
        //facing east
        //
        //	start z - 2
        //	x becomes z add
        //	z becomes x subtract
        //
        //

        switch (direction) {
            case SOUTH:
                return pos.getX() + 2;
            case WEST:
                return pos.getZ() + 2;
            case NORTH:
                return pos.getX() + 2;
            case EAST:
                return pos.getX() + 2;
            default:
                return -1;
        }
    }

    public static boolean formMultiblock(IMultiBlockType type, World world, BlockPos pos, Direction playerEntityFacing) {

        int startX = pos.getX() + 2;
        int startY = pos.getY();
        int startZ = pos.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);

        for (TerraReactorPartIndex part : TerraReactorPartIndex.values()) {

            if (part == TerraReactorPartIndex.UNFORMED) {
                continue;
            }

            BlockPos position = start.add(-part.getDx(), part.getDy(), part.getDz());

            if (world.getBlockState(position).getBlock() == Blocks.AIR) {
                System.out.println(position);
                return false;
            }

            createMultiblock(type, world, position, part);
        }

        return true;
    }

    // pos is lower bottom coordinate of an unformed multiblock
    private static void createMultiblock(IMultiBlockType type, World world, BlockPos pos, TerraReactorPartIndex index) {
        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.with(TerraReactorCoreBlock.FORMED, index), 3);
    }
}
