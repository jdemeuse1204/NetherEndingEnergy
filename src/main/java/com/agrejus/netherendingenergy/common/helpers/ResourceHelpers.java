package com.agrejus.netherendingenergy.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

public class ResourceHelpers {
    public static String resolveBlockPath(Block block) {
        String translationKey = block.getTranslationKey();
        String[] parts = translationKey.split("\\.");
        String path = String.format("%s:%s/%s", parts[1], parts[0], parts[2]);
        if (path.startsWith("minecraft:")) {
            return path.replace("minecraft:", "");
        }
        return path;
    }
}
