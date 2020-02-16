package com.agrejus.netherendingenergy.blocks.abyssal.heatsink;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.common.blocks.PartialModelFillBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class AbyssHeatSinkBlock  extends Block {
    public AbyssHeatSinkBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.ABYSS_HEAT_SINK);
    }
}