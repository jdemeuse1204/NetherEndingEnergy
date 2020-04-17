package com.agrejus.netherendingenergy.blocks.abyssal.link;

import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.creative.energy.CreativeEnergyStoreTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.loading.FMLCommonLaunchHandler;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLServiceProvider;

import javax.annotation.Nullable;

public class AbyssalLinkBlock extends Block {
    public AbyssalLinkBlock() {
        super(Block.Properties.create(Material.IRON)
                .sound(SoundType.GROUND)
                .hardnessAndResistance(.5f)
                .lightValue(0));
        setRegistryName(RegistryNames.ABYSSAL_LINK);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new AbyssalLinkTile();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        //entityIn.changeDimension(worldIn.dimension.getType() == DimensionType.THE_END ? DimensionType.OVERWORLD : DimensionType.THE_END);
        if (worldIn.isRemote) {
            return false;
        }
//worldIn.getChunkAt(pos).set
        player.setPosition(0,0,0);
        player.changeDimension(DimensionType.THE_END);
/*        MinecraftServer server = worldIn.getServer();
        World transferWorld = DimensionManager.getWorld(server, DimensionType.THE_END, true, true);
        player.setWorld(transferWorld);
        player.setPositionAndUpdate(0,0,0);*/
/*        MinecraftServer s = Minecraft.getInstance();
        s.getCommandManager().executeCommand( s, "/tp " + p.getName() + " " + px + " " + py + " " + pz );*/
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
