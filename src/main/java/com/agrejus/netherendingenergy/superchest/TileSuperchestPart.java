package com.agrejus.netherendingenergy.superchest;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.TerraVaporCollectorContainer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class TileSuperchestPart extends TileEntity implements INamedContainerProvider {

    public TileSuperchestPart() {
        super(ModBlocks.TYPE_SUPERCHEST_PART);
    }

    // @todo 1.13
//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) {
//        return oldState.getBlock() != newState.getBlock();
//    }

    @Nullable
    @Override
    public Container createMenu(int worldId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new TerraVaporCollectorContainer(worldId, world, pos, playerInventory, playerEntity);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocks.blockSuperchestPart || state.get(BlockSuperchest.FORMED) == SuperchestPartIndex.UNFORMED) {
            return null;
        }

        BlockPos controllerPos = BlockSuperchest.getControllerPos(world, pos);
        if (controllerPos != null) {
            TileEntity te = world.getTileEntity(controllerPos);
            if (te instanceof TileSuperchest) {
                return te.getCapability(capability);
            }
        }
        return super.getCapability(capability);
    }
}
