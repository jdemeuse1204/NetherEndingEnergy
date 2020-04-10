package com.agrejus.netherendingenergy.blocks.general.wireless.item;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleBlock;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleTileBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class WirelessItemTransferModuleBlock extends ModuleBlock {

    public WirelessItemTransferModuleBlock() {
        super(RegistryNames.WIRELESS_ITEM_TRANSFER_MODULE);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WirelessItemTransferModuleTile();
    }
}
