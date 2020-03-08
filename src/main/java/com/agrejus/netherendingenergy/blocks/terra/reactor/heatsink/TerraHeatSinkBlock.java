package com.agrejus.netherendingenergy.blocks.terra.reactor.heatsink;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.base.reactor.ReactorPartBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorReactorMultiBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class TerraHeatSinkBlock extends ReactorPartBlock {

    public TerraHeatSinkBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0f)
                .lightValue(0), RegistryNames.TERRA_HEAT_SINK, TerraReactorReactorMultiBlock.INSTANCE);
    }


/*    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }*/
}