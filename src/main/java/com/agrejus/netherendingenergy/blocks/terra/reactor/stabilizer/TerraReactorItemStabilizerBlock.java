package com.agrejus.netherendingenergy.blocks.terra.reactor.stabilizer;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.base.reactor.DirectionalReactorPartBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorReactorMultiBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class TerraReactorItemStabilizerBlock extends DirectionalReactorPartBlock {

    public TerraReactorItemStabilizerBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(1.0f)
                .lightValue(0), RegistryNames.TERRA_REACTOR_ITEM_STABILIZER,  TerraReactorReactorMultiBlock.INSTANCE);
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }
}
