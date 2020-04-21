package com.agrejus.netherendingenergy.common.enumeration;

import net.minecraft.util.IStringSerializable;

public enum RootType implements IStringSerializable {
    MAIN_TRUNK("main_trunk"),
    OFFSHOOT("offshootroot"),
    SOURCE("source");

    public static final RootType[] VALUES = values();

    private String name;

    private RootType(String name) {
        this.name = name;
    }

    public static RootType get(String name){
        int size = VALUES.length;
        for(int i = 0;i<size;i++) {
            RootType value = VALUES[i];
            if(value.getName().equals(name)) {
                return value;
            }
        }

        return RootType.MAIN_TRUNK;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
