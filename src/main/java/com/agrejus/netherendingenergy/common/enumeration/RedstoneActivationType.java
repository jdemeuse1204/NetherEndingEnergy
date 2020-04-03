package com.agrejus.netherendingenergy.common.enumeration;

import net.minecraft.util.IStringSerializable;

public enum RedstoneActivationType implements IStringSerializable {
    ACTIVE_WITH_SIGNAL("active_with_signal"),
    ACTIVE_WITHOUT_SIGNAL("active_without_signal"),
    ALWAYS_ACTIVE("always_active");

    public static final RedstoneActivationType[] VALUES = values();
    private String name;
    private RedstoneActivationType(String name) {
        this.name = name;
    }

    public static RedstoneActivationType get(String name){
        int size = VALUES.length;
        for(int i = 0;i<size;i++) {
            RedstoneActivationType activation = VALUES[i];
            if(activation.getName().equals(name)) {
                return activation;
            }
        }

        return RedstoneActivationType.ALWAYS_ACTIVE;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
