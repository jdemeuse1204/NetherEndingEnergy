package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.item;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.base.reactor.ReactorCapabilityTileEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TerraReactorItemPortTile extends ReactorCapabilityTileEntity {
    public TerraReactorItemPortTile() {
        super(ModBlocks.TERRA_REACTOR_ITEM_PORT_TILE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && side != null && getBlockState().get(BlockStateProperties.FACING) == side) {
            // flow through to the core
            return getCapabilityFromCore(cap, side);
        }
        return super.getCapability(cap, side);
    }
}
