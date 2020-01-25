package com.agrejus.netherendingenergy.blocks.terra.machinecasing;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.TerraReactorMultiblock;
import com.agrejus.netherendingenergy.blocks.terra.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreBlock;
import com.agrejus.netherendingenergy.common.multiblock.MultiBlockTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreBlock.FORMED;

public class TerraMachineCasingBlock extends Block {
    public TerraMachineCasingBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.ANVIL)
                .hardnessAndResistance(2.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_MACHINE_CASING);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (player.getHeldItem(hand).getItem() == Items.STICK) {
            TerraReactorCoreBlock.toggleMultiBlock(world, pos, state, player);
            return true;
        }
        // Only work if the block is formed
        if (state.getBlock() == ModBlocks.TERRA_MACHINE_CASING && state.get(FORMED) != TerraReactorPartIndex.UNFORMED) {
            // Find the controller
            BlockPos controllerPos = TerraReactorCoreBlock.getControllerPos(world, pos);
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
            MultiBlockTools.breakMultiblock(TerraReactorMultiblock.INSTANCE, world, pos);
        }
        super.onBlockHarvested(world, pos, state, player);
    }


    public boolean isFullCube(BlockState state) {
        if (state.get(FORMED) == TerraReactorPartIndex.UNFORMED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
    }
}
