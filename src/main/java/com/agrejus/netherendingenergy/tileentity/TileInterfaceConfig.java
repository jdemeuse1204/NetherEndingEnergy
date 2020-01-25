package com.agrejus.netherendingenergy.tileentity;

import net.minecraft.tileentity.TileEntityType;

import java.util.stream.Collectors;

public class TileInterfaceConfig extends TileEntityConfig<TileInterface> {

    public TileInterfaceConfig() {
        super(
                ColossalChests._instance,
                "interface",
                (eConfig) -> new TileEntityType<>(TileInterface::new,
                        ChestMaterial.VALUES.stream()
                                .map(ChestMaterial::getBlockInterface)
                                .collect(Collectors.toSet()), null)
        );
    }

}
