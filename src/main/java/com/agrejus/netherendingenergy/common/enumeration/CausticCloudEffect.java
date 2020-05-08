package com.agrejus.netherendingenergy.common.enumeration;

import net.minecraft.util.IStringSerializable;

public enum CausticCloudEffect implements IStringSerializable {
    NAUSEA("nausea"),
    WITHER("wither"),
    WEAKNESS("weakness"),
    POISON("poison");

    public static final CausticCloudEffect[] VALUES = values();
    private String name;
    private CausticCloudEffect(String name) {
        this.name = name;
    }

    public static CausticCloudEffect get(String name){
        int size = VALUES.length;
        for(int i = 0;i<size;i++) {
            CausticCloudEffect activation = VALUES[i];
            if(activation.getName().equals(name)) {
                return activation;
            }
        }

        return CausticCloudEffect.NAUSEA;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
