package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.common.attributes.CustomFluidAttributes;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import com.agrejus.netherendingenergy.common.tank.MixableAcidFluidTank;
import com.agrejus.netherendingenergy.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

import java.util.ArrayList;

import static net.minecraftforge.common.property.Properties.StaticProperty;

public class TerraMixerBlock extends Block {

    public TerraMixerBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(.01f)
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_MIXER);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TerraMixerTile();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer != null) {
            worldIn.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, placer)), 2);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(BlockStateProperties.POWERED) ? super.getLightValue(state) : 0;
    }

/*    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }*/

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (worldIn.isRemote) {
            return true;
        }

        ArrayList<BucketItem> acceptableFluidBucketItems = new ArrayList<BucketItem>() {
            {
                add(ModItems.ACID_OF_THE_BLAZE_BUCKET);
                add(ModItems.ACID_OF_THE_CHORUS_BUCKET);
                add(ModItems.ACID_OF_THE_DEAD_BUCKET);
                add(ModItems.ACID_OF_THE_ELSEWHERE_BUCKET);
                add(ModItems.ACID_OF_THE_FORREST_BUCKET);
                add(ModItems.ACID_OF_THE_FORTUNATE_BUCKET);
                add(ModItems.ACID_OF_THE_LIVING_BUCKET);
                add(ModItems.ACID_OF_THE_MESSENGER_BUCKET);
                add(ModItems.ACID_OF_THE_MOLTEN_BUCKET);
                add(ModItems.ACID_OF_THE_NETHER_BUCKET);
                add(ModItems.ACID_OF_THE_ORDINARY_BUCKET);
                add(ModItems.ACID_OF_THE_TEARFUL_BUCKET);
                add(ModItems.ACID_OF_THE_TIRELESS_BUCKET);
                add(ModItems.ACID_OF_THE_UNSTABLE_BUCKET);
                add(ModItems.ACID_OF_THE_WINTER_BUCKET);
                add(ModItems.ACID_OF_THE_WISE_BUCKET);
                add(ModItems.ACID_OF_THE_WITHER_BUCKET);
            }
        };

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        ItemStack heldItemStack = player.getHeldItem(hand);
        Item heldItem = heldItemStack.getItem();
        if (acceptableFluidBucketItems.contains(heldItem)) {

            tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.DOWN).ifPresent(w -> {

                BucketItem item = (BucketItem) heldItemStack.getItem();

                // we lose our tags here
                FluidStack fluidToAdd = new FluidStack(item.getFluid(), 1000);

                if (w.fill(fluidToAdd, IFluidHandler.FluidAction.SIMULATE) == 1000) {
                    CompoundNBT tag = heldItemStack.getTag();
                    fluidToAdd.setTag(tag);

                    w.fill(fluidToAdd, IFluidHandler.FluidAction.EXECUTE);
                    tileEntity.markDirty();
                    heldItemStack.shrink(1);

                    ItemStack itemToReturnToHand = new ItemStack(Items.BUCKET);
                    if (heldItemStack.isEmpty()) {
                        player.setHeldItem(Hand.MAIN_HAND, itemToReturnToHand);
                    } else if (!player.inventory.addItemStackToInventory(itemToReturnToHand)) {
                        player.dropItem(itemToReturnToHand, false);
                    }
                }
            });
            return true;
        }

        if (heldItem == Items.BUCKET) {
            tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, Direction.EAST).ifPresent(w -> {

                // we lose our tags here
                MixableAcidFluidTank tank = (MixableAcidFluidTank) w;
                FluidStack simulatedDrain = tank.simulateDrainAmount(1000);

                BucketItem resultBucketItem = null;
                int size = acceptableFluidBucketItems.size();
                for (int i = 0; i < size; i++) {
                    BucketItem acceptableBucketItem = acceptableFluidBucketItems.get(i);
                    if (acceptableBucketItem.getFluid() ==simulatedDrain.getFluid()) {
                        resultBucketItem = acceptableBucketItem;
                        break;
                    }
                }

                if (simulatedDrain.getAmount() == 1000 && resultBucketItem != null) {
                    FluidStack drainedStack = tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    heldItemStack.shrink(1);

                    ItemStack bucketItemStack = this.getAcidItemStack(drainedStack, resultBucketItem);

                    if (heldItemStack.isEmpty()) {
                        player.setHeldItem(Hand.MAIN_HAND, bucketItemStack);
                    } else if (!player.inventory.addItemStackToInventory(bucketItemStack)) {
                        player.dropItem(bucketItemStack, false);
                    }
                }
            });
            return true;
        }

        if (tileEntity instanceof INamedContainerProvider) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
        } else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
        return true;
    }

    private ItemStack getAcidItemStack(FluidStack fluidStack, BucketItem bucket) {
        CompoundNBT tag = new CompoundNBT();
        ItemStack itemStack = new ItemStack(bucket);
        CustomFluidAttributes attributes = (CustomFluidAttributes) bucket.getFluid().getAttributes();

        CompoundNBT stackTag = fluidStack.getTag();
        int spatialAmount = stackTag.getInt("spatialAmount");

        FluidHelpers.serializeCustomFluidAttributes(tag, attributes, spatialAmount);
        itemStack.setTag(tag);
        return itemStack;
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity placer) {
        return Direction.getFacingFromVector((float) (placer.posX - clickedBlock.getX()), (float) (placer.posY - clickedBlock.getY()), (float) (placer.posZ - clickedBlock.getZ()));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED, StaticProperty);
    }
}
