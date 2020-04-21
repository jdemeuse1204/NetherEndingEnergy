package com.agrejus.netherendingenergy.common.attributes;

import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidAttributes;

import java.util.function.BiFunction;

public class CustomFluidAttributes extends FluidAttributes {

    private final float strength;
    private final float efficiency;
    private final float stability;
    private final float response;
    private final float spatial;
    private final int spatialAmount;
    private final int baseEnergyPerTick;
    private final float decayRate;

    protected CustomFluidAttributes(CustomBuilder builder, Fluid fluid) {
        super(builder, fluid);
        this.strength = builder.strength;
        this.efficiency = builder.efficiency;
        this.stability = builder.stability;
        this.response = builder.response;
        this.spatial = builder.spatial;
        this.spatialAmount = builder.spatialAmount;
        this.baseEnergyPerTick = builder.baseEnergyPerTick;
        this.decayRate = builder.decayRate;
    }

    public final float getStrength() {
        return this.strength;
    }

    public final float getEfficiency() {
        return this.efficiency;
    }

    public final float getStability() {
        return this.stability;
    }

    public final float getResponse() {
        return this.response;
    }

    public final float getSpatial() {
        return this.spatial;
    }

    public final float getDecayRate() {
        return this.decayRate;
    }

    public final int getSpatialAmount() {
        return this.spatialAmount;
    }

    public final int getBaseEnergyPerTick() {
        return this.baseEnergyPerTick;
    }

    public static CustomBuilder builder(ResourceLocation stillTexture, ResourceLocation flowingTexture) {
        return new CustomBuilder(stillTexture, flowingTexture, CustomFluidAttributes::new);
    }

    public static class CustomBuilder extends Builder {
        private BiFunction<CustomBuilder, Fluid, FluidAttributes> factory;
        private float strength = 1;
        private float efficiency = 1;
        private float stability = 1;
        private float response = 1;
        private float spatial = 1;
        private int spatialAmount = 1;
        private int baseEnergyPerTick = 360;
        private float decayRate = 1;

        protected CustomBuilder(ResourceLocation stillTexture, ResourceLocation flowingTexture, BiFunction<CustomBuilder, Fluid, FluidAttributes> factory) {
            super(stillTexture, flowingTexture, null);
            this.factory = factory;
        }

        public final CustomBuilder response(float response) {
            this.response = response;
            return this;
        }

        public final CustomBuilder stability(float stability) {
            this.stability = stability;
            return this;
        }

        public final CustomBuilder efficiency(float efficiency) {
            this.efficiency = efficiency;
            return this;
        }

        public final CustomBuilder strength(float strength) {
            this.strength = strength;
            return this;
        }

        public final CustomBuilder spatial(float spatial) {
            this.spatial = spatial;
            return this;
        }

        public final CustomBuilder spatialAmount(int spatialAmount) {
            this.spatialAmount = spatialAmount;
            return this;
        }

        public final CustomBuilder baseEnergyPerTick(int amount) {
            this.baseEnergyPerTick = amount;
            return this;
        }

        public final CustomBuilder decayRate(float decayRate) {
            this.decayRate = decayRate;
            return this;
        }

        @Override
        public FluidAttributes build(Fluid fluid) {
            return factory.apply(this, fluid);
        }
    }
}
