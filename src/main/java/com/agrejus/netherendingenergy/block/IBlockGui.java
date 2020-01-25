package com.agrejus.netherendingenergy.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.Stat;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public interface IBlockGui {

    public default void writeExtraGuiData(PacketBuffer packetBuffer, World world, PlayerEntity player, BlockPos blockPos, Hand hand, BlockRayTraceResult rayTraceResult) {

    }

    /**
     * @return An optional gui opening statistic.
     */
    @Nullable
    public default Stat<ResourceLocation> getOpenStat() {
        return null;
    }

    public static boolean onBlockActivatedHook(IBlockGui block, IBlockContainerProvider blockContainerProvider, BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        // Drop through if the player is sneaking
        if (player.isSneaking()) {
            return false;
        }

        if (!world.isRemote()) {
            INamedContainerProvider containerProvider = blockContainerProvider.get(blockState, world, blockPos);
            if (containerProvider != null) {
                NetworkHooks.openGui((ServerPlayerEntity) player, containerProvider,
                        packetBuffer -> block.writeExtraGuiData(packetBuffer, world, player, blockPos, hand, rayTraceResult));
                Stat<ResourceLocation> openStat = block.getOpenStat();
                if (openStat != null) {
                    player.addStat(openStat);
                }
            }
        }

        return true;
    }

    public static interface IBlockContainerProvider {
        @Nullable
        public INamedContainerProvider get(BlockState blockState, World world, BlockPos blockPos);
    }

}
