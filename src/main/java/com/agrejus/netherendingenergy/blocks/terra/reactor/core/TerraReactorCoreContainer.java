package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorReactorMultiBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.injector.TerraReactorInjectorTile;
import com.agrejus.netherendingenergy.client.gui.container.RedstoneActivatableContainer;
import com.agrejus.netherendingenergy.common.container.InventoryContainerBase;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import com.agrejus.netherendingenergy.common.handlers.ReadOnlySlotItemHandler;
import com.agrejus.netherendingenergy.common.models.BlockInformation;
import com.agrejus.netherendingenergy.common.models.TopLeftPos;
import com.agrejus.netherendingenergy.common.reactor.Injector;
import com.agrejus.netherendingenergy.common.reactor.ReactorSlotType;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class TerraReactorCoreContainer extends RedstoneActivatableContainer<TerraReactorCoreTile> {

    private IIntArray tracking;

    // Exists on both server and client
    // Has slots of inventory and their links
    public TerraReactorCoreContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        this(id, world, pos, playerInventory, playerEntity, new IntArray(13));
    }

    public TerraReactorCoreContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity, IIntArray intArray) {
        super(ModBlocks.TERRA_REACTOR_CORE_CONTAINER, id, world, pos, playerInventory, playerEntity);

        layoutInterfaceSlots();

        layoutAllPlayerSlots();

        tracking = intArray;

        trackIntArray(intArray);
    }

    public List<Injector> getInjectors() {
        return this.tileEntity.getInjectors();
    }

    public void layoutPlayerHotbarSlots() {
        layoutPlayerHotbar(8, 103);
    }

    public void layoutInterfaceSlots() {
        Map<ReactorSlotType, TopLeftPos> slotLocations = TerraReactorReactorMultiBlock.INSTANCE.getSlotLocations();

        // Add the burn and backlog slot
        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {
            TopLeftPos backlogSlotPosition = slotLocations.get(ReactorSlotType.MainBacklog);
            addSlot(new SlotItemHandler(w, 0, backlogSlotPosition.getLeft(), backlogSlotPosition.getTop()));
        });

        List<Injector> injectors = tileEntity.getInjectors();

        int size = injectors.size();
        for (int i = 0; i < size; i++) {

            Injector injector = injectors.get(i);
            TerraReactorInjectorTile injectorTile = injector.getTileEntity();

            if (injectorTile == null) {
                continue;
            }

            injectorTile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {

                ReactorSlotType slotType;
                switch (injector.getFacing()) {
                    default:
                    case NORTH:
                        slotType = ReactorSlotType.NorthInjector;
                        break;
                    case SOUTH:
                        slotType = ReactorSlotType.SouthInjector;
                        break;
                    case EAST:
                        slotType = ReactorSlotType.EastInjector;
                        break;
                    case WEST:
                        slotType = ReactorSlotType.WestInjector;
                        break;
                }

                TopLeftPos slotPosition = slotLocations.get(slotType);
                addSlot(new ReadOnlySlotItemHandler(w, 0, slotPosition.getLeft(), slotPosition.getTop()));
            });
        }
    }

    public void layoutAllPlayerSlots() {
        layoutPlayerInventorySlots(8, 103);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return canInteractWith(ModBlocks.TERRA_REACTOR_CORE_BLOCK);
    }

    public int getAcidStored() {
        return this.tracking.get(1);
    }

    public int getMaxAcidStored() {
        return this.tracking.get(0);
    }

    public int getEnergyStored() {
        return this.tracking.get(2);
    }

    public int getMaxEnergyStored() {
        return this.tracking.get(3);
    }

    public int getGeneratedEnergyPerTick() {
        return this.tracking.get(4);
    }

    public int getBurnItemTicksLeft() {
        return this.tracking.get(5);
    }

    public int getBurnItemTotalTicks() {
        return this.tracking.get(6);
    }

    public int getUsesLeftInjectorOne() {
        return this.tracking.get(7);
    }

    public int getUsesLeftInjectorTwo() {
        return this.tracking.get(8);
    }

    public int getUsesLeftInjectorThree() {
        return this.tracking.get(9);
    }

    public int getUsesLeftInjectorFour() {
        return this.tracking.get(10);
    }

    public int getHeat() {
        return this.tracking.get(11);
    }

    public int getMaxHeat() {
        return this.tracking.get(12);
    }

    public FluidStack getFluid() {
        return this.tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).map(w -> w.getFluidInTank(0)).orElse(null);
    }

    @Override
    public RedstoneActivationType getRedstoneActivationType() {
        return null;
    }

    @Override
    public void changeRedstoneActivationType(RedstoneActivationType type) {

    }
}
