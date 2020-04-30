package com.agrejus.netherendingenergy.common.fluids;

import com.agrejus.netherendingenergy.common.attributes.CustomFluidAttributes;
import com.agrejus.netherendingenergy.common.attributes.AcidAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;

public final class FluidHelpers {
    public static AcidAttributes deserializeAttributes(CompoundNBT tag) {
        float strength = tag.getFloat("strength");
        float efficiency = tag.getFloat("efficiency");
        float stability = tag.getFloat("stability");
        float response = tag.getFloat("response");
        float spatialAmount = tag.getFloat("spatialAmount");
        int basePerTick = tag.getInt("basePerTick");
        float spatial = tag.getFloat("spatial");
        float decayRate = tag.getFloat("decayRate");
        return new AcidAttributes(strength, efficiency, stability, response, spatial, spatialAmount, basePerTick, decayRate);
    }

    public static void serializeCustomFluidAttributes(CompoundNBT tag, AcidAttributes attributes) {
        tag.putFloat("strength", attributes.getStrength());
        tag.putFloat("efficiency", attributes.getEfficiency());
        tag.putFloat("stability", attributes.getStability());
        tag.putFloat("response", attributes.getResponse());
        tag.putInt("basePerTick", attributes.getBaseEnergyPerTick());
        tag.putFloat("spatialAmount", attributes.getSpatialAmount());
        tag.putFloat("spatial", attributes.getSpatial());
        tag.putFloat("decayRate", attributes.getDecayRate());
    }

    public static void serializeCustomFluidAttributes(CompoundNBT tag, CustomFluidAttributes attributes, float spatialAmount) {
        tag.putFloat("strength", attributes.getStrength());
        tag.putFloat("efficiency", attributes.getEfficiency());
        tag.putFloat("stability", attributes.getStability());
        tag.putFloat("response", attributes.getResponse());
        tag.putInt("basePerTick", attributes.getBaseEnergyPerTick());
        tag.putFloat("spatialAmount", spatialAmount);
        tag.putFloat("spatial", attributes.getSpatial());
        tag.putFloat("decayRate", attributes.getDecayRate());
    }

    public static void fillBucketFromTankAndPutInInventory(FluidTank tank, IItemHandler emptyBucketInventory, IItemHandler resultInventory) {
        fillBucketFromTankAndPutInInventory(tank, emptyBucketInventory, resultInventory, null);
    }

    public static void fillBucketFromTankAndPutInInventory(FluidTank tank, IItemHandler emptyBucketInventory, IItemHandler resultInventory, Runnable onDrainSuccess) {
        ItemStack stack = emptyBucketInventory.getStackInSlot(0);

        if (stack.isEmpty() == false) {
            Item item = stack.getItem();
            if (item == Items.BUCKET) {
                FluidStack fluidInTank = tank.getFluidInTank(0);
                if (fluidInTank.getAmount() >= FluidAttributes.BUCKET_VOLUME) {
                    FluidStack drained = tank.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);

                    if (drained.getAmount() == FluidAttributes.BUCKET_VOLUME) {

                        ItemStack outputItemStack = FluidUtil.getFilledBucket(drained);

                        ItemStack insertResult = resultInventory.insertItem(0, outputItemStack, true);

                        if (insertResult.isEmpty() == true) {
                            tank.drain(FluidAttributes.BUCKET_VOLUME, IFluidHandler.FluidAction.EXECUTE);
                            resultInventory.insertItem(0, outputItemStack, false);
                            emptyBucketInventory.extractItem(0, 1, false);

                            if (onDrainSuccess != null) {
                                onDrainSuccess.run();
                            }
                        }
                    }
                }
            }
        }
    }
}
