package com.agrejus.netherendingenergy.common.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;

import java.util.List;

public class PotionsHelper {
    public static boolean equals(Potion potion, ItemStack stack) {
        return stack.getTag().getString("Potion").equals(potion.getRegistryName().toString());
    }

    public static boolean any(List<Potion> potions, ItemStack stack) {
        int length = potions.size();
        for (int i = 0; i < length; i++) {
            if (PotionsHelper.equals(potions.get(i), stack)) {
                return  true;
            }
        }
        return false;
    }
}
