package com.agrejus.netherendingenergy.common.blocks;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class EnergyBlock extends TileEntity implements ITickableTileEntity {
    protected LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);
    protected int energyUsePerTick;
    private int netEnergy;
    private int lastEnergy;

    public EnergyBlock(int energyUsePerTick, TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.energyUsePerTick = energyUsePerTick;
    }

    protected abstract IEnergyStorage createEnergy();

    protected int getEnergyUsedPerTick() {
        return this.netEnergy;
    }

    @Override
    public void tick() {
        // This works
        // Energy fills after tick is complete
        energy.ifPresent(w -> {
            this.netEnergy = w.getEnergyStored() - this.lastEnergy;
            this.lastEnergy = w.getEnergyStored();
        });
    }
}
