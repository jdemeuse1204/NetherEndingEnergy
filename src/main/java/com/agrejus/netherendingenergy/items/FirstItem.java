package com.agrejus.netherendingenergy.items;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import net.minecraft.item.Item;

public class FirstItem extends Item {

    public FirstItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(NetherEndingEnergy.setup.itemGroup));
        setRegistryName("firstitem");
    }
}
