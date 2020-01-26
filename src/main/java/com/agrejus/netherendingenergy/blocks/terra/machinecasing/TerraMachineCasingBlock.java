package com.agrejus.netherendingenergy.blocks.terra.machinecasing;

import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class TerraMachineCasingBlock extends Block {
    public TerraMachineCasingBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.ANVIL)
                .hardnessAndResistance(2.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_MACHINE_CASING);
    }
}
