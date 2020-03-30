package com.agrejus.netherendingenergy.common.models;

import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

import java.util.Arrays;
import java.util.List;

public class MixerRecipe {

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
}
