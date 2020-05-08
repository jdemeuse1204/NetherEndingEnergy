package com.agrejus.netherendingenergy.blocks.flowers.cloud;

import com.agrejus.netherendingenergy.NetherEndingEnergyBlockStateProperties;
import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.common.enumeration.CausticCloudEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CausticCloudBlock extends Block {

    public CausticCloudBlock() {
        super(Properties.create(Material.AIR)
                .sound(SoundType.PLANT)
                .doesNotBlockMovement()
                .hardnessAndResistance(0)
                .lightValue(0));
        setRegistryName(RegistryNames.CAUSTIC_CLOUD);
        setDefaultState(getStateContainer().getBaseState().with(NetherEndingEnergyBlockStateProperties.CAUSTIC_CLOUD_1_3, 1).with(NetherEndingEnergyBlockStateProperties.CAUSTIC_CLOUD_EFFECT, CausticCloudEffect.WITHER));
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty(); // remove hit box
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isRemote && world.getDifficulty() != Difficulty.PEACEFUL) {
            if (entity instanceof LivingEntity) {
                CausticCloudEffect effect = state.get(NetherEndingEnergyBlockStateProperties.CAUSTIC_CLOUD_EFFECT);
                LivingEntity livingentity = (LivingEntity) entity;
                switch (effect) {
                    default:
                    case POISON:
                        livingentity.addPotionEffect(new EffectInstance(Effects.POISON, 200));
                        break;
                    case WITHER:
                        livingentity.addPotionEffect(new EffectInstance(Effects.WITHER, 120));
                        break;
                    case NAUSEA:
                        livingentity.addPotionEffect(new EffectInstance(Effects.WITHER, 80));
                        break;
                    case WEAKNESS:
                        livingentity.addPotionEffect(new EffectInstance(Effects.WITHER, 400));
                        break;
                }
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NetherEndingEnergyBlockStateProperties.CAUSTIC_CLOUD_1_3, NetherEndingEnergyBlockStateProperties.CAUSTIC_CLOUD_EFFECT);
    }
}
