package com.agrejus.netherendingenergy.blocks.terra.link;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.base.tile.NEETileEntity;
import com.agrejus.netherendingenergy.common.enumeration.TransferMode;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.ILinkableTile;
import com.agrejus.netherendingenergy.common.tank.MixableAcidFluidTank;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TerraLinkTile extends NEETileEntity implements ILinkableTile, INamedContainerProvider {

    private int tick;
    protected int transferRate = 10000;
    private boolean isOutOfRange;
    private TransferMode transferMode = TransferMode.NONE;
    private ArrayList<BlockPos> links = new ArrayList<>();
    private MixableAcidFluidTank buffer = new MixableAcidFluidTank(this.transferRate) {
        @Nonnull
        @Override
        public FluidStack drain(FluidStack resource, FluidAction action) {

            if (transferMode != TransferMode.RECEIVE) {
                return FluidStack.EMPTY;
            }

            return super.drain(resource, action);
        }

        @Nonnull
        @Override
        public FluidStack drain(int maxDrain, FluidAction action) {

            if (transferMode != TransferMode.RECEIVE) {
                return FluidStack.EMPTY;
            }

            return super.drain(maxDrain, action);
        }

        @Override
        public int fill(FluidStack resource, FluidAction action) {

            if (transferMode == TransferMode.RECEIVE) {
                return super.fill(resource, action);
            }

            if (transferMode == TransferMode.SEND) {
                int amount = resource.getAmount();

                // Round Robin
                int size = links.size();
                int split = amount / size;
                int splitFillAmount = Math.min(split, transferRate);
                AtomicInteger amountLeft = new AtomicInteger(amount);

                for (int i = 0; i < size; i++) {

                    if (amountLeft.get() == 0) {
                        break;
                    }

                    BlockPos link = links.get(i);

                    if (link == null) {
                        continue;
                    }

                    TileEntity tileEntity = world.getTileEntity(link);

                    if (tileEntity == null || (tileEntity instanceof TerraLinkTile) == false) {
                        continue;
                    }

                    TerraLinkTile receivingTileEntity = (TerraLinkTile) tileEntity;
                    TransferMode destinationMode = receivingTileEntity.getLinkMode();

                    if (destinationMode != TransferMode.RECEIVE) {
                        continue;
                    }

                    // Fill receivers
                    receivingTileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).ifPresent(receiver -> {
                        FluidStack copiedStack = resource.copy();
                        copiedStack.setAmount(splitFillAmount);
                        int amountFilled = receiver.fill(copiedStack, action);

                        if (amountFilled > 0) {
                            amountLeft.addAndGet(-amountFilled);
                        }
                    });
                }

                return amount - amountLeft.get();
            }

            return 0;
        }
    };

    public TerraLinkTile() {
        super(ModBlocks.TERRA_LINK_TILE);
    }

    @Override
    protected void clientTick() {

    }

    @Override
    protected void serverTick() {

        if (this.tick == 0) {
            // check to see if wear are in range
            this.isOutOfRange = this.isOutOfRange();
        }

        ++this.tick;

        if (this.tick >= 20) {
            this.tick = 0;
        }
    }

    protected boolean isOutOfRange() {
        return world.getDimension().getType() != DimensionType.OVERWORLD;
    }

    @Override
    protected void readNBT(CompoundNBT tag) {

        String transferMode = tag.getString("transfer_mode");
        this.transferMode = TransferMode.byName(transferMode);
        this.isOutOfRange = tag.getBoolean("is_out_of_range");
        int size = tag.getInt("size");
        CompoundNBT allLinksTag = (CompoundNBT) tag.get("linked_positions");

        for (int i = 0; i < size; i++) {
            CompoundNBT linksTag = (CompoundNBT) allLinksTag.get("link_" + i);
            BlockPos pos = NBTHelpers.readBlockPosFromNBT(linksTag);
            this.links.add(pos);
        }

        CompoundNBT bufferTag = (CompoundNBT) tag.get("buffer");
        buffer.readFromNBT(bufferTag);
    }

    @Override
    protected void writeNBT(CompoundNBT tag) {
        tag.putString("transfer_mode", this.transferMode.getName());
        tag.putBoolean("is_out_of_range", this.isOutOfRange);

        CompoundNBT allLinksTag = new CompoundNBT();
        int size = links.size();

        for (int i = 0; i < size; i++) {
            CompoundNBT linksTag = new CompoundNBT();
            BlockPos link = links.get(i);
            NBTHelpers.writeToNBT(linksTag, link);
            allLinksTag.put("link_" + i, linksTag);
        }

        tag.putInt("size", size);
        tag.put("linked_positions", allLinksTag);

        CompoundNBT bufferTag = new CompoundNBT();
        buffer.writeToNBT(bufferTag);
        tag.put("buffer", bufferTag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) buffer);
        }

        return super.getCapability(cap, side);
    }

    @Override
    public boolean hasLink(BlockPos pos) {
        return this.links.contains(pos);
    }

    @Override
    public boolean addLink(BlockPos pos) {
        return this.links.add(pos);
    }

    @Override
    public BlockPos[] getLinks() {
        return this.links.toArray(new BlockPos[links.size()]);
    }

    @Override
    public boolean removeLink(BlockPos pos) {
        return this.links.remove(pos);
    }

    @Override
    public void setLinkMode(TransferMode mode) {
        this.transferMode = mode;
        this.markDirty();
        this.update();
    }

    @Override
    public TransferMode getLinkMode() {
        return this.transferMode;
    }

    @Override
    public int clearLinks() {
        int totalLinks = this.links.size();
        this.links = new ArrayList<>();
        return totalLinks;
    }

    @Override
    public int maxAllowedLinks() {
        return 10;
    }

    @Override
    public int totalLinks() {
        return this.links.size();
    }

    @Override
    public void updateTile() {
        this.update();
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int worldId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new TerraLinkContainer(worldId, world, pos, playerInventory, playerEntity);
    }
}
