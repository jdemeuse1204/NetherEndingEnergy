package com.agrejus.netherendingenergy.blocks.general.wireless;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.ILocationDiscoverable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class ModuleTileBase extends TileEntity implements ITickableTileEntity, ILocationDiscoverable {

    private final int maxRange = 12;
    private BlockPos linkedBlockPosition;
    private boolean isSource;
    private boolean isOutOfRange;

    private int tick;
    private int tickTransfer;
    private int tickLocationParticles;

    public ModuleTileBase(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
        this.isOutOfRange = true;
    }

    public BlockPos getDestination() {
        BlockState state = world.getBlockState(pos);
        Direction facing = state.get(BlockStateProperties.FACING);
        return pos.offset(facing);
    }

    public Direction getAttachedFace() {
        BlockState state = world.getBlockState(pos);
        return state.get(BlockStateProperties.FACING).getOpposite();
    }

    public void clear() {
        this.isSource = false;
        this.setLinkedBlockPosition(null);
    }

    public void showLocationParticles() {
        // user packets to send to client from server
        this.tickLocationParticles = 200;
    }

    public void setLinkedBlockPosition(BlockPos pos) {
        this.linkedBlockPosition = pos;
        this.updateBlock();
        markDirty();
    }

    public BlockPos getLinkedBlockPosition() {
        return this.linkedBlockPosition;
    }

    public void updateBlock() {
        BlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 3);
    }

    public void setSource(boolean source) {
        isSource = source;
    }

    public boolean isSource() {
        return isSource;
    }

    protected final boolean isLinked() {
        BlockPos linkedPosition = this.getLinkedBlockPosition();
        if (linkedPosition == null) {
            return false;
        }

        TileEntity tileEntity = world.getTileEntity(linkedPosition);

        if (tileEntity instanceof ModuleTileBase) {
            ModuleTileBase module = (ModuleTileBase) tileEntity;

            return pos.equals(module.getLinkedBlockPosition());
        }

        return false;
    }

    protected ModuleTileBase getLinkedModule() {
        BlockPos linkedPosition = this.getLinkedBlockPosition();
        if (linkedPosition == null) {
            return null;
        }

        TileEntity tileEntity = world.getTileEntity(linkedPosition);

        if (tileEntity == null) {
            return null;
        }

        if (tileEntity instanceof ModuleTileBase) {
            return (ModuleTileBase) tileEntity;
        }

        return null;
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

    private void readNBT(CompoundNBT tag) {
        if (tag.contains("linked_position")) {
            CompoundNBT linkedPositionNbt = (CompoundNBT) tag.get("linked_position");
            this.linkedBlockPosition = NBTHelpers.readBlockPosFromNBT(linkedPositionNbt);
        }

        this.isSource = tag.getBoolean("is_source");
        this.isOutOfRange = tag.getBoolean("is_out_of_range");
    }

    private void writeNBT(CompoundNBT tag) {
        if (this.linkedBlockPosition != null) {
            CompoundNBT linkedPositionNbt = new CompoundNBT();
            NBTHelpers.writeToNBT(linkedPositionNbt, this.linkedBlockPosition);
            tag.put("linked_position", linkedPositionNbt);
        }

        tag.putBoolean("is_source", this.isSource);
        tag.putBoolean("is_out_of_range", this.isOutOfRange);
    }

    protected void onProcess(boolean isOutOfRange) {
    }

    @Override
    public void tick() {

        if (world.isRemote) {

            // Show particle effects for location of module
            if (this.tickLocationParticles > 0) {

                if (this.tickLocationParticles % 5 == 0) {
                    BlockState state = world.getBlockState(pos);
                    this.animateTick(state, world, pos);
                }
                this.tickLocationParticles--;
            }

            return;
        }

        if (this.isOutOfRange == true) {

            if (this.isLinked()) {
                this.isOutOfRange = this.isOutOfRange();
            }
        }

        // Change the antenna light
        if (tick >= 40) {
            BlockState state = world.getBlockState(pos);
            boolean powered = state.get(BlockStateProperties.POWERED);

            if (this.isLinked()) {
                this.isOutOfRange = this.isOutOfRange();
            } else {
                this.isOutOfRange = true;
            }

            if (isOutOfRange == true) {
                BlockState newState = state.with(BlockStateProperties.POWERED, Boolean.valueOf(false));
                world.setBlockState(pos, newState);
                world.notifyBlockUpdate(pos, state, newState, 3);
            }
            if (powered == true && this.linkedBlockPosition == null) {
                BlockState newState = state.with(BlockStateProperties.POWERED, Boolean.valueOf(false));
                world.setBlockState(pos, newState);
                world.notifyBlockUpdate(pos, state, newState, 3);
            } else if (powered == false && this.linkedBlockPosition != null && isLinked() == true) {
                BlockState newState = state.with(BlockStateProperties.POWERED, Boolean.valueOf(true));
                world.setBlockState(pos, newState);
                world.notifyBlockUpdate(pos, state, newState, 3);
            } else if (powered == true && this.linkedBlockPosition != null && isLinked() == false) {
                BlockState newState = state.with(BlockStateProperties.POWERED, Boolean.valueOf(false));
                world.setBlockState(pos, newState);
                world.notifyBlockUpdate(pos, state, newState, 3);
            }

            this.tick = 0;
        }

        int transferTickRate = this.getTransferTickRate();
        if (transferTickRate > 0){
            if (this.tickTransfer % transferTickRate == 0) {
                this.onProcess(isOutOfRange);
            }

            int maxTickTransfer = transferTickRate * 50;
            if (this.tickTransfer > maxTickTransfer) {
                this.tickTransfer = 0;
            }

            tickTransfer++;
        }

        tick++;
    }

    protected boolean isOutOfRange() {
        int distance = BlockHelpers.getThreeDimensionalDistance(pos, this.getLinkedBlockPosition());

        return distance > this.maxRange;
    }

    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos) {
        Random rand = NetherEndingEnergy.random;
        Block block = stateIn.getBlock();
        VoxelShape voxelshape = block.getShape(stateIn, worldIn, pos, ISelectionContext.dummy());
        Vec3d vec3d = voxelshape.getBoundingBox().getCenter();
        double d0 = (double) pos.getX() + vec3d.x;
        double d1 = (double) pos.getZ() + vec3d.z;

        for (int i = 0; i < 3; ++i) {
            if (rand.nextBoolean()) {
                worldIn.addParticle(ParticleTypes.PORTAL, d0 + (double) (rand.nextFloat() / 5.0F), (double) pos.getY() + (0.5D - (double) rand.nextFloat()), d1 + (double) (rand.nextFloat() / 5.0F), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    protected int getTransferTickRate() {
        return 1;
    }
}
