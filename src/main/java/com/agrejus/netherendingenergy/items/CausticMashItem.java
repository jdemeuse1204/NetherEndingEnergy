package com.agrejus.netherendingenergy.items;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.item.Item;

public class CausticMashItem extends Item {

    public CausticMashItem() {
        super(new Item.Properties()
                .maxStackSize(64)
                .group(NetherEndingEnergy.setup.itemGroup));
        setRegistryName(RegistryNames.CAUSTIC_MASH);
    }
}