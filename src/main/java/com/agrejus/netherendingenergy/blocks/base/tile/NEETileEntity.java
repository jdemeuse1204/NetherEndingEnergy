package com.agrejus.netherendingenergy.blocks.base.tile;

import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import com.agrejus.netherendingenergy.common.enumeration.TileEntityUpdateType;
import com.agrejus.netherendingenergy.common.models.MixerRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public abstract class NEETileEntity extends TileEntity implements ITickableTileEntity {

    public NEETileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    private TileEntityUpdateType updateType = TileEntityUpdateType.NONE;

    @Override
    public void tick() {
        if (world.isRemote) {
            this.beforeClientTick();
            this.clientTick();
            this.afterClientTick();
            return;
        }

        this.beforeServerTick();
        this.serverTick();
        this.afterServerTick();
    }

    protected void beforeServerTick() {

    }

    protected void beforeClientTick() {

    }

    protected void afterServerTick() {
        this.processUpdate();
    }

    protected void afterClientTick() {

    }

    protected void markUpdate() {
        this.updateType = TileEntityUpdateType.UPDATE;
    }

    protected void markUpdateAndDirty() {
        this.updateType = TileEntityUpdateType.MARK_DIRTY_AND_UPDATE;
    }

    @Override
    public void markDirty() {
        this.updateType = TileEntityUpdateType.MARK_DIRTY;
    }

    protected boolean hasRedstonePower() {
        return this.getRedstonePower() > 0;
    }

    protected int getRedstonePower() {
        return world.getRedstonePowerFromNeighbors(pos);
    }

    protected void clientTick() {

    }

    protected void serverTick() {

    }

    protected void processUpdate() {
        switch (updateType) {
            default:
            case NONE:
                break;
            case UPDATE:
                this.update();
                break;
            case MARK_DIRTY:
                super.markDirty();
                break;
            case MARK_DIRTY_AND_UPDATE:
                this.update();
                super.markDirty();
                break;
        }

        this.updateType = TileEntityUpdateType.NONE;
    }

    protected void update() {
        BlockState state = world.getBlockState(pos);
        this.update(state);
    }

    protected void update(BlockState newBlockState) {
        BlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, newBlockState, 3);
    }

    @Override
    public void read(CompoundNBT tag) {
        readNBT(tag);
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        writeNBT(tag);
        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        writeNBT(tag);
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        CompoundNBT nbt = packet.getNbtCompound();
        readNBT(nbt);
    }

    protected void readNBT(CompoundNBT tag) {

    }

    protected void writeNBT(CompoundNBT tag) {

    }
}
