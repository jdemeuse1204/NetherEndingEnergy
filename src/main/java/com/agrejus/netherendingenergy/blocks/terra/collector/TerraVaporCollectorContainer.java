package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.interfaces.IVaporStorage;
import com.agrejus.netherendingenergy.tools.CapabilityVapor;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import com.agrejus.netherendingenergy.tools.CustomVaporStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TerraVaporCollectorContainer extends Container {

    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;

    // Exists on both server and client
    // Has slots of inventory and their links
    public TerraVaporCollectorContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ModBlocks.TERRA_VAPOR_COLLECTOR_CONTAINER, id);

        this.tileEntity = world.getTileEntity(pos);
        this.playerEntity = playerEntity;

        trackInt(new IntReferenceHolder() {
            @Override
            public int get() {
                return getVapor();
            }

            @Override
            public void set(int value) {
                tileEntity.getCapability(CapabilityVapor.VAPOR).ifPresent(w -> ((CustomVaporStorage)w).setVapor(value));
            }
        });
    }

    public int getVapor() {
        LazyOptional<IVaporStorage> capability = tileEntity.getCapability(CapabilityVapor.VAPOR);

        return capability.map(w -> w.getVaporStored()).orElse(0);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, ModBlocks.TERRA_VAPOR_COLLECTOR_BLOCK);
    }
}
