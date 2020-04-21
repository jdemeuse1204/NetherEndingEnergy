package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheOrdinaryFluid extends AcidFluid {
    public AcidOfTheOrdinaryFluid() {
        super("ordinary", 0xff88EFEC);
    }

    @Override
    protected float getDecayRate() {
        return 5;
    }

    @Override
    protected int getBaseEnergyPerTick() { return 240; }

    @Override
    protected float getStrength() {
        return .05f;
    }

    @Override
    protected float getEfficiency() {
        return .2f;
    }

    @Override
    protected float getStability() {
        return 1f;
    }

    @Override
    protected float getResponse() {
        return .01f;
    }

    @Override
    protected float getSpatial() {
        return .01f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheOrdinaryBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheOrdinaryBlock extends AcidFluid.AcidBlock {

        public AcidOfTheOrdinaryBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}
