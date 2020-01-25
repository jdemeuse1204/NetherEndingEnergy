package com.agrejus.netherendingenergy.blocks;

import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

// Going Off Of Wither Rose
public class CausticBellBlock extends FlowerBlock {
    public CausticBellBlock() {
        super(Effects.POISON, 8, Properties.create(Material.PLANTS)
                .sound(SoundType.PLANT)
                .doesNotBlockMovement()
                .hardnessAndResistance(1.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.CAUSTIC_BELL);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CausticBellTile();
    }

    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        Material material = block.getMaterial(state);

        // Overworld
        if (block == Blocks.DIRT || material == Material.SAND || block == Blocks.GRAVEL || material == Material.WOOD || block == Blocks.COBBLESTONE) {
            return true;
        }

        // Nether
        if (block == Blocks.NETHERRACK || block == Blocks.SOUL_SAND || block == Blocks.NETHER_BRICKS || block == Blocks.GLOWSTONE) {
            return true;
        }

        // End
        if (block == Blocks.END_STONE || block == Blocks.PURPUR_BLOCK) {
            return true;
        }

        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        VoxelShape voxelshape = this.getShape(stateIn, worldIn, pos, ISelectionContext.dummy());
        Vec3d vec3d = voxelshape.getBoundingBox().getCenter();
        double d0 = (double) pos.getX() + vec3d.x;
        double d1 = (double) pos.getZ() + vec3d.z;

        for (int i = 0; i < 3; ++i) {
            if (rand.nextBoolean()) {
                worldIn.addParticle(ParticleTypes.WITCH, d0 + (double) (rand.nextFloat() / 5.0F), (double) pos.getY() + (0.5D - (double) rand.nextFloat()), d1 + (double) (rand.nextFloat() / 5.0F), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && worldIn.getDifficulty() != Difficulty.PEACEFUL) {
            if (entityIn instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entityIn;
                if (!livingentity.isInvulnerableTo(DamageSource.WITHER)) {
                    livingentity.addPotionEffect(new EffectInstance(Effects.POISON, 40));
                }
            }
        }
    }

    // Inflict Nausea when person is too close, poison when touched
}