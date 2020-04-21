package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.energy;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.base.reactor.ReactorCapabilityTileEntity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TerraReactorEnergyPortTile extends ReactorCapabilityTileEntity {

    public TerraReactorEnergyPortTile() {
        super(ModBlocks.TERRA_REACTOR_ENERGY_PORT_TILE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY && side != null && getBlockState().get(BlockStateProperties.FACING) == side) {
            // flow through to the core
            return getCapabilityFromCore(cap, side);
        }
        return super.getCapability(cap, side);
    }
}
