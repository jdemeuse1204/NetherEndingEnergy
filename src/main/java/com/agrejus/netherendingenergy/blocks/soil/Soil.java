package com.agrejus.netherendingenergy.blocks.soil;

import net.minecraft.block.Block;
import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.item.DyeColor;

public class Soil extends Block implements IBeaconBeamColorProvider {

    private final DyeColor color;

    public Soil(DyeColor color, Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    public DyeColor getColor() {
        return this.color;
    }
}
