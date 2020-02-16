package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.common.blocks.PartialModelFillBlock;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
import com.agrejus.netherendingenergy.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.EmptyHandler;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TerraCollectingStationBlock extends Block {

    public TerraCollectingStationBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_COLLECTING_STATION);
    }

    private static final Pattern COMPILE = Pattern.compile("@", Pattern.LITERAL);

    // So the tank glass is translucent
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

    @Override
    public int getLightValue(BlockState state) {
        return state.get(BlockStateProperties.POWERED) ? super.getLightValue(state) : 0;
    }

    private void transferOutOfContainer(PlayerEntity player, Hand hand, NEEFluidTank tankHandler) {

    }

    private void transferIntoContainer(PlayerEntity player, Hand hand, NEEFluidTank tankHandler) {
        if (player == null || tankHandler == null) {
            return;
        }
        ItemStack heldItem = player.getHeldItem(hand);
        if (heldItem.isEmpty() == false) {
            IItemHandler playerInventory = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP).orElse(EmptyHandler.INSTANCE);
            if (playerInventory != null) {
                FluidUtil.getFluidHandler(ItemHandlerHelper.copyStackWithSize(heldItem, 1)).ifPresent(w-> {
                    int drainAmount = tankHandler.resolveDrainAmount(1000);

                    if (drainAmount == 1000) {
                        FluidActionResult result = FluidUtil.tryEmptyContainerAndStow(heldItem, tankHandler, playerInventory, Integer.MAX_VALUE, player, true);

                        if (result.isSuccess()) {
                            player.setHeldItem(hand, result.getResult());
                        }
                    }
                });
            }
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        if (!worldIn.isRemote) {

            if (player.getHeldItem(handIn).getItem() == Items.BUCKET) {
                // Give player 1 bucket of refined acid
                TerraCollectingStationTile collectingStation = (TerraCollectingStationTile)worldIn.getTileEntity(pos);
                transferIntoContainer(player, handIn, collectingStation.getInputTank());
                return true;
            }

            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if (tileEntity instanceof INamedContainerProvider) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
            return true;
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity placer) {
        return Direction.getFacingFromVector((float) (placer.posX - clickedBlock.getX()), (float) (placer.posY - clickedBlock.getY()), (float) (placer.posZ - clickedBlock.getZ()));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound != null) {
            CompoundNBT nbt = tagCompound.getCompound("output_tank");
            FluidStack fluidStack = null;
            if (!nbt.contains("Empty")) {
                fluidStack = FluidStack.loadFluidStackFromNBT(nbt);
            }
            if (fluidStack == null) {
                addInformationLocalized(tooltip, "message.netherendingenergy.tank", "empty");
            } else {
                String name = fluidStack.getTranslationKey();
                addInformationLocalized(tooltip, "message.netherendingenergy.tank", name + " (" + fluidStack.getAmount() + ")");
            }
        }
    }

    protected void addInformationLocalized(List<ITextComponent> tooltip, String key, Object... parameters) {
        String translated = I18n.format(key, parameters);
        translated = COMPILE.matcher(translated).replaceAll("\u00a7");

        Collections.addAll(tooltip.stream().map(ITextComponent::getFormattedText).collect(Collectors.toList()), StringUtils.split(translated, "\n"));
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return false;
    }

    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }
}