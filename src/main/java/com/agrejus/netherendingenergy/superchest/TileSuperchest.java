package com.agrejus.netherendingenergy.superchest;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

public class TileSuperchest extends TileEntity implements INamedContainerProvider {

    public TileSuperchest() {
        super(ModBlocks.TYPE_SUPERCHEST);
    }

    // @todo 1.13
//    @Override
//    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) {
//        return oldState.getBlock() != newState.getBlock();
//    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);

/*        if (compound.hasKey("items")) {
            itemHandler.deserializeNBT((CompoundNBT) compound.getTag("items"));
        }*/
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        //compound.setTag("items", itemHandler.serializeNBT());
        return super.write(compound);
    }

    public boolean canInteractWith(PlayerEntity playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isRemoved() && playerIn.getDistanceSq(0.5D, 0.5D, 0.5D) <= 64D;
    }

//    @Override
//    public Container createContainer(EntityPlayer player) {
//        return new ContainerSuperchest(player.inventory, this);
//    }

    @Nullable
    @Override
    public Container createMenu(int worldId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new ContainerSuperchest(worldId, world, pos, playerInventory, playerEntity);
    }

    private ItemStackHandler itemHandler = new ItemStackHandler(3*9) {

        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            TileSuperchest.this.markDirty();
        }
    };

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocks.blockSuperchest || state.get(BlockSuperchest.FORMED) == SuperchestPartIndex.UNFORMED) {
            return null;
        }

        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) itemHandler);
        }
        return super.getCapability(capability);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }
}
