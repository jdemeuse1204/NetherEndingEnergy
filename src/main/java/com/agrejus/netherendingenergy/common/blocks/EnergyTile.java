package com.agrejus.netherendingenergy.common.blocks;

import com.agrejus.netherendingenergy.blocks.base.tile.NEETileEntity;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class EnergyTile extends NEETileEntity implements ITickableTileEntity {
    protected CustomEnergyStorage energyStore = this.createEnergyStore();
    private int netEnergy;
    private int lastEnergy;

    public EnergyTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    protected abstract CustomEnergyStorage createEnergyStore();

    protected int getEnergyUsedPerTick() {
        return this.netEnergy;
    }

    protected void trackEnergy() {
        // This works
        // Energy fills after tick is complete
        this.netEnergy = this.energyStore.getEnergyStored() - this.lastEnergy;
        this.lastEnergy = this.energyStore.getEnergyStored();
    }
}
