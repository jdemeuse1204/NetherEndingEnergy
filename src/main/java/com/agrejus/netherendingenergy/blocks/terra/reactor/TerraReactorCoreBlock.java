package com.agrejus.netherendingenergy.blocks.terra.reactor;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.TerraReactorMultiblock;
import com.agrejus.netherendingenergy.blocks.terra.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.multiblock.MultiBlockTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TerraReactorCoreBlock extends Block {

    public static final EnumProperty<TerraReactorPartIndex> FORMED = EnumProperty.<TerraReactorPartIndex>create("formed", TerraReactorPartIndex.class);

    public TerraReactorCoreBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(.01f)
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_REACTOR_CORE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TerraReactorCoreTile();
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

/*    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        if (!worldIn.isRemote) {
            TileEntity tileEntity = worldIn.getTileEntity(pos);

            if (tileEntity instanceof INamedContainerProvider) {
                NetworkHooks.openGui((ServerPlayerEntity) player,(INamedContainerProvider) tileEntity,  tileEntity.getPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
            return true;
        }

        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }*/

    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity placer) {
        return Direction.getFacingFromVector((float) (placer.posX - clickedBlock.getX()), (float) (placer.posY - clickedBlock.getY()), (float) (placer.posZ - clickedBlock.getZ()));
    }

    @Override
    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
        return false;
    }

    public boolean isFullCube(BlockState state) { return false; }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (player.getHeldItem(hand).getItem() == Items.STICK) {
            toggleMultiBlock(world, pos, state, player);
            return true;
        }
        // Only work if the block is formed
        if (state.getBlock() == ModBlocks.TERRA_REACTOR_CORE_BLOCK && state.get(FORMED) != TerraReactorPartIndex.UNFORMED) {
            return super.onBlockActivated(state, world, pos, player, hand, hit);
        } else {
            return false;
        }
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
        if (!world.isRemote) {
            MultiBlockTools.breakMultiblock(TerraReactorMultiblock.INSTANCE, world, pos);
        }
        super.harvestBlock(world, player, pos, state, te, stack);
    }


    public static void toggleMultiBlock(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // Form or break the multiblock
        if (!world.isRemote) {
            TerraReactorPartIndex formed = state.get(FORMED);
            if (formed == TerraReactorPartIndex.UNFORMED) {
                if (MultiBlockTools.formMultiblock(TerraReactorMultiblock.INSTANCE, world, pos)) {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Made a superchest!"), false);
                } else {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Could not form superchest!"), false);
                }
            } else {
                if (!MultiBlockTools.breakMultiblock(TerraReactorMultiblock.INSTANCE, world, pos)) {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Not a valid superchest!"), false);
                }
            }
        }
    }

    public static boolean isFormedSuperchestController(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.getBlock() == ModBlocks.TERRA_REACTOR_CORE_BLOCK && state.get(FORMED) != TerraReactorPartIndex.UNFORMED;
    }

    @Nullable
    public static BlockPos getControllerPos(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == ModBlocks.TERRA_REACTOR_CORE_BLOCK && state.get(TerraReactorCoreBlock.FORMED) != TerraReactorPartIndex.UNFORMED) {
            return pos;
        }
        if (state.getBlock() == ModBlocks.TERRA_MACHINE_CASING && state.get(TerraReactorCoreBlock.FORMED) != TerraReactorPartIndex.UNFORMED) {
            TerraReactorPartIndex index = state.get(TerraReactorCoreBlock.FORMED);
            // This index indicates where in the superblock this part is located. From this we can find the location of the bottom-left coordinate
            BlockPos bottomLeft = pos.add(-index.getDx(), -index.getDy(), -index.getDz());
            for (TerraReactorPartIndex idx : TerraReactorPartIndex.VALUES) {
                if (idx != TerraReactorPartIndex.UNFORMED) {
                    BlockPos p = bottomLeft.add(idx.getDx(), idx.getDy(), idx.getDz());
                    if (isFormedSuperchestController(world, p)) {
                        return p;
                    }
                }
            }

        }
        return null;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FORMED);
    }
}