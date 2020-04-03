package com.agrejus.netherendingenergy.blocks.creative.energy;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CreativeEnergyStoreTile extends TileEntity implements ITickableTileEntity {

    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(10000000, 10000000, 10000000);
    }

    public CreativeEnergyStoreTile() {
        super(ModBlocks.Creative.CREATIVE_ENERGY_STORE_TILE);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityEnergy.ENERGY) {
            return energy.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void tick() {
        this.sendOutPower();
    }

    private void sendOutPower() {
        energy.ifPresent(w -> {

            CustomEnergyStorage energyStore = (CustomEnergyStorage) w;

            // Its Creative!
            energyStore.addEnergy(10000000);

            for (Direction direction : Direction.values()) {
                if (energyStore.getEnergyStored() > 0) {

                    TileEntity tileEntity = world.getTileEntity(pos.offset(direction));

                    if (tileEntity != null) {
                        tileEntity.getCapability(CapabilityEnergy.ENERGY, direction.getOpposite()).ifPresent(x -> {
                            if (x.canReceive() == false) {
                                return;
                            }

                            x.receiveEnergy(1000, false);

                            // Add Back Energy Received
                            energyStore.addEnergy(1000);
                        });
                    }
                }
            }
        });
    }
}
