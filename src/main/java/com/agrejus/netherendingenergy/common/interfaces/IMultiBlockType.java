package com.agrejus.netherendingenergy.common.interfaces;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.reactor.IReactorConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public interface IMultiBlockType {

    BlockPos getBlockFromControllerPosition(IWorld world, BlockPos controllerPosition, Block findBlock);
    BlockPos getControllerPosition(BlockPos clickedBlockPos, TerraReactorPartIndex part);
    BlockPos getRedstonePortPosition(IWorld world, BlockPos clickedBlockPos, TerraReactorPartIndex part);
    void toggleMultiBlock(IWorld world, BlockPos pos, BlockState state, PlayerEntity player);
    void unformBlock(IWorld world, BlockPos pos);
    void formBlock(IWorld world, BlockPos pos, TerraReactorPartIndex part);
    boolean isValidUnformedMultiBlock(IWorld world, BlockPos pos, IReactorConfig config);
    boolean isValidFormedMultiBlock(IWorld world, BlockPos pos, IReactorConfig config);
    boolean tryFormMultiBlock(IWorld world, BlockPos pos);
    boolean tryUnFormMultiBlock(IWorld world, BlockPos pos);
    List<Class> getMenuAccessibleBlockClasses();
    boolean isController(Class cls);
    Item getFormationItem();
}
