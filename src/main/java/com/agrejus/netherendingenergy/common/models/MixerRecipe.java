package com.agrejus.netherendingenergy.common.models;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MixerRecipe implements INBTSerializable<CompoundNBT> {

    private Fluid resultFluid;
    private Fluid ingredientFluid;
    private List<Item> items;
    private int yield;
    private int itemUsagePerProcessingUnit;
    private int acidUsagePerProcessingUnit = 50;
    private int processingUnitTickLength;
    private int resultFluidProcessingUnitAmount;

    // to make 1 mB we need 1mB of acid and 8 items
    // Every x ticks, use 8 items and 1mB of acid

    public MixerRecipe() {
        this.resultFluidProcessingUnitAmount = 50;
        this.processingUnitTickLength = 60;
        this.items = new ArrayList<>();
    }

    public MixerRecipe(Fluid resultFluid, Fluid ingredientFluid, int yield, Item... items) {
        this.resultFluid = resultFluid;
        this.ingredientFluid = ingredientFluid;
        this.yield = yield;
        this.itemUsagePerProcessingUnit = (yield / 1000);
        this.resultFluidProcessingUnitAmount = 50;
        this.processingUnitTickLength = 60;
        this.items = Arrays.asList(items);
    }

    public int getResultFluidProcessingUnitAmount() {
        return resultFluidProcessingUnitAmount;
    }

    public int getItemUsagePerProcessingUnit() {
        return itemUsagePerProcessingUnit;
    }

    public int getAcidUsagePerProcessingUnit() {
        return acidUsagePerProcessingUnit;
    }

    public int getProcessingUnitTickLength() {
        return processingUnitTickLength;
    }

    public boolean isItemValid(Item item) {
        return this.items.contains(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public int getYield() {
        return yield;
    }

    public Fluid getResultFluid() {
        return resultFluid;
    }

    public Fluid getIngredientFluid() {
        return ingredientFluid;
    }

    @Override
    public CompoundNBT serializeNBT() {

        CompoundNBT tag = new CompoundNBT();
        tag.putInt("yield", this.yield);
        tag.putInt("itemUsagePerProcessingUnit", this.itemUsagePerProcessingUnit);
        tag.putInt("acidUsagePerProcessingUnit", this.acidUsagePerProcessingUnit);
        tag.putInt("processingUnitTickLength", this.processingUnitTickLength);
        tag.putInt("resultFluidProcessingUnitAmount", this.resultFluidProcessingUnitAmount);

        if (this.resultFluid != null) {
            tag.putString("resultFluid", this.resultFluid.getRegistryName().toString());
        }

        if (this.ingredientFluid != null) {
            tag.putString("ingredientFluid", this.ingredientFluid.getRegistryName().toString());
        }

        int size = this.items.size();

        for (int i = 0; i < size; i++) {
            Item item = this.items.get(i);
            tag.putString("Items" + i, item.getRegistryName().toString());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.yield = nbt.getInt("yield");
        this.itemUsagePerProcessingUnit = nbt.getInt("itemUsagePerProcessingUnit");
        this.acidUsagePerProcessingUnit = nbt.getInt("acidUsagePerProcessingUnit");
        this.processingUnitTickLength = nbt.getInt("processingUnitTickLength");
        this.resultFluidProcessingUnitAmount = nbt.getInt("resultFluidProcessingUnitAmount");

        ResourceLocation resultFluidResourceLocation = new ResourceLocation(nbt.getString("resultFluid"));
        this.resultFluid = ForgeRegistries.FLUIDS.getValue(resultFluidResourceLocation);

        ResourceLocation ingredientFluidResourceLocation = new ResourceLocation(nbt.getString("ingredientFluid"));
        this.ingredientFluid = ForgeRegistries.FLUIDS.getValue(ingredientFluidResourceLocation);

        this.items = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            String itemRegistryName = nbt.getString("Items" + i);
            if (itemRegistryName == null || itemRegistryName.equals("")) {
                break;
            }
            ResourceLocation itemResourceLocation = new ResourceLocation(itemRegistryName);
            Item item = ForgeRegistries.ITEMS.getValue(itemResourceLocation);

            if (item == null) {
                continue;
            }

            this.items.add(item);
        }
    }
}
