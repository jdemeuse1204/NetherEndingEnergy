package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.common.attributes.CustomFluidAttributes;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import com.agrejus.netherendingenergy.common.tank.MixableAcidFluidTank;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TerraCollectingStationBlock extends Block {

    public TerraCollectingStationBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(.5f)
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_COLLECTING_STATION);
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
        return new TerraCollectingStationTile();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (placer != null) {
            worldIn.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, placer)), 2);
        }
    }

    private ItemStack getAcidOfTheOrdinaryItemStack(FluidStack fluidStack) {
        CompoundNBT tag = new CompoundNBT();
        BucketItem bucket = ModItems.ACID_OF_THE_ORDINARY_BUCKET;
        ItemStack itemStack = new ItemStack(bucket);
        CustomFluidAttributes attributes = (CustomFluidAttributes)bucket.getFluid().getAttributes();

        CompoundNBT stackTag = fluidStack.getTag();
        int spatialAmount = stackTag.getInt("spatialAmount");

        FluidHelpers.serializeCustomFluidAttributes(tag, attributes, spatialAmount);
        itemStack.setTag(tag);
        return itemStack;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (worldIn.isRemote) {
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        ItemStack heldItemStack = player.getHeldItem(hand);
        if (heldItemStack.getItem() == Items.BUCKET) {
            tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(w -> {

                // we lose our tags here
                MixableAcidFluidTank tank = (MixableAcidFluidTank) w;
                if (tank.simulateDrainAmount(1000).getAmount() == 1000) {
                    FluidStack drainedStack = tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    heldItemStack.shrink(1);

                    ItemStack bucketItemStack = this.getAcidOfTheOrdinaryItemStack(drainedStack);

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

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity placer) {
        return Direction.getFacingFromVector((float) (placer.posX - clickedBlock.getX()), (float) (placer.posY - clickedBlock.getY()), (float) (placer.posZ - clickedBlock.getZ()));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED);
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }
}