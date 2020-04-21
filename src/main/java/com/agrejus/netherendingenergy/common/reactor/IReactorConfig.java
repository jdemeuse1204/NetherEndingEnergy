package com.agrejus.netherendingenergy.common.reactor;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IReactorConfig {
    Map<Block, Integer> getBlockCounts();
    HashMap<TerraReactorPartIndex, List<Block>> getParts();
    List<TerraReactorPartIndex> getInjectorLocations();
}
