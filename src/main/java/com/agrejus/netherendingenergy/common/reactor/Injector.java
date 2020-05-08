package com.agrejus.netherendingenergy.common.reactor;

import com.agrejus.netherendingenergy.blocks.terra.reactor.injector.TerraReactorInjectorTile;
import net.minecraft.util.Direction;

public class Injector {
    private final Direction facing;
    private final boolean isActive;
    private final TerraReactorInjectorTile tileEntity;

    public Injector(TerraReactorInjectorTile tileEntity, Direction facing, boolean isActive) {
        this.facing = facing;
        this.isActive = isActive;
        this.tileEntity = tileEntity;
    }

    public Direction getFacing() {
        return facing;
    }

    public boolean isActive() {
        return isActive;
    }

    public TerraReactorInjectorTile getTileEntity() {
        return tileEntity;
    }
}
