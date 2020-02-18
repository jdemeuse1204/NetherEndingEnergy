package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.*;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
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

public class CausticBellBlock extends FlowerBlock {

    public static final EnumProperty<CausticBellTrait> DOMINANT_TRAIT = EnumProperty.<CausticBellTrait>create("dominant_trait", CausticBellTrait.class);
    public static final EnumProperty<CausticBellTrait> RECESSIVE_TRAIT = EnumProperty.<CausticBellTrait>create("recessive_trait", CausticBellTrait.class);

    public CausticBellBlock() {
        super(Effects.POISON, 8, Properties.create(Material.PLANTS)
                .sound(SoundType.PLANT)
                .doesNotBlockMovement()
                .hardnessAndResistance(0)
                .lightValue(0));
        setRegistryName(RegistryNames.CAUSTIC_BELL);
    }

    // centered on the block
    public Block.OffsetType getOffsetType() {
        return OffsetType.NONE;
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
        return block == ModBlocks.TERRA_COLLECTING_STATION_BLOCK || material == Material.EARTH;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        VoxelShape voxelshape = this.getShape(stateIn, worldIn, pos, ISelectionContext.dummy());
        Vec3d vec3d = voxelshape.getBoundingBox().getCenter();
        double d0 = (double) pos.getX() + vec3d.x;
        double d1 = (double) pos.getZ() + vec3d.z;

        BasicParticleType particleType = this.isNoxious(stateIn) ? ParticleTypes.DAMAGE_INDICATOR: ParticleTypes.WITCH;

        for (int i = 0; i < 3; ++i) {
            if (rand.nextBoolean()) {
                worldIn.addParticle(particleType, d0 + (double) (rand.nextFloat() / 5.0F), (double) pos.getY() + (0.5D - (double) rand.nextFloat()), d1 + (double) (rand.nextFloat() / 5.0F), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && worldIn.getDifficulty() != Difficulty.PEACEFUL) {
            if (entityIn instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entityIn;
                if (!livingentity.isInvulnerableTo(DamageSource.WITHER)) {

                    if (this.isNoxious(state)) {
                        livingentity.addPotionEffect(new EffectInstance(Effects.WITHER, 100));
                    } else {
                        livingentity.addPotionEffect(new EffectInstance(Effects.POISON, 60));
                    }
                }
            }
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote) {
            System.out.println("DOMINANT: " +  state.get(this.DOMINANT_TRAIT));
            System.out.println("RECESSIVE: " +  state.get(this.RECESSIVE_TRAIT));
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(DOMINANT_TRAIT, RECESSIVE_TRAIT);
    }

    public boolean isNoxious(BlockState state) {
        return state.get(DOMINANT_TRAIT) == CausticBellTrait.NOXIOUS;
    }
}
