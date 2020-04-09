package com.agrejus.netherendingenergy.blocks.general.wireless.fluid;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.NetherEndingEnergyBlockStateProperties;
import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.items.ModItems;
import com.agrejus.netherendingenergy.items.wireless.LinkingRemoteItem;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class WirelessFluidTransferModuleBlock extends Block {

    protected static final VoxelShape UP_AABB = Block.makeCuboidShape(6, 14, 12, 10, 16, 6);
    protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(6, 0, 4, 10, 2, 10);

    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0, 6, 6, 2, 12, 10);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(14, 6, 6, 16, 12, 10);
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(6, 6, 0, 10, 12, 2);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(6, 6, 14, 10, 12, 16);

    public WirelessFluidTransferModuleBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.GROUND)
                .hardnessAndResistance(1.0f)
                .lightValue(0)
                .doesNotBlockMovement());
        setRegistryName(RegistryNames.WIRELESS_FLUID_TRANSFER_MODULE);
        setDefaultState(getStateContainer().getBaseState().with(BlockStateProperties.FACING, Direction.NORTH).with(BlockStateProperties.POWERED, Boolean.valueOf(false)));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape voxelshape = VoxelShapes.empty();
        Direction facing = state.get(BlockStateProperties.FACING);

        switch (facing) {
            case UP:
                voxelshape = VoxelShapes.or(voxelshape, UP_AABB);
                break;
            case DOWN:
                voxelshape = VoxelShapes.or(voxelshape, DOWN_AABB);
                break;
            default:
            case NORTH:
                voxelshape = VoxelShapes.or(voxelshape, NORTH_AABB);
                break;
            case SOUTH:
                voxelshape = VoxelShapes.or(voxelshape, SOUTH_AABB);
                break;
            case EAST:
                voxelshape = VoxelShapes.or(voxelshape, EAST_AABB);
                break;
            case WEST:
                voxelshape = VoxelShapes.or(voxelshape, WEST_AABB);
                break;
        }

        return voxelshape;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WirelessFluidTransferModuleTile();
    }

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        if (worldIn.isRemote) {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }

        ItemStack itemStack = player.getHeldItem(Hand.MAIN_HAND);
        Item item = itemStack.getItem();

        if (item == ModItems.LINKING_REMOTE && handIn == Hand.MAIN_HAND) {
            LinkingRemoteItem remoteItem = (LinkingRemoteItem) item;

            // Don't link to self
            if (remoteItem.getOrigin() != null && pos.equals(remoteItem.getOrigin())) {
                return false;
            }

            remoteItem.setPoint(pos, player);

            if (remoteItem.getDestination() != null) {

                BlockPos origin = remoteItem.getOrigin();
                BlockPos destination = remoteItem.getDestination();

                TileEntity originTile = worldIn.getTileEntity(origin);
                TileEntity destinationTile = worldIn.getTileEntity(destination);

                if (originTile instanceof WirelessFluidTransferModuleTile && destinationTile instanceof WirelessFluidTransferModuleTile) {
                    WirelessFluidTransferModuleTile originTransferModule = (WirelessFluidTransferModuleTile) originTile;
                    WirelessFluidTransferModuleTile destinationTransferModule = (WirelessFluidTransferModuleTile) destinationTile;

                    originTransferModule.setLinkedBlockPosition(destination);
                    destinationTransferModule.setLinkedBlockPosition(origin);
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Link Complete"), false);

                }
            }
            return false;
        }

        return true;
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {

        // remove linking data
        if (te != null && te instanceof WirelessFluidTransferModuleTile) {
            WirelessFluidTransferModuleTile wirelessFluidTransferModuleTile = (WirelessFluidTransferModuleTile) te;
            wirelessFluidTransferModuleTile.setLinkedBlockPosition(null);
            wirelessFluidTransferModuleTile.updateBlock();
            wirelessFluidTransferModuleTile.markDirty();
        }

        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {

        if (placer != null) {
            Direction facing = getFacingFromEntity(pos, placer);
            if (placer.isSneaking()) {
                world.setBlockState(pos, state.with(BlockStateProperties.FACING, facing.getOpposite()), 2);
            } else {
                world.setBlockState(pos, state.with(BlockStateProperties.FACING, facing), 2);
            }
        }
    }

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity placer) {
        return Direction.getFacingFromVector((float) (placer.posX - clickedBlock.getX()), (float) (placer.posY - clickedBlock.getY()), (float) (placer.posZ - clickedBlock.getZ()));
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {

        BlockState newBlockState = worldIn.getBlockState(fromPos);
        if (newBlockState.getBlock() == Blocks.AIR && blockIn != Blocks.AIR) {
            worldIn.destroyBlock(pos, true);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED);
    }
}
