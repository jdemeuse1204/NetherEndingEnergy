package com.agrejus.netherendingenergy.blocks.terra.machinecasing;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.blocks.PartialModelFillBlock;
import com.agrejus.netherendingenergy.common.multiblock.MultiBlockTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class TerraMachineCasingBlock extends Block {

    public static final EnumProperty<TerraReactorPartIndex> FORMED = EnumProperty.<TerraReactorPartIndex>create("formed", TerraReactorPartIndex.class);

    public TerraMachineCasingBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.ANVIL)
                .hardnessAndResistance(2.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_MACHINE_CASING);
        setDefaultState(getStateContainer().getBaseState().with(FORMED, TerraReactorPartIndex.UNFORMED));
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (player.getHeldItem(hand).getItem() == Items.STICK) {
            TerraReactorCoreBlock.toggleMultiBlock(world, pos, state, player);
            return true;
        }
        // Only work if the block is formed
        if (state.getBlock() == ModBlocks.TERRA_MACHINE_CASING_BLOCK && state.get(FORMED) != TerraReactorPartIndex.UNFORMED) {
            // Find the controller
            BlockPos controllerPos = TerraReactorCoreBlock.getControllerPos(world, pos);
            if (controllerPos != null) {
                BlockState controllerState = world.getBlockState(controllerPos);
                return controllerState.getBlock().onBlockActivated(controllerState, world, controllerPos, player, hand, hit);
            }
        }
        return false;
    }


    @Override
    public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isRemote) {
            MultiBlockTools.breakMultiblock(TerraReactorMultiBlock.INSTANCE, world, pos);
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
