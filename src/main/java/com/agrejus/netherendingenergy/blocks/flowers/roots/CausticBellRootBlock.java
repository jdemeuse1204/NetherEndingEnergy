package com.agrejus.netherendingenergy.blocks.flowers.roots;

import com.agrejus.netherendingenergy.NetherEndingEnergyBlockStateProperties;
import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Effects;
import net.minecraft.state.StateContainer;

public class CausticBellRootBlock extends Block {
    public CausticBellRootBlock() {
        super(Properties.create(Material.WOOD)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0F)
                .lightValue(0));
        setRegistryName(RegistryNames.CAUSTIC_ROOTS);
        setDefaultState(getStateContainer().getBaseState().with(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5, 0));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5);
    }
}
