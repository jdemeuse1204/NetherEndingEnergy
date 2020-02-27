package com.agrejus.netherendingenergy.blocks.base.reactor;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.common.interfaces.IMultiBlockType;
import com.agrejus.netherendingenergy.common.multiblock.MultiBlockTools;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreBlock.FORMED;

public abstract class ReactorPartBlock extends Block {

    protected final IMultiBlockType Type;

    public ReactorPartBlock(Block.Properties properties, String registryName, IMultiBlockType type) {
        super(properties);
        setRegistryName(registryName);
        setDefaultState(this.getDefaultReactorState());
        this.Type = type;
    }

    @Override
    public void harvestBlock(World world, PlayerEntity player, BlockPos pos, BlockState state, TileEntity te, ItemStack stack) {
        if (!world.isRemote) {

            // Reactor not formed, break the block
            if (state.get(FORMED) == TerraReactorPartIndex.UNFORMED) {
                super.onBlockHarvested(world, pos, state, player);
                return;
            }

            MultiBlockTools.breakMultiblock(this.Type, world, pos, state.get(FORMED));
        }
        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {

        if (!world.isRemote) {
            if (player.getHeldItem(hand).getItem() == this.Type.getFormationItem()) {
                this.Type.toggleMultiBlock(world, pos, state, player);
                return true;
            }
            // Only work if the block is formed
            Class blockClass = state.getBlock().getClass();
            if (this.Type.getMenuAccessibleBlockClasses().contains(blockClass) && state.get(FORMED) != TerraReactorPartIndex.UNFORMED) {

                if (this.Type.isController(blockClass)) {
                    TileEntity tileEntity = world.getTileEntity(pos);

                    if (tileEntity instanceof INamedContainerProvider) {
                        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
                        return true;
                    }

                    return super.onBlockActivated(state, world, pos, player, hand, hit);
                }

                BlockPos controllerPos = this.Type.getControllerPosition(pos, state.get(FORMED));
                if (controllerPos != null) {
                    BlockState controllerState = world.getBlockState(controllerPos);
                    return controllerState.getBlock().onBlockActivated(controllerState, world, controllerPos, player, hand, hit);
                }
            }
        }

        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    protected IProperty<?>[] getFillStateProperties() {
        return new IProperty<?>[]{FORMED};
    }

    protected BlockState getDefaultReactorState() {
        return getStateContainer().getBaseState().with(FORMED, TerraReactorPartIndex.UNFORMED);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(this.getFillStateProperties());
    }
}
