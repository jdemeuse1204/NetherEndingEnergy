package com.agrejus.netherendingenergy.tileentity;

import net.minecraft.tileentity.TileEntityType;

import java.util.stream.Collectors;

public class TileColossalChestConfig extends TileEntityConfig<TileColossalChest> {

    public TileColossalChestConfig() {
        super(
                ColossalChests._instance,
                "colossal_chest",
                (eConfig) -> new TileEntityType<>(TileColossalChest::new,
                        ChestMaterial.VALUES.stream()
                                .map(ChestMaterial::getBlockCore)
                                .collect(Collectors.toSet()), null)
        );
    }

}
