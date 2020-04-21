package com.agrejus.netherendingenergy.blocks.general.wireless;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.blocks.base.tile.NEETileEntity;
import com.agrejus.netherendingenergy.common.enumeration.TransferMode;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.ILinkableTile;
import com.agrejus.netherendingenergy.common.interfaces.ILocationDiscoverable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
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

import java.util.Random;

public abstract class ModuleTileBase extends NEETileEntity implements ITickableTileEntity, ILocationDiscoverable, ILinkableTile {

    private final int maxRange = 12;
    private BlockPos linkedBlockPosition;
    private TransferMode transferMode = TransferMode.NONE;
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

    protected boolean isSource() {
        return this.transferMode != null && this.transferMode == TransferMode.SEND;
    }

    public Direction getAttachedFace() {
        BlockState state = world.getBlockState(pos);
        return state.get(BlockStateProperties.FACING).getOpposite();
    }

    public void showLocationParticles() {
        // user packets to send to client from server
        this.tickLocationParticles = 200;
    }

    public BlockPos getLinkedBlockPosition() {
        return this.linkedBlockPosition;
    }

    public void updateBlock() {
        BlockState state = world.getBlockState(getPos());
        world.notifyBlockUpdate(getPos(), state, state, 3);
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

    public final boolean isConnectedTo(ModuleTileBase linkedModule) {

        // Ensure blocks are connected to each other
        BlockPos otherLinkedPos = linkedModule.getLinkedBlockPosition();
        BlockPos otherPos = linkedModule.getPos();
        BlockPos linkedPos = this.getLinkedBlockPosition();
        return this.pos != null && otherLinkedPos != null && this.pos.equals(otherLinkedPos) &&
                otherPos != null && otherPos.equals(linkedPos);
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
    protected void readNBT(CompoundNBT tag) {
        if (tag.contains("linked_position")) {
            CompoundNBT linkedPositionNbt = (CompoundNBT) tag.get("linked_position");
            this.linkedBlockPosition = NBTHelpers.readBlockPosFromNBT(linkedPositionNbt);
        }

        String transferMode = tag.getString("transfer_mode");
        this.transferMode = TransferMode.byName(transferMode);
        this.isOutOfRange = tag.getBoolean("is_out_of_range");
    }

    @Override
    protected void writeNBT(CompoundNBT tag) {
        if (this.linkedBlockPosition != null) {
            CompoundNBT linkedPositionNbt = new CompoundNBT();
            NBTHelpers.writeToNBT(linkedPositionNbt, this.linkedBlockPosition);
            tag.put("linked_position", linkedPositionNbt);
        }

        tag.putString("transfer_mode", this.transferMode.getName());
        tag.putBoolean("is_out_of_range", this.isOutOfRange);
    }

    protected void onProcess(boolean isOutOfRange) {
    }

    @Override
    protected void clientTick() {
        // Show particle effects for location of module
        if (this.tickLocationParticles > 0) {

            if (this.tickLocationParticles % 5 == 0) {
                BlockState state = world.getBlockState(pos);
                this.animateTick(state, world, pos);
            }
            this.tickLocationParticles--;
        }
    }

    @Override
    protected void serverTick() {
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
        if (transferTickRate > 0) {
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

    @Override
    public boolean addLink(BlockPos pos) {
        this.linkedBlockPosition = pos;
        this.updateBlock();
        markDirty();
        return true;
    }

    @Override
    public boolean removeLink(BlockPos pos) {
        this.linkedBlockPosition = null;
        this.transferMode = TransferMode.NONE;
        this.updateBlock();
        markDirty();
        return true;
    }

    @Override
    public void setLinkMode(TransferMode mode) {
        this.transferMode = mode;
        this.updateBlock();
        markDirty();
    }

    @Override
    public TransferMode getLinkMode() {
        return this.transferMode;
    }

    @Override
    public int clearLinks() {
        int linksCleared = 0;
        if (this.linkedBlockPosition != null) {
            linksCleared = 1;
        }
        this.linkedBlockPosition = null;
        this.transferMode = TransferMode.NONE;
        return linksCleared;
    }

    @Override
    public int maxAllowedLinks() {
        return 1;
    }

    @Override
    public BlockPos[] getLinks() {

        if (this.linkedBlockPosition == null) {
            return new BlockPos[0];
        }

        return new BlockPos[]{
                this.linkedBlockPosition
        };
    }

    @Override
    public int totalLinks() {
        int totalLinks = 0;
        if (this.linkedBlockPosition != null) {
            totalLinks = 1;
        }
        return totalLinks;
    }

    @Override
    public void updateTile() {
        this.updateBlock();
    }
}
