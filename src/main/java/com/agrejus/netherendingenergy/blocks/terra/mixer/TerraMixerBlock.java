package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.common.blocks.PartialModelFillBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TerraMixerBlock extends Block {

    private int y;

    public TerraMixerBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(.01f)
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_MIXER);

        this.y = 0;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TerraMixerTile(this);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        this.y = 0;
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        this.y = pos.getY();
        if (placer != null) {
            worldIn.setBlockState(pos, state.with(BlockStateProperties.FACING, getFacingFromEntity(pos, placer)), 2);
        }

        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public int getLightValue(BlockState state) {
        return state.get(BlockStateProperties.POWERED) ? super.getLightValue(state) : 0;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        if (!worldIn.isRemote) {
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
}
