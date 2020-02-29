package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.liquid;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock.FORMED;

public class TerraReactorAcidPortTile extends TileEntity {
    public TerraReactorAcidPortTile() {
        super(ModBlocks.TERRA_REACTOR_ACID_PORT_TILE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        BlockPos controllerPosition = TerraReactorMultiBlock.INSTANCE.getControllerPosition(pos, getBlockState().get(FORMED));
        if (controllerPosition != null) {
            TerraReactorCoreTile coreTile = (TerraReactorCoreTile) world.getTileEntity(controllerPosition);
            if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && coreTile != null) {
                return coreTile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side).cast();
            }
        }

        return super.getCapability(cap, side);
    }
}