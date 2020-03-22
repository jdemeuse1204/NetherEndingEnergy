package com.agrejus.netherendingenergy.fluids;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class AcidOfTheBlazeFluid extends AcidFluid {

    public AcidOfTheBlazeFluid() {
        super("blaze", 0xffFFD528);
    }

    @Override
    protected float getDecayRate() {
        return 1;
    }

    @Override
    protected int getBaseEnergyPerTick() { return 380; }

    @Override
    protected float getStrength() {
        return 3.5f;
    }

    @Override
    protected float getEfficiency() {
        return 4;
    }

    @Override
    protected float getStability() {
        return .2f;
    }

    @Override
    protected float getResponse() {
        return 4f;
    }

    @Override
    protected float getSpatial() {
        return .035f;
    }

    @Override
    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return new AcidOfTheBlazeBlock(stillFluid, Block.Properties.create(Material.WATER));
    }

    public static class AcidOfTheBlazeBlock extends AcidFluid.AcidBlock {

        public AcidOfTheBlazeBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
            super(supplier, properties);
        }

        @Override
        public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {

            if (!worldIn.isRemote && worldIn.getDifficulty() != Difficulty.PEACEFUL) {
                if (entityIn instanceof LivingEntity) {
                    LivingEntity livingentity = (LivingEntity) entityIn;
                    if (!livingentity.isInvulnerableTo(DamageSource.WITHER)) {
                        livingentity.setFire(2);
                    }
                }
            }
        }
    }
}