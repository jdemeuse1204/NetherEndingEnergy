package com.agrejus.netherendingenergy.item;

import net.minecraft.item.Item;

public class ItemUpgradeToolConfig extends ItemConfig {

    public ItemUpgradeToolConfig(boolean upgrade) {
        super(
                ColossalChests._instance,
                "upgrade_tool" + (upgrade ? "" : "_reverse"),
                (eConfig) -> new ItemUpgradeTool(new Item.Properties()
                        .maxStackSize(1)
                        .group(ColossalChests._instance.getDefaultItemGroup()),
                        upgrade)
        );
    }

}
