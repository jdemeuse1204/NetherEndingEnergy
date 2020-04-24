package com.agrejus.netherendingenergy.common.blocks;

import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import com.agrejus.netherendingenergy.common.interfaces.IRedstoneActivatable;
import net.minecraft.tileentity.TileEntityType;

public abstract class RedstoneEnergyTile extends EnergyTile implements IRedstoneActivatable {

    protected RedstoneActivationType redstoneActivationType = RedstoneActivationType.ALWAYS_ACTIVE;

    public RedstoneEnergyTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public RedstoneActivationType getRedstoneActivationType() {
        return this.redstoneActivationType;
    }

    @Override
    public void setRedstoneActivationType(RedstoneActivationType type) {
        this.redstoneActivationType = type;
        markDirty();
    }

    protected boolean canTick() {
        if (this.redstoneActivationType != RedstoneActivationType.ALWAYS_ACTIVE) {
            int redstonePower = world.getRedstonePowerFromNeighbors(pos);
            if (this.redstoneActivationType == RedstoneActivationType.ACTIVE_WITH_SIGNAL && redstonePower == 0) {
                return false;
            }

            if (this.redstoneActivationType == RedstoneActivationType.ACTIVE_WITHOUT_SIGNAL && redstonePower > 0) {
                return false;
            }
        }
        return true;
    }
}
