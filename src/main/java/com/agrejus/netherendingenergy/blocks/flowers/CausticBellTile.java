package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

public class CausticBellTile extends TileEntity implements ITickableTileEntity {

    private float strength;
    private float yield;
    private int counter;
    private Block blockBelow;

    private HashMap<Block, Float> yieldBlockValues = new HashMap<Block, Float>() {{
        // Overworld
        put(Blocks.GRASS_BLOCK, 1.5f);
        put(Blocks.DIRT, 1.5f);
        put(Blocks.SAND, 1.3f);
        put(Blocks.GRAVEL, 1.6f);
        put(Blocks.STONE, 2f);

        // Nether
        put(Blocks.SOUL_SAND, 2f);
        put(Blocks.NETHERRACK, 3f);
        put(Blocks.NETHER_BRICKS, 2.5f);
        put(Blocks.GLOWSTONE, .8f);

        // End
        put(Blocks.END_STONE, 4f);
        put(Blocks.PURPUR_BLOCK, 3f);
    }};
    private HashMap<Block, Float> strengthBlockValues = new HashMap<Block, Float>() {{
        // Overworld
        put(Blocks.GRASS_BLOCK, 1.5f);
        put(Blocks.DIRT, 1.5f);
        put(Blocks.SAND, 1f);
        put(Blocks.GRAVEL, 1.4f);
        put(Blocks.STONE, 2f);

        //Nether
        put(Blocks.SOUL_SAND, 2f);
        put(Blocks.NETHERRACK, 1f);
        put(Blocks.NETHER_BRICKS, 1.5f);
        put(Blocks.GLOWSTONE, 3.2f);

        // End
        put(Blocks.END_STONE, 2f);
        put(Blocks.PURPUR_BLOCK, 3f);
    }};

    private HashMap<Material, Float> yieldMaterialValues = new HashMap<Material, Float>() {{
        put(Material.WOOD, .8f);
    }};
    private HashMap<Material, Float> strengthMaterialValues = new HashMap<Material, Float>() {{
        put(Material.WOOD, 2.2f);
    }};


    /* Overworld
     * Dirt              Y=1.5,S=1.5
     * Sand              Y=1.3,S=1.7
     * Gravel            Y=1.6,S=1.4
     * Stone             Y=1,S=2
     * Wood              Y=.8,S=2.2
     * */

    /* Nether
     * Soul Sand        Y=2,S=2
     * Netherrack       Y=3,S=1
     * Nether Brick     Y=2.5,S=1.5
     * Glowstone        Y=.8,S=3.2
     * */

    /* End
     * End Stone        Y=4,S=2
     * Purpur           Y=3,S=3
     * */

    public CausticBellTile() {
        super(ModBlocks.CAUSTIC_BELL_TILE);
    }

    public float getStrength() {
        return strength;
    }

    public float getYield() {
        return yield;
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

            markDirty();

            counter = 20;
        }
    }

    private float getYieldFromBlock(Block block) {

        if (yieldBlockValues.containsKey(block)) {
            return yieldBlockValues.get(block);
        }

        BlockState state = block.getDefaultState();
        Material material = block.getMaterial(state);

        if (yieldMaterialValues.containsKey(material)) {
            return yieldMaterialValues.get(material);
        }

        return 0f;
    }

    private Float getStrengthFromBlock(Block block) {

        if (strengthBlockValues.containsKey(block)) {
            return strengthBlockValues.get(block);
        }

        BlockState state = block.getDefaultState();
        Material material = block.getMaterial(state);

        if (strengthMaterialValues.containsKey(material)) {
            return strengthMaterialValues.get(material);
        }

        return 0f;
    }
}
