package com.agrejus.netherendingenergy.fluids;

import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class RawAcidFluid extends FlowingFluid {

    public static final FlowingFluid RAW_ACID_FLOWING = FluidHelpers.registerFluid("raw_acid_flowing", new RawAcidFluid.Flowing());
    public static final FlowingFluid RAW_ACID_STILL = FluidHelpers.registerFluid("raw_acid_still", new RawAcidFluid.Source());

    public Fluid getFlowingFluid() {
        return RAW_ACID_FLOWING;
    }

    public Fluid getStillFluid() {
        return RAW_ACID_STILL;
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public Item getFilledBucket() {
        return Items.WATER_BUCKET;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(World worldIn, BlockPos pos, IFluidState state, Random random) {
        if (!state.isSource() && !state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        } else if (random.nextInt(10) == 0) {
            worldIn.addParticle(ParticleTypes.UNDERWATER, (double)((float)pos.getX() + random.nextFloat()), (double)((float)pos.getY() + random.nextFloat()), (double)((float)pos.getZ() + random.nextFloat()), 0.0D, 0.0D, 0.0D);
        }

    }

    @Nullable
    @OnlyIn(Dist.CLIENT)
    public IParticleData getDripParticleData() {
        return ParticleTypes.DRIPPING_WATER;
    }

    protected boolean canSourcesMultiply() {
        return true;
    }

    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
        TileEntity tileentity = state.getBlock().hasTileEntity() ? worldIn.getTileEntity(pos) : null;
        Block.spawnDrops(state, worldIn.getWorld(), pos, tileentity);
    }

    public int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    public BlockState getBlockState(IFluidState state) {
        return Blocks.WATER.getDefaultState().with(FlowingFluidBlock.LEVEL, Integer.valueOf(getLevelFromState(state)));
    }

    public boolean isEquivalentTo(Fluid fluidIn) {
        return fluidIn == Fluids.WATER || fluidIn == Fluids.FLOWING_WATER;
    }

    public int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return 1;
    }

    public int getTickRate(IWorldReader p_205569_1_) {
        return 5;
    }

    public boolean func_215665_a(IFluidState p_215665_1_, IBlockReader p_215665_2_, BlockPos p_215665_3_, Fluid p_215665_4_, Direction p_215665_5_) {
        return p_215665_5_ == Direction.DOWN && !p_215665_4_.isIn(FluidTags.WATER);
    }

    protected float getExplosionResistance() {
        return 100.0F;
    }

    public static class Flowing extends RawAcidFluid {
        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(IFluidState p_207192_1_) {
            return p_207192_1_.get(LEVEL_1_8);
        }

        public boolean isSource(IFluidState state) {
            return false;
        }
    }

    public static class Source extends RawAcidFluid {
        public int getLevel(IFluidState p_207192_1_) {
            return 8;
        }

        public boolean isSource(IFluidState state) {
            return true;
        }
    }
}
