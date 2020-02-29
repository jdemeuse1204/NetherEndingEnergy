package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.energy;

import com.agrejus.netherendingenergy.Config;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TerraReactorEnergyPortTile extends TileEntity {
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    private int counter;

    public TerraReactorEnergyPortTile() {

        super(ModBlocks.TERRA_REACTOR_ENERGY_PORT_TILE);
    }

    // On block break save energy amount to core
    // On reactor formed, transfer energy to this block

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(Config.FIRSTBLOCK_MAXPOWER.get(), 0, 0);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }

        return super.getCapability(cap, side);
    }
}
