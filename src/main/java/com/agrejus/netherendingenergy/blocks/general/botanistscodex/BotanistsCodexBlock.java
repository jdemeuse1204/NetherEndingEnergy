package com.agrejus.netherendingenergy.blocks.general.botanistscodex;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.common.blocks.DroppableInventoryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class BotanistsCodexBlock extends DroppableInventoryBlock {
    public BotanistsCodexBlock() {
        super(Block.Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(2.0f)
                .lightValue(0));
        setRegistryName(RegistryNames.BOTANISTS_CODEX);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BotanistsCodexTile();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (world.isRemote) {
            return super.onBlockActivated(state, world, pos, player, hand, hit);
        }

        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof INamedContainerProvider) {
            NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
        } else {
            throw new IllegalStateException("Our named container provider is missing!");
        }
        return true;
    }
}
