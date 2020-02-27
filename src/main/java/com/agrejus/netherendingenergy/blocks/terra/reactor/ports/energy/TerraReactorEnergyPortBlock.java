package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.energy;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.base.reactor.ReactorPartBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorMultiBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IProperty;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreBlock.FORMED;

public class TerraReactorEnergyPortBlock extends ReactorPartBlock {

    public TerraReactorEnergyPortBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(1.0f)
                .lightValue(0), RegistryNames.TERRA_REACTOR_ENERGY_PORT, TerraReactorMultiBlock.INSTANCE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TerraReactorEnergyPortTile();
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
    protected IProperty<?>[] getFillStateProperties() {
        return new IProperty<?>[]{BlockStateProperties.FACING, FORMED};
    }
}
