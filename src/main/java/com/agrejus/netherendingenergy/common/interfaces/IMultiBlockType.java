package com.agrejus.netherendingenergy.common.interfaces;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.reactor.IReactorConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public interface IMultiBlockType {

    @Nullable
    BlockPos getBottomLowerLeft(World world, BlockPos pos);
    BlockPos getBlockFromControllerPosition(World world, BlockPos controllerPosition, Block findBlock);
    BlockPos getControllerPosition(BlockPos clickedBlockPos, TerraReactorPartIndex part);
    BlockPos getRedstonePortPosition(World world, BlockPos clickedBlockPos, TerraReactorPartIndex part);
    void toggleMultiBlock(World world, BlockPos pos, BlockState state, PlayerEntity player);
    void unformBlock(World world, BlockPos pos);
    void formBlock(World world, BlockPos pos, TerraReactorPartIndex part);
    boolean isValidUnformedMultiBlock(World world, BlockPos pos);
    boolean isValidFormedMultiBlock(World world, BlockPos pos, IReactorConfig config);
    boolean tryFormMultiBlock(World world, BlockPos pos);
    boolean tryUnFormMultiBlock(World world, BlockPos pos);
    List<Class> getMenuAccessibleBlockClasses();
    boolean isController(Class cls);
    Item getFormationItem();
}
