package com.agrejus.netherendingenergy.blocks.general.wireless;

import com.agrejus.netherendingenergy.common.interfaces.ILinkableTile;
import com.agrejus.netherendingenergy.network.NetherEndingEnergyNetworking;
import com.agrejus.netherendingenergy.network.PacketShowLocationParticles;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
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
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class ModuleBlock extends Block {

    protected static final VoxelShape UP_AABB = Block.makeCuboidShape(6, 14, 12, 10, 16, 6);
    protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(6, 0, 4, 10, 2, 10);

    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0, 6, 6, 2, 12, 10);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(14, 6, 6, 16, 12, 10);
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(6, 6, 0, 10, 12, 2);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(6, 6, 14, 10, 12, 16);

    public ModuleBlock(String registryName) {
        super(Properties.create(Material.IRON)
                .sound(SoundType.GROUND)
                .hardnessAndResistance(1.0f)
                .lightValue(0)
                .doesNotBlockMovement());
        setRegistryName(registryName);
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

    public abstract TileEntity createTileEntity(BlockState state, IBlockReader world);

    @Override
    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {

        // remove linking data
        if (te != null && te instanceof ModuleTileBase) {
            ILinkableTile module = (ILinkableTile) te;
            module.clearLinks();
            module.updateTile();
            module.markDirty();
        }

        super.harvestBlock(worldIn, player, pos, state, te, stack);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        if (worldIn.isRemote) {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }

        if (handIn == Hand.OFF_HAND) {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }

        if (player.getHeldItemMainhand().isEmpty() == false || player.getHeldItemOffhand().isEmpty() == false) {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity == null || (tileEntity instanceof ModuleTileBase) == false) {
            return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
        }

        ModuleTileBase module = (ModuleTileBase) tileEntity;

        BlockPos linkedPosition = module.getLinkedBlockPosition();

        if (linkedPosition == null) {
            player.sendStatusMessage(new StringTextComponent("Module has no link"), false);
            return false;
        }

        TileEntity linkedTileEntity = worldIn.getTileEntity(linkedPosition);

        if (linkedTileEntity == null || (linkedTileEntity instanceof ModuleTileBase) == false) {
            player.sendStatusMessage(new StringTextComponent("Module has no link"), false);
            return false;
        }

        ModuleTileBase linkedModule = (ModuleTileBase) linkedTileEntity;

        if (module.isConnectedTo(linkedModule) == false) {
            player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Module contains broken link, please clear and relink"), false);
            return false;
        }

        BlockPos linkedBlockPosition = linkedModule.getDestination();
        Block linkedBlock = worldIn.getBlockState(linkedBlockPosition).getBlock();

        String blockName = linkedBlock.getNameTextComponent().getFormattedText();

        NetherEndingEnergyNetworking.sendToPlayer(new PacketShowLocationParticles(linkedModule.getPos()), (ServerPlayerEntity) player);
        player.sendStatusMessage(new StringTextComponent(String.format("Linked to %s @ x:%s, y:%s, z:%s", blockName, linkedBlockPosition.getX(), linkedBlockPosition.getY(), linkedBlockPosition.getZ())), false);
        return true;
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

        BlockState moduleBlockState = worldIn.getBlockState(pos);

        if (moduleBlockState == null || moduleBlockState.has(BlockStateProperties.FACING) == false) {
            super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
            return;
        }

        Direction facing = moduleBlockState.get(BlockStateProperties.FACING);
        BlockPos attachedBlockPosition = pos.offset(facing);
        Block attachedBlock = worldIn.getBlockState(attachedBlockPosition).getBlock();

        if (attachedBlock == Blocks.AIR) {
            worldIn.destroyBlock(pos, true);
        }
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.FACING, BlockStateProperties.POWERED);
    }
}
