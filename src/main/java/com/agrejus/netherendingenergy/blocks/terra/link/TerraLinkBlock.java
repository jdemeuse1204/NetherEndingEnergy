package com.agrejus.netherendingenergy.blocks.terra.link;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class TerraLinkBlock extends Block {

    protected String channelName = "Test";

    public TerraLinkBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.WOOD)
                .hardnessAndResistance(.01f)
                .lightValue(0));
        setRegistryName(RegistryNames.TERRA_LINK);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TerraLinkTile();
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (world.isRemote) {
            return super.onBlockActivated(state, world, pos, player, hand, hit);
        }

        ItemStack mainHandItemStack = player.getHeldItemMainhand();
        if (hand == Hand.MAIN_HAND && mainHandItemStack.isEmpty() == true && player.isSneaking() && player instanceof ServerPlayerEntity && world instanceof ServerWorld) {

            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity == null || (tileEntity instanceof TerraLinkTile) == false) {
                return super.onBlockActivated(state, world, pos, player, hand, hit);
            }

            TerraLinkTile link = (TerraLinkTile) tileEntity;

            BlockPos[] links = link.getLinks();
            if (links.length == 0) {
                return super.onBlockActivated(state, world, pos, player, hand, hit);
            }
            BlockPos linkPos = links[0];

            // Drain RF to teleport
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            serverPlayer.teleport((ServerWorld)world, linkPos.getX() + .5, linkPos.getY() + 1.5, linkPos.getZ() + .5, hit.getFace().getOpposite().getHorizontalAngle(), player.rotationPitch);
            return true;
        }

        if(mainHandItemStack.getItem() != ModItems.LINKING_REMOTE) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof INamedContainerProvider) {
                NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }

            return true;
        }

        return false;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (world.isRemote) return;
        world.getCapability(NetherEndingEnergy.CAPABILITY, null).ifPresent(cap -> {
            cap.add(pos);
        });
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (world.isRemote) return;
        world.getCapability(NetherEndingEnergy.CAPABILITY, null).ifPresent(cap -> {
            cap.remove(pos);
        });
    }
}
