package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.base.reactor.ReactorRedstoneInputPartBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorMultiBlock;
import net.minecraft.block.Block;
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

// RENAME TO INPUT PORT, WE ARE HAVING AN OUTPUT PORT AS WELL
public class ReactorRedstonePortBlock extends ReactorRedstoneInputPartBlock {

    public ReactorRedstonePortBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(.01f)
                .lightValue(0), RegistryNames.REACTOR_REDSTONE_PORT, TerraReactorMultiBlock.INSTANCE);
    }

    @Override
    protected IProperty<?>[] getFillStateProperties() {
        return new IProperty<?>[]{FORMED, BlockStateProperties.FACING, BlockStateProperties.POWERED};
    }

    @Override
    protected BlockState getDefaultReactorState() {
        return super.getDefaultReactorState().with(BlockStateProperties.POWERED, Boolean.valueOf(false));
    }

    @Override
    public void onRedstoneSignalChanged(@Nullable Direction poweredDirection, boolean isPowered, int power, BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {

        if (poweredDirection != null && poweredDirection == state.get(BlockStateProperties.FACING)) {
            if (isPowered == true) {
                world.setBlockState(pos, state.with(BlockStateProperties.POWERED, Boolean.valueOf(true)), 2);
                return;
            }
            world.setBlockState(pos, state.with(BlockStateProperties.POWERED, Boolean.valueOf(false)), 2);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ReactorRedstonePortTile();
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
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return side != null && side == state.get(BlockStateProperties.FACING);
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return false;
    }
}
