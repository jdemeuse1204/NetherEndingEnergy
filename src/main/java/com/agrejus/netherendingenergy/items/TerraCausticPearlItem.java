package com.agrejus.netherendingenergy.items;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.item.Item;

public class TerraCausticPearlItem  extends Item {

    public TerraCausticPearlItem() {
        super(new Item.Properties()
                .maxStackSize(16)
                .group(NetherEndingEnergy.setup.itemGroup));
        setRegistryName(RegistryNames.TERRA_CAUSTIC_PEARL);
    }
}