package com.agrejus.netherendingenergy.blocks.soil;

import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class CausticFarmlandBlock extends Block {
    public CausticFarmlandBlock() {
        super(Block.Properties.create(Material.EARTH)
                .sound(SoundType.GROUND)
                .hardnessAndResistance(1.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.CAUSTIC_FARMLAND);
    }
}
