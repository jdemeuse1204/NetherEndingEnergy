package com.agrejus.netherendingenergy.blocks.terra.reactor.ports.liquid;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.base.reactor.DirectionalReactorPartBlock;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreTile;
import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorReactorMultiBlock;
import com.agrejus.netherendingenergy.common.fluids.CustomFluidAttributes;
import com.agrejus.netherendingenergy.common.fluids.FluidHelpers;
import com.agrejus.netherendingenergy.common.reactor.attributes.AcidAttributes;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TerraReactorAcidPortBlock extends DirectionalReactorPartBlock {
    public TerraReactorAcidPortBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0f)
                .lightValue(0), RegistryNames.TERRA_REACTOR_ACID_PORT, TerraReactorReactorMultiBlock.INSTANCE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TerraReactorAcidPortTile();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (worldIn.isRemote) {
            return true;
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);
        ItemStack itemstack = player.getHeldItem(hand);
        Item item = itemstack.getItem();

        if (item instanceof BucketItem) {
            BucketItem bucketItem = (BucketItem) item;
            Fluid fluid = bucketItem.getFluid();

            if (TerraReactorCoreTile.getAllowedFluids().contains(fluid)) {
                tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getFace()).ifPresent(w -> {

                    FluidStack stackToFill = this.getFillStack(fluid, itemstack);

                    if (w.fill(stackToFill, IFluidHandler.FluidAction.SIMULATE) == FluidAttributes.BUCKET_VOLUME) {
                        w.fill(stackToFill, IFluidHandler.FluidAction.EXECUTE);
                        player.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BUCKET));
                    }
                });
                return true;
            }

            // Don't flow through and open gui if fluid not allowed
            return true;
        }

        if (tileEntity instanceof INamedContainerProvider) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
        } else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
        return true;
    }

    private FluidStack getFillStack(Fluid fluid, ItemStack itemStack) {
        AcidAttributes attributes = this.getAcidAttributes(itemStack);
        FluidStack stackToFill = new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME);
        CompoundNBT tag = new CompoundNBT();

        // in case we put a bucket in from the creative menu
        if (attributes == null) {
            CustomFluidAttributes fluidAttributes = (CustomFluidAttributes) stackToFill.getFluid().getAttributes();
            attributes = new AcidAttributes(fluidAttributes.getStrength(),
                    fluidAttributes.getEfficiency(),
                    fluidAttributes.getStability(),
                    fluidAttributes.getResponse(),
                    fluidAttributes.getUses(),
                    fluidAttributes.getSpatial(),
                    fluidAttributes.getSpatialAmount());
        }

        FluidHelpers.serializeCustomFluidAttributes(tag, attributes);
        stackToFill.setTag(tag);
        return stackToFill;
    }

    private AcidAttributes getAcidAttributes(ItemStack stack) {
        CompoundNBT tag = stack.getTag();

        if (tag == null) {
            return null; // Should not be null,if so grab default attributes
        }

        return FluidHelpers.deserializeAttributes(tag);
    }
}
