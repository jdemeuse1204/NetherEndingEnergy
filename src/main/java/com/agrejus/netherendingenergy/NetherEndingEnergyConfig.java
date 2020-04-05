package com.agrejus.netherendingenergy;

import com.agrejus.netherendingenergy.common.models.BellTraits;
import com.agrejus.netherendingenergy.common.models.MixerRecipe;
import com.agrejus.netherendingenergy.common.models.NumberRoll;
import com.agrejus.netherendingenergy.fluids.ModFluids;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NetherEndingEnergyConfig {

    private static CausticBell bell;
    private static General general;
    private static Mixer mixer;

    public static General General() {
        if (general == null) {
            general = new General();
        }
        return general;
    }

    public static CausticBell CausticBell() {
        if (bell == null) {
            bell = new CausticBell();
        }

        return bell;
    }

    public static Mixer Mixer() {
        if (mixer == null) {
            mixer = new Mixer();
        }

        return mixer;
    }

    public static class General {
        public ArrayList<Direction> horizontalDirections = new ArrayList<Direction>() {
            {
                add(Direction.EAST);
                add(Direction.WEST);
                add(Direction.NORTH);
                add(Direction.SOUTH);
            }
        };
    }

    public static class Mixer {

        public ArrayList<MixerRecipe> recipes = new ArrayList<MixerRecipe>() {
            {
                add(new MixerRecipe(ModFluids.ACID_OF_THE_WITHER, ModFluids.ACID_OF_THE_NETHER, 8000, Items.WITHER_SKELETON_SKULL));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_LIVING, ModFluids.ACID_OF_THE_ORDINARY, 2000, Items.CHICKEN, Items.BEEF, Items.MUTTON));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_DEAD, ModFluids.ACID_OF_THE_LIVING, 4000, Items.ROTTEN_FLESH));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_FORREST, ModFluids.ACID_OF_THE_ORDINARY, 2000,
                        Items.SPRUCE_SAPLING,
                        Items.ACACIA_SAPLING,
                        Items.BIRCH_SAPLING,
                        Items.OAK_SAPLING,
                        Items.DARK_OAK_SAPLING,
                        Items.JUNGLE_SAPLING));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_WISE, ModFluids.ACID_OF_THE_LIVING, 4000, Items.EXPERIENCE_BOTTLE));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_ELSEWHERE, ModFluids.ACID_OF_THE_UNSTABLE, 8000, Items.ENDER_PEARL));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_WINTER, ModFluids.ACID_OF_THE_ORDINARY, 2000, Items.SNOWBALL));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_TEARFUL, ModFluids.ACID_OF_THE_NETHER, 8000, Items.GHAST_TEAR));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_BLAZE, ModFluids.ACID_OF_THE_NETHER, 8000, Items.BLAZE_POWDER));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_NETHER, ModFluids.ACID_OF_THE_DEAD, 6000, Items.NETHER_WART));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_MOLTEN, ModFluids.ACID_OF_THE_FORREST, 4000, Items.LAVA_BUCKET));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_UNSTABLE, ModFluids.ACID_OF_THE_TIRELESS, 6000, Items.GUNPOWDER));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_MESSENGER, ModFluids.ACID_OF_THE_LIVING, 4000, Items.REDSTONE));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_FORTUNATE, ModFluids.ACID_OF_THE_MESSENGER, 6000, Items.LAPIS_LAZULI));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_CHORUS, ModFluids.ACID_OF_THE_ELSEWHERE, 10000, Items.CHORUS_FRUIT));
                add(new MixerRecipe(ModFluids.ACID_OF_THE_TIRELESS, ModFluids.ACID_OF_THE_WINTER, 4000, Items.SUGAR));
            }
        };

        public Map<Item, Integer> destructibleItems = new HashMap<Item, Integer>() {
            {
                put(Items.ROTTEN_FLESH, 20);
                put(Items.WITHER_SKELETON_SKULL, 2000);
                put(Items.NETHER_STAR, 5000);
                put(Items.CHICKEN, 750);
                put(Items.BEEF, 750);
                put(Items.MUTTON, 750);
                put(Items.ACACIA_SAPLING, 10);
                put(Items.OAK_SAPLING, 10);
                put(Items.BIRCH_SAPLING, 10);
                put(Items.DARK_OAK_SAPLING, 10);
                put(Items.JUNGLE_SAPLING, 10);
                put(Items.SPRUCE_SAPLING, 10);
                put(Items.EXPERIENCE_BOTTLE, 2500);
                put(Items.ENDER_PEARL, 2000);
                put(Items.SNOWBALL, 200);
                put(Items.GHAST_TEAR, 1000);
                put(Items.BLAZE_POWDER, 1250);
                put(Items.NETHER_WART, 750);
                put(Items.LAVA_BUCKET, 650);
                put(Items.GUNPOWDER, 550);
                put(Items.REDSTONE, 150);
                put(Items.LAPIS_LAZULI, 400);
                put(Items.CHORUS_FRUIT, 4000);
                put(Items.SUGAR, 600);
            }
        };
    }

    public static class CausticBell {
        public NumberRoll[] ranges = new NumberRoll[]{
                new NumberRoll(0, 500, 0),
                new NumberRoll(501, 750, 5),
                new NumberRoll(751, 875, 10),
                new NumberRoll(876, 938, 15),
                new NumberRoll(939, 970, 19)
        };

        public Direction[] mainTrunkDirections = new Direction[]{
                Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
        };
        public Map<Block, BellTraits> consumableBlocks = new HashMap<Block, BellTraits>() {
            {
                put(Blocks.DIAMOND_BLOCK, new BellTraits(.75f, 2.3f, 2.5f));

                put(Blocks.EMERALD_BLOCK, new BellTraits(1, 3.5f, 4f));

                put(Blocks.IRON_BLOCK, new BellTraits(0, .15f, .001f));

                put(Blocks.LAPIS_BLOCK, new BellTraits(.45f, .02f, .55f));

                put(Blocks.REDSTONE_BLOCK, new BellTraits(.15f));

                put(Blocks.PURPUR_BLOCK, new BellTraits(3.1f, 1f, 2.1f));
                put(Blocks.END_STONE, new BellTraits(.95f));
                put(Blocks.NETHER_BRICKS, new BellTraits(.1f));
                put(Blocks.NETHERRACK, new BellTraits(.001f));
                put(Blocks.SOUL_SAND, new BellTraits(-.001f, 1.9f, 1.6f));
                put(Blocks.GLOWSTONE, new BellTraits(.45f));
                put(Blocks.BONE_BLOCK, new BellTraits(-.001f, -.001f, 3f));
                put(Blocks.BRICKS, new BellTraits(.01f));
                put(Blocks.COAL_BLOCK, new BellTraits(.05f, 1.5f, -.5f));
                put(Blocks.CHORUS_FLOWER, new BellTraits(0, 1f, 0));

                put(Blocks.DRAGON_EGG, new BellTraits(10f, 5f, 1f));
                put(Blocks.NETHER_WART_BLOCK, new BellTraits(.25f));
                put(Blocks.QUARTZ_BLOCK, new BellTraits(.1f));

                // Heads
                put(Blocks.CREEPER_HEAD, new BellTraits(.6f, .2f, -.1f));
                put(Blocks.DRAGON_HEAD, new BellTraits(2f, 3f, -.5f));
                put(Blocks.PLAYER_HEAD, new BellTraits(1f, .6f, -.3f));
                put(Blocks.ZOMBIE_HEAD, new BellTraits(.8f, .4f, -.25f));
            }
        };

        public Map<Material, BellTraits> consumableMaterials = new HashMap<Material, BellTraits>() {
            {
                put(Material.WOOD, new BellTraits(-.001f));
                put(Material.EARTH, new BellTraits(-.01f));
                put(Material.ROCK, new BellTraits(-.01f));
                put(Material.BAMBOO, new BellTraits(-.001f));
                put(Material.BAMBOO_SAPLING, new BellTraits(-.001f));
                put(Material.CLAY, new BellTraits(.02f, 0, 0f));
                put(Material.IRON, new BellTraits(0, .02f, 0f));
                put(Material.LAVA, new BellTraits(0, .03f, -.001f));
                put(Material.WATER, new BellTraits(-.001f, -.001f, .01f));
                put(Material.LEAVES, new BellTraits(-.001f));
                put(Material.FIRE, new BellTraits(0, .03f, -.001f));
                put(Material.TNT, new BellTraits(.001f, .035f, -.001f));
                put(Material.ICE, new BellTraits(-.001f, -.001f, .01f));
                put(Material.SNOW, new BellTraits(-.001f, -.001f, .01f));
                put(Material.SNOW_BLOCK, new BellTraits(-.001f, -.001f, .01f));
            }
        };
        public ArrayList<Direction> spreadableDirections = new ArrayList<Direction>() {
            {
                add(Direction.NORTH);
                add(Direction.SOUTH);
                add(Direction.EAST);
                add(Direction.WEST);

                add(Direction.NORTH);
                add(Direction.SOUTH);
                add(Direction.EAST);
                add(Direction.WEST);

                add(Direction.NORTH);
                add(Direction.SOUTH);
                add(Direction.EAST);
                add(Direction.WEST);

                add(Direction.NORTH);
                add(Direction.SOUTH);
                add(Direction.EAST);
                add(Direction.WEST);

                add(Direction.NORTH);
                add(Direction.SOUTH);
                add(Direction.EAST);
                add(Direction.WEST);

                add(Direction.NORTH);
                add(Direction.SOUTH);
                add(Direction.EAST);
                add(Direction.WEST);

                add(Direction.NORTH);
                add(Direction.SOUTH);
                add(Direction.EAST);
                add(Direction.WEST);

                add(Direction.NORTH);
                add(Direction.SOUTH);
                add(Direction.EAST);
                add(Direction.WEST);
            }
        };
    }
}
