package com.agrejus.netherendingenergy.common.interfaces;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.models.BlockInformation;
import com.agrejus.netherendingenergy.common.models.TopLeftPos;
import com.agrejus.netherendingenergy.common.reactor.IReactorConfig;
import com.agrejus.netherendingenergy.common.reactor.ReactorSlotType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.List;
import java.util.Map;

public interface IReactorMultiBlockType {

    Map<ReactorSlotType, TopLeftPos> getSlotLocations();
    BlockInformation getBlockFromControllerPosition(IWorld world, BlockPos controllerPosition, TerraReactorPartIndex part);
    BlockPos getBlockFromControllerPosition(IWorld world, BlockPos controllerPosition, Block findBlock, IReactorConfig config);
    BlockPos getControllerPosition(BlockPos clickedBlockPos, TerraReactorPartIndex part);
    BlockPos getRedstonePortPosition(IWorld world, BlockPos clickedBlockPos, TerraReactorPartIndex part, IReactorConfig config);
    void toggleMultiBlock(IWorld world, BlockPos pos, BlockState state, PlayerEntity player);
    void unformBlock(IWorld world, BlockPos pos);
    void formBlock(IWorld world, BlockPos pos, BlockState state, TerraReactorPartIndex part, BlockPos core);
    boolean isValidUnformedMultiBlock(IWorld world, BlockPos pos, PlayerEntity player, IReactorConfig config);
    boolean isValidFormedMultiBlock(IWorld world, BlockPos pos, IReactorConfig config);
    boolean tryFormMultiBlock(IWorld world, BlockPos pos, IReactorConfig config);
    boolean tryUnFormMultiBlock(IWorld world, BlockPos pos, IReactorConfig config);
    List<Block> getMenuAccessibleBlockClasses();
    boolean isController(Block block);
    Item getFormationItem();
}
