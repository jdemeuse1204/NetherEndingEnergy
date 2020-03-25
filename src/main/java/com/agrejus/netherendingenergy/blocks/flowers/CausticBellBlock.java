package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;
import com.agrejus.netherendingenergy.items.ModItems;
import com.agrejus.netherendingenergy.particle.ModParticles;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
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
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Random;

public class CausticBellBlock extends FlowerBlock {

    public static final EnumProperty<CausticBellTrait> SUPERIOR_TRAIT = EnumProperty.<CausticBellTrait>create("superior_trait", CausticBellTrait.class);
    public static final EnumProperty<CausticBellTrait> INFERIOR_TRAIT = EnumProperty.<CausticBellTrait>create("inferior_trait", CausticBellTrait.class);
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
        return new CausticBellTile(state.get(this.SUPERIOR_TRAIT), state.get(this.INFERIOR_TRAIT), state.get(this.RECESSIVE_TRAIT));
    }

    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        Block block = state.getBlock();
        Material material = block.getMaterial(state);

        if (material == Material.EARTH || block == ModBlocks.CAUSTIC_ROOTS_BLOCK) {
            return true;
        }

        if (block == ModBlocks.TERRA_COLLECTING_STATION_BLOCK) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if (tileEntity != null) {
                ItemStack stack = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(w -> w.getStackInSlot(0)).orElse(ItemStack.EMPTY);

                return stack.getItem() == Items.DIRT;
            }
        }

        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        VoxelShape voxelshape = this.getShape(stateIn, worldIn, pos, ISelectionContext.dummy());
        Vec3d vec3d = voxelshape.getBoundingBox().getCenter();
        double d0 = (double) pos.getX() + vec3d.x;
        double d1 = (double) pos.getZ() + vec3d.z;

        BasicParticleType particleType = this.isNoxious(pos, worldIn) ? ParticleTypes.DAMAGE_INDICATOR: ParticleTypes.WITCH;

        for (int i = 0; i < 3; ++i) {
            if (rand.nextBoolean()) {
                worldIn.addParticle(ModParticles.CAUSTIC_CLOUD, d0 + (double) (rand.nextFloat() / 5.0F), (double) pos.getY() + (0.5D - (double) rand.nextFloat()), d1 + (double) (rand.nextFloat() / 5.0F), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {

        if(world.isRemote) {
            return;
        }

        if (player.getHeldItemMainhand().getItem() != Items.SHEARS) {
            spawnAsEntity(world, pos, new ItemStack(ModItems.CAUSTIC_MASH, 1));
            return;
        }

        super.harvestBlock(world, player, pos, state, te, stack);
    }

    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && worldIn.getDifficulty() != Difficulty.PEACEFUL) {
            if (entityIn instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entityIn;
                if (!livingentity.isInvulnerableTo(DamageSource.WITHER)) {

                    if (this.isNoxious(pos, worldIn)) {
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
            CausticBellTile tile = (CausticBellTile)worldIn.getTileEntity(pos);
            System.out.println("Tile SUPERIOR: " +  tile.getSuperiorTrait());
            System.out.println("Tile INFERIOR: " +  tile.getInferiorTrait());
            System.out.println("Tile RECESSIVE: " +  tile.getRecessiveTrait());
            System.out.println("YIELD: " +  tile.getYield());
            System.out.println("PURITY: " +  tile.getPurity());
            System.out.println("STRENGTH: " +  tile.getStrength());

            //worldIn.setBlockState(pos.down().east(), ModBlocks.CAUSTIC_DIRT_BLOCK.getDefaultState(), 3);
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(SUPERIOR_TRAIT, INFERIOR_TRAIT, RECESSIVE_TRAIT);
    }

    public boolean isNoxious(BlockPos pos, World world) {
        CausticBellTile tile = (CausticBellTile)world.getTileEntity(pos);
        return tile.getSuperiorTrait() == CausticBellTrait.NOXIOUS;
    }
}
