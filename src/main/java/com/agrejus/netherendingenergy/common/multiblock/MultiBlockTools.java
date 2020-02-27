package com.agrejus.netherendingenergy.common.multiblock;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.interfaces.IMultiBlockType;
import com.agrejus.netherendingenergy.common.reactor.IReactorConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class MultiBlockTools {

    // Return true on success;
    public static boolean breakMultiblock(IMultiBlockType type, IWorld world, BlockPos pos, TerraReactorPartIndex part) {
        BlockPos controllerPosition = type.getControllerPosition(pos, part);
        return type.tryUnFormMultiBlock(world, controllerPosition);
    }

    public static boolean formMultiblock(IMultiBlockType type, IWorld world, BlockPos pos, IReactorConfig config) {

        if (type.isValidUnformedMultiBlock(world, pos, config) == false) {
            return false;
        }

        return type.tryFormMultiBlock(world, pos);
    }

    public static BlockPos getController(IMultiBlockType type, IWorld world, BlockPos clickedBlockPos, TerraReactorPartIndex part) {

        return type.getControllerPosition(clickedBlockPos, part);
    }
}
