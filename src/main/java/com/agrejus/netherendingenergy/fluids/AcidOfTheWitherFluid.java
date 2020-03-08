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

public class AcidOfTheWitherFluid  extends AcidFluid {
    public AcidOfTheWitherFluid() {
        super("wither", 0xff343434);
    }

    @Override
    protected int getUses() {
        return 25;
    }

    @Override
    protected float getStrength() {
        return 8f;
    }

    @Override
    protected float getEfficiency() {
        return 7f;
    }

    @Override
    protected float getStability() {
        return .1f;
    }

    @Override
    protected float getResponse() {
        return 3f;
    }

    @Override
    protected float getSpatial() {
        return .1f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheWitherFluid.AcidOfTheWitherBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheWitherBlock extends AcidFluid.AcidBlock {

        public AcidOfTheWitherBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }

        @Override
        public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
            if (!worldIn.isRemote && worldIn.getDifficulty() != Difficulty.PEACEFUL) {
                if (entityIn instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity) entityIn;
                    if (!livingentity.isInvulnerableTo(DamageSource.WITHER)) {
                        livingentity.addPotionEffect(new EffectInstance(Effects.WITHER, 100));
                    }
                }
            }
        }
    }
}