package com.agrejus.netherendingenergy.common.reactor;

import com.agrejus.netherendingenergy.common.attributes.PotionAttributes;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.reactor.fuel.WorldFuelBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;

public class ReactorBaseConfig {

    public static ReactorBaseConfig INSTANCE = new ReactorBaseConfig();
    private static Map<Potion, PotionAttributes> potionAttributesMap;
    private static Map<Item, PotionAttributes> itemAttributesMap;

    public static WorldFuelBase getBaseFuel(DimensionType type) {

        if (type == DimensionType.OVERWORLD) {
            return new WorldFuelBase(0, .3f, 254, 62, 1.25f, 16);
        }

        if (type == DimensionType.THE_NETHER) {
            return new WorldFuelBase(0, .08f, 32, 0, .04f, 32);
        }

        if (type == DimensionType.THE_END) {
            return new WorldFuelBase(0, .1f, 40000, 0, .06f, 1000);
        }

        // Default
        return new WorldFuelBase(0, .06f, 254, 62, .03f, 16);
    }

    public static WorldFuelBase getBaseFuel(ReactorBaseType type) {
        switch (type) {
            default:
            case Terra:
                return new WorldFuelBase(0, .3f, 254, 62, 1.25f, 16);
            case Chaotic:
                return new WorldFuelBase(0, .08f, 32, 0, .04f, 32);
            case Abyssal:
                return new WorldFuelBase(0, .1f, 40000, 0, .06f, 1000);
        }
    }

    public int ComputeSpatial(World world, BlockPos position) {
        DimensionType dimensionType = world.getDimension().getType();
        int level = 0;

        if (dimensionType == DimensionType.OVERWORLD) {
            level = position.getY();
        }

        if (dimensionType == DimensionType.THE_NETHER) {

        }

        if (dimensionType == DimensionType.THE_END) {
            // farthest x,z away from 0,0
            BlockPos startingPosition = new BlockPos(0,64,0);
            level = BlockHelpers.getHorizontalDistance(position, startingPosition);
        }

        WorldFuelBase fuelBase = ReactorBaseConfig.getBaseFuel(dimensionType);
        int spatialAmount = 1;
        if (level > fuelBase.getBaseLevel()) {
            spatialAmount = (level - fuelBase.getBaseLevel()) / fuelBase.getStepAmount();
        }

        return spatialAmount;
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
            itemAttributesMap.put(Items.DRAGON_BREATH, new PotionAttributes(5f, 2f, -.6f, .03f, 1000, .01f));
        }

        return itemAttributesMap;
    }

    private static Map<Potion, PotionAttributes> getPotionAttributesMap() {
        if (potionAttributesMap == null) {
            potionAttributesMap = new HashMap<>();
            potionAttributesMap.put(Potions.FIRE_RESISTANCE, new PotionAttributes(.25f, .1f, -.03f, .02f, 100, .01f));
            potionAttributesMap.put(Potions.LONG_FIRE_RESISTANCE, new PotionAttributes(.5f, .15f, -.08f, .04f, 300, .01f));

            potionAttributesMap.put(Potions.HEALING, new PotionAttributes(.03f, .03f, .08f, 0f, 100, .01f));
            potionAttributesMap.put(Potions.HEALING, new PotionAttributes(.06f, .06f, .15f, 0f, 300, .01f));

            potionAttributesMap.put(Potions.REGENERATION, new PotionAttributes(0f, .03f, .05f, .01f, 50, .01f));
            potionAttributesMap.put(Potions.STRONG_REGENERATION, new PotionAttributes(0f, .08f, .1f, .02f, 100, .01f));
            potionAttributesMap.put(Potions.LONG_REGENERATION, new PotionAttributes(0f, .15f, .2f, .04f, 300, .01f));

            potionAttributesMap.put(Potions.STRENGTH, new PotionAttributes(.15f, -.05f, 0f, 0f, 50, .01f));
            potionAttributesMap.put(Potions.STRONG_STRENGTH, new PotionAttributes(.3f, -.03f, 0f, 0f, 100, .01f));
            potionAttributesMap.put(Potions.LONG_STRENGTH, new PotionAttributes(.4f, 0f, 0f, 0f, 300, .01f));

            potionAttributesMap.put(Potions.SWIFTNESS, new PotionAttributes(0f, .08f, .05f, .01f, 50, .01f));
            potionAttributesMap.put(Potions.STRONG_SWIFTNESS, new PotionAttributes(0f, .12f, .08f, .01f, 100, .01f));
            potionAttributesMap.put(Potions.LONG_SWIFTNESS, new PotionAttributes(0f, .25f, .15f, .01f, 300, .01f));

            potionAttributesMap.put(Potions.POISON, new PotionAttributes(-.03f, -.08f, -.08f, -.02f, 50, .01f));
            potionAttributesMap.put(Potions.STRONG_POISON, new PotionAttributes(-.06f, -.16f, -.2f, -.03f, 100, .01f));
            potionAttributesMap.put(Potions.LONG_POISON, new PotionAttributes(-.12f, -.24f, -.35f, -.04f, 300, .01f));

            potionAttributesMap.put(Potions.SLOWNESS, new PotionAttributes(-.02f, .1f, .35f, 0f, 100, .01f));
            potionAttributesMap.put(Potions.STRONG_SLOWNESS, new PotionAttributes(0f, .3f, .45f, 0f, 300, .01f));

            potionAttributesMap.put(Potions.WEAKNESS, new PotionAttributes(-.01f, -.01f, -.01f, 0f, 100, .01f));
            potionAttributesMap.put(Potions.LONG_WEAKNESS, new PotionAttributes(-.05f, -.05f, -.05f, 0f, 300, .01f));
        }
        return potionAttributesMap;
    }
}
