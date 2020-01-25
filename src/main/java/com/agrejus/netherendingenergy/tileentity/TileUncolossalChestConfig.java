package com.agrejus.netherendingenergy.tileentity;

import com.google.common.collect.Sets;
import net.minecraft.tileentity.TileEntityType;

public class TileUncolossalChestConfig extends TileEntityConfig<TileUncolossalChest> {

    public TileUncolossalChestConfig() {
        super(
                ColossalChests._instance,
                "uncolossal_chest",
                (eConfig) -> new TileEntityType<>(TileUncolossalChest::new,
                        Sets.newHashSet(RegistryEntries.BLOCK_UNCOLOSSAL_CHEST), null)
        );
    }

}