package com.agrejus.netherendingenergy.fluids;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.setup.ModSetup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.*;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;
import java.util.function.Supplier;

public class RawAcidFluid extends AcidFluid {

    public RawAcidFluid() {
        super("raw_acid_fluid", NetherEndingEnergy.MODID + ":block/fluids/raw_acid_still", NetherEndingEnergy.MODID + ":block/fluids/raw_acid_flow", 0xffB200DE);
    }

    @Override
    protected int getBaseEnergyPerTick() { return 0; }

    @Override
    protected float getSpatial() {
        return 0;
    }

    @Override
    protected float getStrength() {
        return 0;
    }

    @Override
    protected float getEfficiency() {
        return 0;
    }

    @Override
    protected float getStability() {
        return 0;
    }

    @Override
    protected float getResponse() {
        return 0;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new RawAcidBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    @Override
    protected float getDecayRate() {
        return 0;
    }

    public static class RawAcidBlock extends AcidFluid.AcidBlock {
        public RawAcidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }
    }
}