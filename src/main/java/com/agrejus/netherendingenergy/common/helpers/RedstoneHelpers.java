package com.agrejus.netherendingenergy.common.helpers;

public class RedstoneHelpers {
    public static int computeSignalStrength(int current, int max) {

        int result = Math.round(((float)current / (float)max) * 15);

        if (result == 0 && current > 1) {
            return 1;
        }

        if (current > max) {
            return 15;
        }

        return result;
    }
}
