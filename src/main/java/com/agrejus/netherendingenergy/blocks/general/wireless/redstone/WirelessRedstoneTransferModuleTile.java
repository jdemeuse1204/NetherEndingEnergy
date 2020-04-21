package com.agrejus.netherendingenergy.blocks.general.wireless.redstone;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleTileBase;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;

public class WirelessRedstoneTransferModuleTile extends ModuleTileBase implements ITickableTileEntity {

    public WirelessRedstoneTransferModuleTile() {
        super(ModBlocks.WIRELESS_REDSTONE_TRANSFER_MODULE_TILE);
    }

    private int redstonePower;

    @Override
    protected int getTransferTickRate() {
        return 0; // 0 = disabled
    }

    public int getRedstonePower() {
        return redstonePower;
    }

    @Override
    public void tick() {
        super.tick();

        if (world.isRemote) {
            return;
        }

        if (this.isSource() == true) {
            return;
        }

        BlockPos linkedBlockPosition = getLinkedBlockPosition();

        if (linkedBlockPosition == null || this.isOutOfRange() == true) {
            return;
        }

        int currentRedstonePower = world.getRedstonePowerFromNeighbors(getLinkedBlockPosition());

        if (currentRedstonePower != this.redstonePower) {
            this.redstonePower = currentRedstonePower;
            this.update(currentRedstonePower);
        }
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        this.redstonePower = tag.getInt("redstone_power");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag.putInt("redstone_power", this.redstonePower);
        return super.write(tag);
    }

    private void update(int power) {
        BlockState state = world.getBlockState(getPos());
        BlockState newState = state.with(BlockStateProperties.POWER_0_15, power);

        world.setBlockState(pos, newState, 3);
        world.notifyBlockUpdate(pos, state, newState, 3);
    }
}
