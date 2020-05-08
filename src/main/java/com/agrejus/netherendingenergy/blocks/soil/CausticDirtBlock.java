package com.agrejus.netherendingenergy.blocks.soil;

import com.agrejus.netherendingenergy.NetherEndingEnergyBlockStateProperties;
import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.interfaces.ICaustic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock.FORMED;

public class CausticDirtBlock extends Block implements ICaustic {
    public CausticDirtBlock() {
        super(Block.Properties.create(Material.EARTH)
                .sound(SoundType.GROUND)
                .hardnessAndResistance(.5f)
                .lightValue(0));
        setRegistryName(RegistryNames.CAUSTIC_DIRT);
    }
}
