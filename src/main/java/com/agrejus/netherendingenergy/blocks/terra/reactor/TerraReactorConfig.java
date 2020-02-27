package com.agrejus.netherendingenergy.blocks.terra.reactor;

import com.agrejus.netherendingenergy.blocks.base.reactor.energyport.ReactorEnergyPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.ports.item.TerraReactorItemPortBlock;
import com.agrejus.netherendingenergy.blocks.terra.ports.liquid.TerraReactorAcidPortBlock;
import com.agrejus.netherendingenergy.common.reactor.IReactorConfig;
import java.util.HashMap;
import java.util.Map;

public class TerraReactorConfig implements IReactorConfig {

    public static TerraReactorConfig INSTANCE = new TerraReactorConfig();

    @Override
    public Map<Class, Integer> getBlockCounts() {
        Map<Class, Integer> result = new HashMap<Class, Integer>();

        result.put(ReactorEnergyPortBlock.class, 1);
        result.put(TerraReactorItemPortBlock.class, 1);
        result.put(TerraReactorAcidPortBlock.class, 1);

        return result;
    }
}
