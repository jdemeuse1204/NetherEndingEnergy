package com.agrejus.netherendingenergy.common.enumeration;

import net.minecraft.util.IStringSerializable;

public enum LevelType implements IStringSerializable {
    TERRA("terra"),
    CHAOTIC("chaotic"),
    ABYSSAL("abyssal");

    public static final LevelType[] VALUES = values();

    private String name;

    private LevelType(String name) {
        this.name = name;
    }

    public static LevelType get(String name){
        int size = VALUES.length;
        for(int i = 0;i<size;i++) {
            LevelType value = VALUES[i];
            if(value.getName().equals(name)) {
                return value;
            }
        }

        return LevelType.TERRA;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
