package com.agrejus.netherendingenergy.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class ChestWallConfig extends BlockConfig {

    public ChestWallConfig(ChestMaterial material) {
        super(
                ColossalChests._instance,
                "chest_wall_" + material.getName(),
                eConfig -> new ChestWall(Block.Properties.create(Material.ROCK)
                        .hardnessAndResistance(5.0F)
                        .sound(SoundType.WOOD)
                        .harvestLevel(0), // Wood tier
                        material),
                (eConfig, block) -> new ItemBlockMaterial(block, new Item.Properties()
                        .group(ColossalChests._instance.getDefaultItemGroup()), material)
        );
    }

}
