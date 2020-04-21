package com.agrejus.netherendingenergy.common.enumeration;

import net.minecraft.util.IStringSerializable;

public enum TransferMode implements IStringSerializable {
    SEND("send"),
    RECEIVE("receive"),
    SEND_AND_RECEIVE("send_and_receive"),
    NONE("none");

    public static final TransferMode[] VALUES = values();
    private String name;

    private TransferMode(String name) {
        this.name = name;
    }

    public static TransferMode byName(String name) {
        int size = VALUES.length;
        for (int i = 0; i < size; i++) {
            TransferMode activation = VALUES[i];
            if (activation.getName().equals(name)) {
                return activation;
            }
        }

        return TransferMode.NONE;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
