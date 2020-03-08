package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.liquid;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.base.reactor.ReactorCapabilityTileEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TerraReactorAcidPortTile extends ReactorCapabilityTileEntity {
    public TerraReactorAcidPortTile() {
        super(ModBlocks.TERRA_REACTOR_ACID_PORT_TILE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && side != null && getBlockState().get(BlockStateProperties.FACING) == side) {
            // flow through to the core
            return getCapabilityFromCore(cap, side);
        }
        return super.getCapability(cap, side);
    }
}