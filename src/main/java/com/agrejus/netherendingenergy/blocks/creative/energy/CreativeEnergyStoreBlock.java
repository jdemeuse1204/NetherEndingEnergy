package com.agrejus.netherendingenergy.blocks.creative.energy;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class CreativeEnergyStoreBlock  extends Block {
    public CreativeEnergyStoreBlock() {
        super(Block.Properties.create(Material.IRON)
                .sound(SoundType.GROUND)
                .hardnessAndResistance(.5f)
                .lightValue(0));
        setRegistryName(RegistryNames.Creative.CREATIVE_ENERGY_STORE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CreativeEnergyStoreTile();
    }
}