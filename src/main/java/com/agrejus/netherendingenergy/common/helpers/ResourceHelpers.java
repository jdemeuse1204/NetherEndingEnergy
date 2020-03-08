package com.agrejus.netherendingenergy.common.helpers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class ResourceHelpers {
    public static String resolveBlockPath(Block block) {
        return resolveTranslationKey(block.getTranslationKey());
    }

    public static String resolveFluidPath(Block block) {
        return resolveTranslationKey(block.getTranslationKey());
    }

    public static String resolveItemPath(Item item) {
        return resolveTranslationKey(item.getTranslationKey());
    }

    private static String resolveTranslationKey(String translationKey) {
        String[] parts = translationKey.split("\\.");
        String path = String.format("%s:%s/%s", parts[1], parts[0], parts[2]);
        if (path.startsWith("minecraft:")) {
            return path.replace("minecraft:", "");
        }
        return path;
    }
}
