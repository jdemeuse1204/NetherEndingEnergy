package com.agrejus.netherendingenergy.blocks.base.reactor;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock.FORMED;

public abstract class ReactorCapabilityTileEntity extends TileEntity {

    public ReactorCapabilityTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    protected <T> LazyOptional<T> getCapabilityFromCore(@Nonnull Capability<T> cap, @Nullable Direction side) {
        BlockPos controllerPosition = TerraReactorReactorMultiBlock.INSTANCE.getControllerPosition(pos, getBlockState().get(FORMED));
        if (controllerPosition != null) {
            TileEntity tileEntity = world.getTileEntity(controllerPosition);

            if (tileEntity instanceof TerraReactorCoreTile) {
                TerraReactorCoreTile coreTile = (TerraReactorCoreTile) world.getTileEntity(controllerPosition);
                return coreTile.getCapability(cap, side);
            }
        }

        return super.getCapability(cap, side);
    }
}
