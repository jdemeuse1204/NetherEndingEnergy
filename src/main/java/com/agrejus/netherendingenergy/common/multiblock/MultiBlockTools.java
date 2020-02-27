package com.agrejus.netherendingenergy.common.multiblock;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.interfaces.IMultiBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class MultiBlockTools {

    // Return true on success;
    public static boolean breakMultiblock(IMultiBlockType type, World world, BlockPos pos, TerraReactorPartIndex part) {
        BlockPos controllerPosition = type.getControllerPosition(pos, part);
        return type.tryUnFormMultiBlock(world, controllerPosition);
    }

    public static boolean formMultiblock(IMultiBlockType type, World world, BlockPos pos, PlayerEntity player) {

        if (type.isValidUnformedMultiBlock(world, pos) == false) {
            return false;
        }

        return type.tryFormMultiBlock(world, pos);
    }

    public static BlockPos getController(IMultiBlockType type, World world, BlockPos clickedBlockPos, TerraReactorPartIndex part) {

        return type.getControllerPosition(clickedBlockPos, part);
    }
}
