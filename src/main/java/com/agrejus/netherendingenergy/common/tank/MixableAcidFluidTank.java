package com.agrejus.netherendingenergy.common.tank;

import com.agrejus.netherendingenergy.common.attributes.AcidAttributes;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class MixableAcidFluidTank extends NEEFluidTank {
    public MixableAcidFluidTank(int capacity) {
        super(capacity);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        FluidStack result = super.drain(resource, action);

        if (this.fluid == FluidStack.EMPTY) {
            onContentsChanged();
        }

        return result;
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        FluidStack result = super.drain(maxDrain, action);

        if (this.fluid == FluidStack.EMPTY) {
            onContentsChanged();
        }

        return result;
    }

    public boolean isOfSameType(FluidStack fluidStack) {
        return this.fluid.getFluid() == fluidStack.getFluid();
    }

    private int executeFill(FluidStack resource) {
        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }
        if (fluid.isEmpty()) {
            onContentsChanged();
            fluid = new FluidStack(resource, Math.min(capacity, resource.getAmount()));
            return fluid.getAmount();
        }
        if (!this.isOfSameType(resource)) {
            return 0;
        }
        int filled = capacity - fluid.getAmount();

        if (resource.getAmount() < filled) {
            fluid.grow(resource.getAmount());
            filled = resource.getAmount();
        } else {
            fluid.setAmount(capacity);
        }
        return filled;
    }

    private int simulateFill(FluidStack resource) {
        if (resource.isEmpty() || !isFluidValid(resource)) {
            return 0;
        }

        // Simulate Only
        if (fluid.isEmpty()) {
            return Math.min(capacity, resource.getAmount());
        }

        // Because of NBT, fluids will be different, but we
        // are doing weighted averages and combining them.
        // Only test the type
        if (!this.isOfSameType(resource)) {
            return 0;
        }
        return Math.min(capacity - fluid.getAmount(), resource.getAmount());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {

        if (action == FluidAction.SIMULATE) {
            return simulateFill(resource);
        }

        // Lets make sure we can add before we start mixing
        int simulatedFillAmount = simulateFill(resource);
        if (simulatedFillAmount != resource.getAmount() || simulatedFillAmount == 0) {
            return 0;
        }

        float weightedStrength;
        float weightedEfficiency;
        float weightedStability;
        float weightedResponse;
        float weightedDecayRate;
        float weightedSpatial;
        AcidAttributes newAcidAttributes = FluidHelpers.deserializeAttributes(resource.getTag());

        if (this.fluid.isEmpty()) {
            weightedStrength = newAcidAttributes.getStrength();
            weightedEfficiency = newAcidAttributes.getEfficiency();
            weightedStability = newAcidAttributes.getStability();
            weightedResponse = newAcidAttributes.getResponse();
            weightedDecayRate = newAcidAttributes.getDecayRate();
            weightedSpatial = newAcidAttributes.getSpatialAmount();
        } else {
            AcidAttributes currentAcidAttributes = FluidHelpers.deserializeAttributes(this.fluid.getTag());

            int newAcidAmount = resource.getAmount();
            int currentAcidAmount = this.fluid.getAmount();

            // Compute Weighted Averages
            weightedStrength = this.getWeightedAverageStrength(newAcidAttributes, newAcidAmount, currentAcidAttributes, currentAcidAmount);
            weightedEfficiency = this.getWeightedAverageEfficiency(newAcidAttributes, newAcidAmount, currentAcidAttributes, currentAcidAmount);
            weightedStability = this.getWeightedAverageStability(newAcidAttributes, newAcidAmount, currentAcidAttributes, currentAcidAmount);
            weightedResponse = this.getWeightedAverageResponse(newAcidAttributes, newAcidAmount, currentAcidAttributes, currentAcidAmount);
            weightedDecayRate = this.getWeightedAverageDecayRate(newAcidAttributes, newAcidAmount, currentAcidAttributes, currentAcidAmount);
            weightedSpatial = this.getWeightedAverageSpatial(newAcidAttributes, newAcidAmount, currentAcidAttributes, currentAcidAmount);
        }

        // Set the fluid stack first
        int result = executeFill(resource);

        CompoundNBT tag = fluid.getTag();

        tag.putFloat("strength", weightedStrength);
        tag.putFloat("efficiency", weightedEfficiency);
        tag.putFloat("stability", weightedStability);
        tag.putFloat("response", weightedResponse);
        tag.putFloat("decayRate", weightedDecayRate);
        tag.putFloat("spatialAmount", weightedSpatial);

        //Set the tag with weighted averages
        fluid.setTag(tag);

        return result;
    }

    private float getWeightedAverageSpatial(AcidAttributes newAcidAttributes, int newAcidAmount, AcidAttributes currentAcidAttributes, int currentAcidAmount) {
        return getWeightedAverage(newAcidAmount, newAcidAttributes.getSpatialAmount(), currentAcidAmount, currentAcidAttributes.getSpatialAmount());
    }

    private float getWeightedAverageDecayRate(AcidAttributes newAcidAttributes, int newAcidAmount, AcidAttributes currentAcidAttributes, int currentAcidAmount) {
        return getWeightedAverage(newAcidAmount, newAcidAttributes.getDecayRate(), currentAcidAmount, currentAcidAttributes.getDecayRate());
    }

    private float getWeightedAverageResponse(AcidAttributes newAcidAttributes, int newAcidAmount, AcidAttributes currentAcidAttributes, int currentAcidAmount) {
        return getWeightedAverage(newAcidAmount, newAcidAttributes.getResponse(), currentAcidAmount, currentAcidAttributes.getResponse());
    }

    private float getWeightedAverageStability(AcidAttributes newAcidAttributes, int newAcidAmount, AcidAttributes currentAcidAttributes, int currentAcidAmount) {
        return getWeightedAverage(newAcidAmount, newAcidAttributes.getStability(), currentAcidAmount, currentAcidAttributes.getStability());
    }

    private float getWeightedAverageEfficiency(AcidAttributes newAcidAttributes, int newAcidAmount, AcidAttributes currentAcidAttributes, int currentAcidAmount) {
        return getWeightedAverage(newAcidAmount, newAcidAttributes.getEfficiency(), currentAcidAmount, currentAcidAttributes.getEfficiency());
    }

    private float getWeightedAverageStrength(AcidAttributes newAcidAttributes, int newAcidAmount, AcidAttributes currentAcidAttributes, int currentAcidAmount) {
        return getWeightedAverage(newAcidAmount, newAcidAttributes.getStrength(), currentAcidAmount, currentAcidAttributes.getStrength());
    }

    private float getWeightedAverage(int newAcidAmount, float newAcidAttributeValue, int currentAcidAmount, float currentAcidAttributeValue) {
        return ((newAcidAmount * newAcidAttributeValue) + (currentAcidAmount * currentAcidAttributeValue)) / (newAcidAmount + currentAcidAmount);
    }
}
