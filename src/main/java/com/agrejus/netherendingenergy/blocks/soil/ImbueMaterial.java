package com.agrejus.netherendingenergy.blocks.soil;

import net.minecraft.util.IStringSerializable;

public enum ImbueMaterial implements IStringSerializable {

    DIAMOND("diamond"),
    EMERALD("emerald"),
    LAPIS("lapis"),
    CRYSTALIZED_VITROL("crystalized_vitrol"),

    NETHER_QUARTZ("nether_quartz"),
    GLOWSTONE("glowstone"),
    NETHER_BRICK("nether_brick"),
    OILY_HEART("oily_heart"),

    PURPUR("purpur"),
    CHOROUS_FLOWER("chorous_flower"),
    SHULKER_BLOCK("shulker_block"),
    ENIGMATIC_GROWTH("enigmatic_growth");

    private final String name;

    private ImbueMaterial(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}


