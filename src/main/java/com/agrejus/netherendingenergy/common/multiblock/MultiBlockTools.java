package com.agrejus.netherendingenergy.common.multiblock;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.interfaces.IReactorMultiBlockType;
import com.agrejus.netherendingenergy.common.reactor.IReactorConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class MultiBlockTools {

    // Return true on success;
    public static boolean breakMultiblock(IReactorMultiBlockType type, IWorld world, BlockPos pos, TerraReactorPartIndex part, IReactorConfig config) {
        BlockPos controllerPosition = type.getControllerPosition(pos, part);
        return type.tryUnFormMultiBlock(world, controllerPosition, config);
    }

    public static boolean formMultiblock(IReactorMultiBlockType type, IWorld world, BlockPos pos, PlayerEntity player, IReactorConfig config) {

        if (type.isValidUnformedMultiBlock(world, pos, player, config) == false) {
            return false;
        }

        return type.tryFormMultiBlock(world, pos, config);
    }
}
