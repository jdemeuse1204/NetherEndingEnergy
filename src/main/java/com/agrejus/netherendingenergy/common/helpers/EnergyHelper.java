package com.agrejus.netherendingenergy.common.helpers;

import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.concurrent.atomic.AtomicInteger;

public class EnergyHelper {
    private static int trySendEnergy(CustomEnergyStorage customEnergyStorage, World world, BlockPos pos, Direction direction) {
        AtomicInteger totalSent = new AtomicInteger(0);
        TileEntity tileEntity = world.getTileEntity(pos.offset(direction));

        if (tileEntity != null) {
            tileEntity.getCapability(CapabilityEnergy.ENERGY, direction).ifPresent(handler -> {

                // Do we have enough and can the destination receive energy?
                if (customEnergyStorage.getEnergyStored() > 0 && handler.canReceive()) {

                    int amountToFill = customEnergyStorage.extractEnergy(customEnergyStorage.getMaxExtract(), true);
                    int amountReceived = handler.receiveEnergy(amountToFill, false);
                    customEnergyStorage.extractEnergy(amountReceived, false);
                    totalSent.getAndAdd(amountReceived);
                }
            });
        }

        return totalSent.get();
    }

    public static int trySendEnergyAllDirections(LazyOptional<IEnergyStorage> energyStore, World world, BlockPos pos) {
        AtomicInteger totalSent = new AtomicInteger(0);

        if (energyStore == null) {
            return 0;
        }

        energyStore.ifPresent(w -> {

            if ((w instanceof CustomEnergyStorage) == false) {
                return;
            }

            CustomEnergyStorage customEnergyStorage = (CustomEnergyStorage) w;

            if (customEnergyStorage.getEnergyStored() > 0) {

                for (Direction direction : Direction.values()) {

                    int amountSent = trySendEnergy(customEnergyStorage, world, pos, direction);
                    totalSent.getAndAdd(amountSent);
                }
            }
        });

        return totalSent.get();
    }
}
