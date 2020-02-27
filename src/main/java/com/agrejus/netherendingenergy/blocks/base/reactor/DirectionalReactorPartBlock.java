package com.agrejus.netherendingenergy.blocks.base.reactor;

import com.agrejus.netherendingenergy.common.interfaces.IMultiBlockType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import java.util.List;

public class DirectionalReactorPartBlock extends ReactorPartBlock {
    public DirectionalReactorPartBlock(Block.Properties properties, String registryName, IMultiBlockType type) {
        super(properties, registryName, type);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer != null) {
            world.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, placer)), 2);
        }
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity placer) {
        return Direction.getFacingFromVector((float) (placer.posX - clickedBlock.getX()), (float) (placer.posY - clickedBlock.getY()), (float) (placer.posZ - clickedBlock.getZ()));
    }

    @Override
    protected List<IProperty<?>> getFillStateProperties() {
        List<IProperty<?>> result = super.getFillStateProperties();
        result.add(BlockStateProperties.FACING);
        return result;
    }
}
