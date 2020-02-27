package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.redstone;

import com.agrejus.netherendingenergy.Config;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ReactorRedstonePortTile extends TileEntity {
    // Do we need a tile entity? I dont think so
    public ReactorRedstonePortTile() {
        super(ModBlocks.REACTOR_REDSTONE_PORT_TILE);
    }
}
