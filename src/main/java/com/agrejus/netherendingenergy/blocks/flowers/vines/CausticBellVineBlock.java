package com.agrejus.netherendingenergy.blocks.flowers.vines;

import com.agrejus.netherendingenergy.RegistryNames;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CausticBellVineBlock extends Block implements net.minecraftforge.common.IShearable {

    public static final BooleanProperty DOWN = SixWayBlock.DOWN;
    public static final BooleanProperty UP = SixWayBlock.UP;
    public static final BooleanProperty NORTH = SixWayBlock.NORTH;
    public static final BooleanProperty EAST = SixWayBlock.EAST;
    public static final BooleanProperty SOUTH = SixWayBlock.SOUTH;
    public static final BooleanProperty WEST = SixWayBlock.WEST;
    public static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = SixWayBlock.FACING_TO_PROPERTY_MAP.entrySet().stream().collect(Util.toMapCollector());
    protected static final VoxelShape UP_AABB = Block.makeCuboidShape(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_AABB = Block.makeCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape NORTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    protected static final VoxelShape SOUTH_AABB = Block.makeCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape DOWN_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    public CausticBellVineBlock() {
        super(Block.Properties.create(Material.TALL_PLANTS)
                .doesNotBlockMovement()
                .hardnessAndResistance(0.2F)
                .sound(SoundType.PLANT));
        setRegistryName(RegistryNames.CAUSTIC_VINES);
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, Boolean.valueOf(false)).with(NORTH, Boolean.valueOf(false)).with(EAST, Boolean.valueOf(false)).with(SOUTH, Boolean.valueOf(false)).with(WEST, Boolean.valueOf(false)).with(DOWN, Boolean.valueOf(false)));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        VoxelShape voxelshape = VoxelShapes.empty();
        if (state.get(UP)) {
            voxelshape = VoxelShapes.or(voxelshape, UP_AABB);
        }

        if (state.get(NORTH)) {
            voxelshape = VoxelShapes.or(voxelshape, NORTH_AABB);
        }

        if (state.get(EAST)) {
            voxelshape = VoxelShapes.or(voxelshape, EAST_AABB);
        }

        if (state.get(SOUTH)) {
            voxelshape = VoxelShapes.or(voxelshape, SOUTH_AABB);
        }

        if (state.get(WEST)) {
            voxelshape = VoxelShapes.or(voxelshape, WEST_AABB);
        }

        if (state.get(DOWN)) {
            voxelshape = VoxelShapes.or(voxelshape, DOWN_AABB);
        }

        return voxelshape;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

    @Nonnull
    @Override
    public List<ItemStack> onSheared(@Nonnull ItemStack item, IWorld world, BlockPos pos, int fortune) {

        return new ArrayList<ItemStack>() {
            {
                add(item);
            }
        };
    }

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return this.func_196543_i(this.func_196545_h(state, worldIn, pos));
    }

    private boolean func_196543_i(BlockState p_196543_1_) {
        return this.func_208496_w(p_196543_1_) > 0;
    }

    private int func_208496_w(BlockState p_208496_1_) {
        int i = 0;

        for (BooleanProperty booleanproperty : FACING_TO_PROPERTY_MAP.values()) {
            if (p_208496_1_.get(booleanproperty)) {
                ++i;
            }
        }

        return i;
    }

    private boolean func_196541_a(IBlockReader p_196541_1_, BlockPos p_196541_2_, Direction p_196541_3_) {
        BlockPos blockpos = p_196541_2_.offset(p_196541_3_);
        if (canAttachTo(p_196541_1_, blockpos, p_196541_3_)) {
            return true;
        } else if (p_196541_3_.getAxis() == Direction.Axis.Y) {
            return false;
        } else {
            BooleanProperty booleanproperty = FACING_TO_PROPERTY_MAP.get(p_196541_3_);
            BlockState blockstate = p_196541_1_.getBlockState(p_196541_2_.up());
            return blockstate.getBlock() == this && blockstate.get(booleanproperty);
        }
    }

    public static boolean canAttachTo(IBlockReader p_196542_0_, BlockPos worldIn, Direction neighborPos) {
        BlockState blockstate = p_196542_0_.getBlockState(worldIn);
        return Block.doesSideFillSquare(blockstate.getCollisionShape(p_196542_0_, worldIn), neighborPos.getOpposite());
    }

    private BlockState func_196545_h(BlockState p_196545_1_, IBlockReader p_196545_2_, BlockPos p_196545_3_) {
        BlockPos blockpos = p_196545_3_.up();
        BlockState blockstate = null;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BooleanProperty booleanproperty = getPropertyFor(direction);
            if (p_196545_1_.get(booleanproperty)) {
                boolean flag = this.func_196541_a(p_196545_2_, p_196545_3_, direction);
                if (!flag) {
                    if (blockstate == null) {
                        blockstate = p_196545_2_.getBlockState(blockpos);
                    }

                    flag = blockstate.getBlock() == this && blockstate.get(booleanproperty);
                }

                p_196545_1_ = p_196545_1_.with(booleanproperty, Boolean.valueOf(flag));
            }
        }

        return p_196545_1_;
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        BlockState blockstate = this.func_196545_h(stateIn, worldIn, currentPos);
        return !this.func_196543_i(blockstate) ? Blocks.AIR.getDefaultState() : blockstate;
    }

    public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
        BlockState blockstate = useContext.getWorld().getBlockState(useContext.getPos());
        if (blockstate.getBlock() == this) {
            return this.func_208496_w(blockstate) < FACING_TO_PROPERTY_MAP.size();
        } else {
            return super.isReplaceable(state, useContext);
        }
    }

    @Nullable
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = context.getWorld().getBlockState(context.getPos());
        boolean flag = blockstate.getBlock() == this;
        BlockState blockstate1 = flag ? blockstate : this.getDefaultState();

        for (Direction direction : context.getNearestLookingDirections()) {
            BooleanProperty booleanproperty = getPropertyFor(direction);
            boolean flag1 = flag && blockstate.get(booleanproperty);
            if (!flag1 && this.func_196541_a(context.getWorld(), context.getPos(), direction)) {
                return blockstate1.with(booleanproperty, Boolean.valueOf(true));
            }
        }

        return flag ? blockstate1 : null;
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(UP, NORTH, EAST, SOUTH, WEST, DOWN);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        switch (rot) {
            case CLOCKWISE_180:
                return state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
            case COUNTERCLOCKWISE_90:
                return state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
            case CLOCKWISE_90:
                return state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
            default:
                return state;
        }
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        switch (mirrorIn) {
            case LEFT_RIGHT:
                return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
            case FRONT_BACK:
                return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
            default:
                return super.mirror(state, mirrorIn);
        }
    }

    public static BooleanProperty getPropertyFor(Direction side) {
        return FACING_TO_PROPERTY_MAP.get(side);
    }
}
