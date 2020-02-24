package com.agrejus.netherendingenergy.blocks.terra.heatsink;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.blocks.PartialModelFillBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;

public class TerraHeatSinkBlock extends PartialModelFillBlock {

    public static final EnumProperty<TerraReactorPartIndex> FORMED = EnumProperty.<TerraReactorPartIndex>create("formed", TerraReactorPartIndex.class);

    public TerraHeatSinkBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_HEAT_SINK);
        setDefaultState(getStateContainer().getBaseState().with(FORMED, TerraReactorPartIndex.UNFORMED));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
    }
}