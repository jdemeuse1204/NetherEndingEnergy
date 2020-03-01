package com.agrejus.netherendingenergy.fluids;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.setup.ModSetup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AcidFluid {
    protected static RegistryObject<FlowingFluid> stillFluid;
    protected static RegistryObject<FlowingFluid> flowingFluid;

    protected static RegistryObject<FlowingFluidBlock> block;
    protected static RegistryObject<Item> bucket;

    protected static ForgeFlowingFluid.Properties properties;

    private static ResourceLocation stillTextureResourceLocation;
    private static ResourceLocation flowingTextureResourceLocation;

    public static FlowingFluid getStill() {
        return stillFluid.get();
    }

    public static FlowingFluid getFlowing() {
        return flowingFluid.get();
    }

    public static Item getItem() {
        return bucket.get();
    }

    public static ResourceLocation getStillTexture() {
        return stillTextureResourceLocation;
    }

    public static ResourceLocation getFlowingTexture() {
        return flowingTextureResourceLocation;
    }

    protected FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid) {
        return null;
    }
// S
    protected AcidFluid(String acidName) {
        String key = String.format("acid_of_the_%s_fluid", acidName);
        String stillTexture = String.format("%s:block/fluids/acid_of_the_%s_still", NetherEndingEnergy.MODID, acidName);
        String flowTexture = String.format("%s:block/fluids/acid_of_the_%s_flow", NetherEndingEnergy.MODID, acidName);
        stillTextureResourceLocation = new ResourceLocation(stillTexture);
        flowingTextureResourceLocation = new ResourceLocation(flowTexture);
        stillFluid = NetherEndingEnergy.FLUIDS.register(key, () -> new ForgeFlowingFluid.Source(properties));
        flowingFluid = NetherEndingEnergy.FLUIDS.register(flowing(key), () -> new ForgeFlowingFluid.Flowing(properties));

        FlowingFluidBlock b = createBlock(stillFluid);
        block = NetherEndingEnergy.BLOCKS.register(key, () -> b);
        bucket = NetherEndingEnergy.ITEMS.register(bucket(key), () -> new BucketItem(stillFluid, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ModSetup.itemGroup).rarity(Rarity.UNCOMMON)));

        properties = new ForgeFlowingFluid.Properties(stillFluid, flowingFluid, FluidAttributes.builder(stillTextureResourceLocation, flowingTextureResourceLocation).rarity(Rarity.UNCOMMON)).bucket(bucket).block(block).levelDecreasePerBlock(2);
    }

    public static String flowing(String fluid) {

        return fluid + "_flowing";
    }

    public static String bucket(String fluid) {

        return fluid + "_bucket";
    }

    public static class AcidBlock extends FlowingFluidBlock {

        public AcidBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
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