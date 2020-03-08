package com.agrejus.netherendingenergy.blocks.terra.reactor.casing;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.base.reactor.ReactorPartBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorReactorMultiBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class TerraReactorCasingBlock extends ReactorPartBlock {

    public TerraReactorCasingBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.ANVIL)
                .hardnessAndResistance(2.0f)
                .lightValue(0), RegistryNames.TERRA_REACTOR_CASING, TerraReactorReactorMultiBlock.INSTANCE);
    }
}
