
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

public class RawAcidFluid {
    protected RegistryObject<FlowingFluid> stillFluid;
    protected RegistryObject<FlowingFluid> flowingFluid;

    protected RegistryObject<FlowingFluidBlock> block;
    protected RegistryObject<Item> bucket;

    protected ForgeFlowingFluid.Properties properties;

    public static final FlowingFluid LAVA = register("lava", new LavaFluid.Source());

    private static <T extends Fluid> T register(String key, T p_215710_1_) {
        return (T)(Registry.register(Registry.FLUID, key, p_215710_1_));
    }

    protected RawAcidFluid(String key, String stillTexture, String flowTexture) {

        stillFluid = NetherEndingEnergy.FLUIDS.register(key, () -> new ForgeFlowingFluid.Source(properties));
        flowingFluid = NetherEndingEnergy.FLUIDS.register(flowing(key), () -> new ForgeFlowingFluid.Flowing(properties));

        block = NetherEndingEnergy.BLOCKS.register(key, () -> new RawAcidBlock(stillFluid, Block.Properties.create(Material.WATER)));
        bucket = NetherEndingEnergy.ITEMS.register(bucket(key), () -> new BucketItem(stillFluid, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ModSetup.itemGroup).rarity(Rarity.UNCOMMON)));

        properties = new ForgeFlowingFluid.Properties(stillFluid, flowingFluid, FluidAttributes.builder(new ResourceLocation(stillTexture), new ResourceLocation(flowTexture)).rarity(Rarity.UNCOMMON)).bucket(bucket).block(block).levelDecreasePerBlock(2);
    }

    public static RawAcidFluid create(String key) {

        return new RawAcidFluid(key, NetherEndingEnergy.MODID + ":block/fluids/raw_acid_still", NetherEndingEnergy.MODID + ":block/fluids/raw_acid_flow");
    }

    public static String flowing(String fluid) {

        return fluid + "_flowing";
    }

    public static String bucket(String fluid) {

        return fluid + "_bucket";
    }

    public static class RawAcidBlock extends FlowingFluidBlock {

        public RawAcidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {

            super(supplier, properties);
        }

        @Override
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
    }
}