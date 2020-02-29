package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.item;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorConfig;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock.FORMED;

public class TerraReactorItemPortTile extends TileEntity {
    public TerraReactorItemPortTile() {
        super(ModBlocks.TERRA_REACTOR_ITEM_PORT_TILE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        BlockPos controllerPosition = TerraReactorMultiBlock.INSTANCE.getControllerPosition(pos, getBlockState().get(FORMED));
        if (controllerPosition != null) {
            TerraReactorCoreTile coreTile = (TerraReactorCoreTile) world.getTileEntity(controllerPosition);
            if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && coreTile != null) {
                return coreTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side).cast();
            }
        }


        return super.getCapability(cap, side);
    }

}
