package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.base.reactor.ReactorPartBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class TerraReactorCoreBlock extends ReactorPartBlock {

    public static final EnumProperty<TerraReactorPartIndex> FORMED = EnumProperty.<TerraReactorPartIndex>create("formed", TerraReactorPartIndex.class);

    public TerraReactorCoreBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(.01f)
                .lightValue(0), RegistryNames.TERRA_REACTOR_CORE, TerraReactorReactorMultiBlock.INSTANCE);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (world.isRemote){
            return true;
        }

        if (player.isSneaking() && player.getHeldItem(hand).getItem() == this.Type.getFormationItem()) {
            this.Type.tryFormMultiBlock(world, pos, state, player);
            return true;
        }

        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(FORMED) == TerraReactorPartIndex.UNFORMED ? 0 : 16;
    }

    // When formed, make block invisible/not clickable
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return state.get(FORMED) != TerraReactorPartIndex.UNFORMED ? Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D) : super.getShape(state, worldIn, pos, context);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TerraReactorCoreTile();
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }
}