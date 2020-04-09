package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.client.gui.container.RedstoneActivatableContainer;
import com.agrejus.netherendingenergy.common.container.InventoryContainerBase;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import com.agrejus.netherendingenergy.common.handlers.ReactorInventoryStackHandler;
import com.agrejus.netherendingenergy.common.handlers.ReadOnlySlotItemHandler;
import com.agrejus.netherendingenergy.common.models.MixerRecipe;
import com.agrejus.netherendingenergy.common.models.TopLeftPos;
import com.agrejus.netherendingenergy.common.reactor.ReactorSlotType;
import com.agrejus.netherendingenergy.network.NetherEndingEnergyNetworking;
import com.agrejus.netherendingenergy.network.PacketChangeRedstoneActivationType;
import com.agrejus.netherendingenergy.network.PacketEmptyTank;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.NetworkManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TerraMixerContainer extends RedstoneActivatableContainer<TerraMixerTile> {

    private TerraMixerTile tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    private IIntArray tracking;
    private World world;
    private BlockPos pos;

    // what inventories do I have?

    // Exists on both server and client
    // Has slots of inventory and their links
    public TerraMixerContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        this(id, world, pos, playerInventory, playerEntity, new IntArray(12));

        this.world = world;
        this.pos = pos;
    }

    public TerraMixerContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity, IIntArray intArray) {
        super(ModBlocks.TERRA_MIXER_CONTAINER, id, world, pos, playerInventory, playerEntity);

        this.world = world;
        this.tileEntity = (TerraMixerTile)this.world.getTileEntity(pos);

        this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {
            addSlot(new SlotItemHandler(w, 0, 10, 33));
            addSlot(new ReadOnlySlotItemHandler(w, 1, 55, 33));
        });

        this.tileEntity.getAcidInputSlotInventory().ifPresent(w -> {
            addSlot(new SlotItemHandler(w, 0, 75, 33));
        });
        this.tileEntity.getAcidResultSlotInventory().ifPresent(w -> {
            addSlot(new SlotItemHandler(w, 0, 66, 75));
        });
        this.tileEntity.getOutputSlotInventory().ifPresent(w -> {
            addSlot(new SlotItemHandler(w, 0, 152, 81));
        });

        this.playerEntity = playerEntity;
        this.tracking = intArray;

        layoutPlayerInventorySlots(8, 103);

        trackIntArray(intArray);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, ModBlocks.TERRA_MIXER_BLOCK);
    }

    public int getOutputTankCapacity() {
        return this.tracking.get(0);
    }

    public int getOutputFluidAmount() {
        return this.tracking.get(1);
    }

    public int getEnergyStored() {
        return this.tracking.get(4);
    }

    public int getMaxEnergyStored() {
        return this.tracking.get(5);
    }

    public int getInputTankFluidColor() {
        return this.tracking.get(6);
    }

    public void voidInputTank() {
        this.tileEntity.voidInputTank();
    }

    public void voidOutputTank() {
        this.tileEntity.voidOutputTank();
    }

    public MixerRecipe getCurrentRecipe() {
        return this.tileEntity.getCurrentRecipe();
    }

    public void changeRedstoneActivationType(RedstoneActivationType type) {
        this.tileEntity.changeRedstoneActivationType(type);
    }

    public RedstoneActivationType getRedstoneActivationType() {
        return this.tileEntity.getRedstoneActivationType();
    }


    public BlockPos currentPosition() {
        return tileEntity.getPos();
    }

    public FluidStack getInputTankFluid() {
        return this.tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).map(w -> w.getFluidInTank(0)).orElse(FluidStack.EMPTY);
    }

    public FluidStack getOutputTankFluid() {
        return this.tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.EAST).map(w -> w.getFluidInTank(0)).orElse(FluidStack.EMPTY);
    }

    public int getInputTankCapacity() {
        return this.tracking.get(2);
    }

    public int getInputFluidAmount() {
        return this.tracking.get(3);
    }

    public int getDestructibleItemTicks() {
        return this.tracking.get(7);
    }

    public int getDestructibleItemTotalTicks() {
        return this.tracking.get(8);
    }

    public int getOutputTankFluidColor() {
        return this.tracking.get(9);
    }

    public int getEnergyPerTick() {
        return this.tracking.get(10);
    }

    public int getBurningItemUsageCount() {
        return this.tracking.get(11);
    }
}
