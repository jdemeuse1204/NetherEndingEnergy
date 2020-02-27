package com.agrejus.netherendingenergy.blocks.terra.reactor.stabilizer;

import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ReactorItemStabilizer extends Block {

    public ReactorItemStabilizer() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(1.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.REACTOR_ITEM_STABILIZER);
    }
}
