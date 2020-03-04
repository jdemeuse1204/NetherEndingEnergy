package com.agrejus.netherendingenergy.blocks.terra.reactor.heatsink;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.base.reactor.ReactorPartBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock.FORMED;

public class TerraHeatSinkBlock extends ReactorPartBlock {

    public TerraHeatSinkBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0f)
                .lightValue(0), RegistryNames.TERRA_HEAT_SINK, TerraReactorMultiBlock.INSTANCE);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return true;
    }
}