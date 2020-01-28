package com.agrejus.netherendingenergy.blocks.chaotic.heatsink;

import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class ChaoticHeatSinkBlock extends Block {
    public ChaoticHeatSinkBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.CHAOTIC_HEAT_SINK);
    }
}