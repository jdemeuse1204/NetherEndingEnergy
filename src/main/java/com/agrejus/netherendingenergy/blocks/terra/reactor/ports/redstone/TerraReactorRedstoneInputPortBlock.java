package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.base.reactor.DirectionalReactorPartBlock;
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

import java.util.List;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock.FORMED;

// RENAME TO INPUT PORT, WE ARE HAVING AN OUTPUT PORT AS WELL
public class TerraReactorRedstoneInputPortBlock extends DirectionalReactorPartBlock {

    public TerraReactorRedstoneInputPortBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(.01f)
                .lightValue(0), RegistryNames.TERRA_REACTOR_REDSTONE_INPUT_PORT, TerraReactorMultiBlock.INSTANCE);
    }

    @Override
    protected List<IProperty<?>> getFillStateProperties() {
        List<IProperty<?>> result = super.getFillStateProperties();
        result.add(BlockStateProperties.POWERED);
        return result;
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
        return new TerraReactorRedstoneInputPortTile();
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
