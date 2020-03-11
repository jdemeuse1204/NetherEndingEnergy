package com.agrejus.netherendingenergy.common.reactor;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorConfig;
import com.agrejus.netherendingenergy.common.reactor.attributes.PotionAttributes;
import com.agrejus.netherendingenergy.common.reactor.fuel.FuelBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;

import java.util.HashMap;
import java.util.Map;

public class ReactorBaseConfig {

    public static ReactorBaseConfig INSTANCE = new ReactorBaseConfig();
    private static Map<Potion, PotionAttributes> potionAttributesMap;
    private static Map<Item, PotionAttributes> itemAttributesMap;

    public static FuelBase getBaseFuel(ReactorBaseType type) {
        switch (type) {
            default:
            case Terra:
                return new FuelBase(1f, .7f, 1f, .06f, 360, 16);
            case Chaotic:
                return new FuelBase(2.4f, 2.5f, .5f, .08f, 420, 32);
            case Abyssal:
                return new FuelBase(1.7f, 3.5f, 2f, .1f, 640, 64);
        }
    }

    public int getUsagesForPotion(ItemStack stack) {
        Potion potion = PotionUtils.getPotionFromItem(stack);
        PotionAttributes found = getPotionAttributesMap().get(potion);
        if (found == null) {
            return 0;
        }

        return found.getUses();
    }

    public int getUsagesForItem(Item item) {
        PotionAttributes found = getItemAttributesMap().get(item);
        if (found == null) {
            return 0;
        }

        return found.getUses();
    }

    public boolean canUsePotion(Potion potion) {
        return getPotionAttributesMap().containsKey(potion);
    }

    public boolean canUseItem(Item item) {
        return getItemAttributesMap().containsKey(item);
    }

    public PotionAttributes getItemAttributes(Item item) {
        return getItemAttributesMap().get(item);
    }

    public PotionAttributes getPotionAttributes(Potion potion) {
        return getPotionAttributesMap().get(potion);
    }

    private static Map<Item, PotionAttributes> getItemAttributesMap() {
        if (itemAttributesMap == null) {
            itemAttributesMap = new HashMap<>();
            itemAttributesMap.put(Items.DRAGON_BREATH, new PotionAttributes(5f, 2f, -.6f, .03f, 1000));
        }

        return itemAttributesMap;
    }

    private static Map<Potion, PotionAttributes> getPotionAttributesMap() {
        if (potionAttributesMap == null) {
            potionAttributesMap = new HashMap<>();
            potionAttributesMap.put(Potions.FIRE_RESISTANCE, new PotionAttributes(.25f, .1f, -.03f, .02f, 100));
            potionAttributesMap.put(Potions.LONG_FIRE_RESISTANCE, new PotionAttributes(.5f, .15f, -.08f, .04f, 300));

            potionAttributesMap.put(Potions.HEALING, new PotionAttributes(.03f, .03f, .08f, 0f, 100));
            potionAttributesMap.put(Potions.HEALING, new PotionAttributes(.06f, .06f, .15f, 0f, 300));

            potionAttributesMap.put(Potions.REGENERATION, new PotionAttributes(0f, .03f, .05f, .01f, 50));
            potionAttributesMap.put(Potions.STRONG_REGENERATION, new PotionAttributes(0f, .08f, .1f, .02f, 100));
            potionAttributesMap.put(Potions.LONG_REGENERATION, new PotionAttributes(0f, .15f, .2f, .04f, 300));

            potionAttributesMap.put(Potions.STRENGTH, new PotionAttributes(.15f, -.05f, 0f, 0f, 50));
            potionAttributesMap.put(Potions.STRONG_STRENGTH, new PotionAttributes(.3f, -.03f, 0f, 0f, 100));
            potionAttributesMap.put(Potions.LONG_STRENGTH, new PotionAttributes(.4f, 0f, 0f, 0f, 300));

            potionAttributesMap.put(Potions.SWIFTNESS, new PotionAttributes(0f, .08f, .05f, .01f, 50));
            potionAttributesMap.put(Potions.STRONG_SWIFTNESS, new PotionAttributes(0f, .12f, .08f, .01f, 100));
            potionAttributesMap.put(Potions.LONG_SWIFTNESS, new PotionAttributes(0f, .25f, .15f, .01f, 300));

            potionAttributesMap.put(Potions.POISON, new PotionAttributes(-.03f, -.08f, -.08f, -.02f, 50));
            potionAttributesMap.put(Potions.STRONG_POISON, new PotionAttributes(-.06f, -.16f, -.2f, -.03f, 100));
            potionAttributesMap.put(Potions.LONG_POISON, new PotionAttributes(-.12f, -.24f, -.35f, -.04f, 300));

            potionAttributesMap.put(Potions.SLOWNESS, new PotionAttributes(-.02f, .1f, .35f, 0f, 100));
            potionAttributesMap.put(Potions.STRONG_SLOWNESS, new PotionAttributes(0f, .3f, .45f, 0f, 300));

            potionAttributesMap.put(Potions.WEAKNESS, new PotionAttributes(-.01f, -.01f, -.01f, 0f, 100));
            potionAttributesMap.put(Potions.LONG_WEAKNESS, new PotionAttributes(-.05f, -.05f, -.05f, 0f, 300));
        }
        return potionAttributesMap;
    }
}
