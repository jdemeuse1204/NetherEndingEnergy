package com.agrejus.netherendingenergy.common.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionBrewing;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.datafix.fixes.PotionItems;

import java.util.List;

public class PotionsHelper {
    public static boolean equals(Potion potion, ItemStack stack) {
        Potion foundStackPotion = PotionUtils.getPotionFromItem(stack);
        return foundStackPotion == potion;
    }

    public static boolean any(List<Potion> potions, Potion potion) {
        int length = potions.size();
        for (int i = 0; i < length; i++) {
            if (potions.get(i) == potion) {
                return  true;
            }
        }
        return false;
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
