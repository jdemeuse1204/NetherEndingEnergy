package com.agrejus.netherendingenergy.fluids;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.setup.ModSetup;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
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

import java.util.function.Supplier;

public abstract class AcidFluid {
    protected RegistryObject<FlowingFluid> stillFluid;
    protected RegistryObject<FlowingFluid> flowingFluid;
    protected RegistryObject<FlowingFluidBlock> block;
    protected RegistryObject<Item> bucket;
    protected ForgeFlowingFluid.Properties properties;

    private ResourceLocation stillTextureResourceLocation;
    private ResourceLocation flowingTextureResourceLocation;

    public ResourceLocation getStillTexture() {
        return stillTextureResourceLocation;
    }

    public ResourceLocation getFlowingTexture() {
        return flowingTextureResourceLocation;
    }

    protected abstract FlowingFluidBlock createBlock(RegistryObject<FlowingFluid> stillFluid);

    protected BucketItem createBucketItem(RegistryObject<FlowingFluid> stillFlowingFluid) {
        return new BucketItem(stillFlowingFluid, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ModSetup.itemGroup).rarity(Rarity.UNCOMMON));
    }

    protected ForgeFlowingFluid.Properties createProperties(RegistryObject<FlowingFluid> stillFlowingFluid, RegistryObject<FlowingFluid> flowingFlowingFluid) {
        return new ForgeFlowingFluid.Properties(stillFlowingFluid, flowingFlowingFluid, FluidAttributes.builder(getStillTexture(), getFlowingTexture()).rarity(Rarity.UNCOMMON)).bucket(bucket).block(block).levelDecreasePerBlock(2);
    }

    protected AcidFluid(String key, String stillTexture, String flowingTexture) {
        stillTextureResourceLocation = new ResourceLocation(stillTexture);
        flowingTextureResourceLocation = new ResourceLocation(flowingTexture);
        stillFluid = NetherEndingEnergy.FLUIDS.register(key, () -> new ForgeFlowingFluid.Source(properties));
        flowingFluid = NetherEndingEnergy.FLUIDS.register(flowing(key), () -> new ForgeFlowingFluid.Flowing(properties));

        block = NetherEndingEnergy.BLOCKS.register(key, () -> createBlock(stillFluid));
        bucket = NetherEndingEnergy.ITEMS.register(bucket(key), () -> createBucketItem(stillFluid));

        properties = createProperties(stillFluid, flowingFluid);
    }

    protected AcidFluid(String acidName) {
        this(String.format("acid_of_the_%s_fluid", acidName), String.format("%s:block/fluids/acid_of_the_%s_still", NetherEndingEnergy.MODID, acidName), String.format("%s:block/fluids/acid_of_the_%s_flow", NetherEndingEnergy.MODID, acidName));
    }

    public String flowing(String fluid) {

        return fluid + "_flowing";
    }

    public String bucket(String fluid) {

        return fluid + "_bucket";
    }

    public static abstract class AcidBlock extends FlowingFluidBlock {

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