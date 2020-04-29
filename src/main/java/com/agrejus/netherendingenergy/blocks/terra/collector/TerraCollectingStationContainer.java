package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.client.gui.container.RedstoneActivatableContainer;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class TerraCollectingStationContainer extends RedstoneActivatableContainer<TerraCollectingStationTile> {

    private TerraCollectingStationTile tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    private IIntArray tracking;

    // Exists on both server and client
    // Has slots of inventory and their links
    public TerraCollectingStationContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        this(id, world, pos, playerInventory, playerEntity, new IntArray(18));
    }

    public TerraCollectingStationContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity, IIntArray intArray) {
        super(ModBlocks.TERRA_COLLECTING_STATION_CONTAINER, id, world, pos, playerInventory, playerEntity);

        this.tileEntity = (TerraCollectingStationTile) world.getTileEntity(pos);
        this.playerEntity = playerEntity;
        this.playerInventory = new InvWrapper(playerInventory);

        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {
            addSlot(new SlotItemHandler(w, 0, 21, 55));
        });

        // where is the top left slot? This is the player inventory
        layoutPlayerInventorySlots(8, 103);

        tracking = intArray;

        trackIntArray(intArray);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, ModBlocks.TERRA_COLLECTING_STATION_BLOCK);
    }

    public int getOutputTankCapacity() {
        return this.tracking.get(0);
    }

    public int getOutputFluidAmount() {
        return this.tracking.get(1);
    }

    public int getInputTankCapacity() {
        return this.tracking.get(2);
    }

    public int getInputFluidAmount() {
        return this.tracking.get(3);
    }

    public int getRefineTicks() {
        return this.tracking.get(4);
    }

    public int getTotalTicksToRefine() {
        return this.tracking.get(5);
    }

    public int getEnergyStored() {
        return this.tracking.get(6);
    }

    public int getMaxEnergyStored() {
        return this.tracking.get(7);
    }

    public int getOutputTankFluidColor() {
        return this.tracking.get(8);
    }

    public int getInputTankFluidColor() {
        return this.tracking.get(9);
    }

    public int getCollectionTick() {
        return this.tracking.get(10);
    }

    public int getCollectionTotalTicks() {
        return this.tracking.get(11);
    }

    public float getCycleCollectionAmount() {
        return (float) this.tracking.get(12) / 10000f;
    }

    public int getEnergyPerTick() {
        return this.tracking.get(13);
    }

    public float getBellYield() {
        return (float) this.tracking.get(14) / 10000f;
    }

    public float getBellStrength() {
        return (float) this.tracking.get(15) / 10000f;
    }

    public float getBellPurity() {
        return (float) this.tracking.get(16) / 10000f;
    }

    public boolean hasBellPlanted() {
        return this.tracking.get(17) == 1 ? true : false;
    }

    @OnlyIn(Dist.CLIENT)
    public int getProcessProgressionScaled() {

        int i = this.getTotalTicksToRefine() - this.getRefineTicks();//this.field_217064_e.get(2); // cook Time
        int j = this.getTotalTicksToRefine();//this.field_217064_e.get(3); // cook Time Total

        // 24 is the width of the progression arrow for the GUI
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }

    public void changeRedstoneActivationType(RedstoneActivationType type) {
        this.tileEntity.changeRedstoneActivationType(type);
    }

    public RedstoneActivationType getRedstoneActivationType() {
        return this.tileEntity.getRedstoneActivationType();
    }
}
