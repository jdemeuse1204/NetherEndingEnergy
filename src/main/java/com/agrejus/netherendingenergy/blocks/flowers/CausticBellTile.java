package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class CausticBellTile extends TileEntity implements ITickableTileEntity {

    private int strength;
    private int yield;
    private int purity;

    private int counter;
    private Block blockBelow;

    private HashMap<Block, Integer> yieldBlockValues = new HashMap<Block, Integer>() {{
        // Overworld = 30
        put(Blocks.GRASS_BLOCK, 1);
        put(Blocks.DIRT, 1);
        put(Blocks.SAND, 1);
        put(Blocks.GRAVEL, 1);
        put(Blocks.STONE, 1);

        // Nether = 45
        put(Blocks.SOUL_SAND, 4);
        put(Blocks.NETHERRACK, 3);
        put(Blocks.NETHER_BRICKS, 8);
        put(Blocks.GLOWSTONE, 7);

        // End = 70
        put(Blocks.END_STONE, 13);
        put(Blocks.PURPUR_BLOCK, 16);
    }};
    private HashMap<Material, Integer> yieldMaterialValues = new HashMap<Material, Integer>() {{
        put(Material.WOOD, 1);
    }};

    private HashMap<Block, Integer> strengthBlockValues = new HashMap<Block, Integer>() {{
        // Overworld
        put(Blocks.GRASS_BLOCK, 10);
        put(Blocks.DIRT, 10);
        put(Blocks.SAND, 11);
        put(Blocks.GRAVEL, 12);
        put(Blocks.STONE, 15);

        //Nether
        put(Blocks.SOUL_SAND, 25);
        put(Blocks.NETHERRACK, 24);
        put(Blocks.NETHER_BRICKS, 32);
        put(Blocks.GLOWSTONE, 28);

        // End
        put(Blocks.END_STONE, 38);
        put(Blocks.PURPUR_BLOCK, 45);
    }};
    private HashMap<Material, Integer> strengthMaterialValues = new HashMap<Material, Integer>() {{
        put(Material.WOOD, 11);
    }};

    private HashMap<Block, Integer> purityBlockValues = new HashMap<Block, Integer>() {{
        // Overworld
        put(Blocks.GRASS_BLOCK, 1);
        put(Blocks.DIRT, 1);
        put(Blocks.SAND, 1);
        put(Blocks.GRAVEL, 1);
        put(Blocks.STONE, 1);

        //Nether
        put(Blocks.SOUL_SAND, 5);
        put(Blocks.NETHERRACK, 5);
        put(Blocks.NETHER_BRICKS, 8);
        put(Blocks.GLOWSTONE, 6);

        // End
        put(Blocks.END_STONE, 12);
        put(Blocks.PURPUR_BLOCK, 18);
    }};
    private HashMap<Material, Integer> purityMaterialValues = new HashMap<Material, Integer>() {{
        put(Material.WOOD, 1);
    }};

    public CausticBellTile() {
        super(ModBlocks.CAUSTIC_BELL_TILE);
    }

    public int getStrength() {
        return strength;
    }

    public int getYield() {
        return yield;
    }

    public int getPurity() {
        return purity;
    }

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        if (counter > 0) {

            counter--;
            if (counter <= 0) {
                // going from 1 to 0, will reset next, do operation here
                // Collect Gas here

            }
            markDirty();
        }

        if (counter <= 0) {

            // Start of the operation
            BlockPos position = getPos().down();
            blockBelow = world.getBlockState(position).getBlock();
            strength = getStrengthFromBlock(blockBelow);
            yield = getYieldFromBlock(blockBelow);
            purity = getPurityFromBlock(blockBelow);

            markDirty();

            counter = 20;
        }
    }

    private int getPurityFromBlock(Block block) {

        if (purityBlockValues.containsKey(block)) {
            return purityBlockValues.get(block);
        }

        BlockState state = block.getDefaultState();
        Material material = block.getMaterial(state);

        if (purityMaterialValues.containsKey(material)) {
            return purityMaterialValues.get(material);
        }

        return 0;
    }

    private int getYieldFromBlock(Block block) {

        if (yieldBlockValues.containsKey(block)) {
            return yieldBlockValues.get(block);
        }

        BlockState state = block.getDefaultState();
        Material material = block.getMaterial(state);

        if (yieldMaterialValues.containsKey(material)) {
            return yieldMaterialValues.get(material);
        }

        return 0;
    }

    private int getStrengthFromBlock(Block block) {

        if (strengthBlockValues.containsKey(block)) {
            return strengthBlockValues.get(block);
        }

        BlockState state = block.getDefaultState();
        Material material = block.getMaterial(state);

        if (strengthMaterialValues.containsKey(material)) {
            return strengthMaterialValues.get(material);
        }

        return 0;
    }
}
