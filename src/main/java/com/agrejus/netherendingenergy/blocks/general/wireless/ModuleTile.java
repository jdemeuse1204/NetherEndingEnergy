package com.agrejus.netherendingenergy.blocks.general.wireless;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public abstract class ModuleTile<C> extends ModuleTileBase {

    private final Capability<C> capability;

    public ModuleTile(TileEntityType<?> tileEntityTypeIn, Capability<C> capability) {
        super(tileEntityTypeIn);
        this.capability = capability;
    }

    @Override
    protected void onProcess(boolean isOutOfRange) {

        if (isOutOfRange == true) {
            return;
        }

        // Actively Pulling, remote get capability stuff, otherwise we cannot accept and pull at the same time
        if (this.isSource() == true) {

            ModuleTileBase linkedModule = this.getLinkedModule();

            if (linkedModule != null && linkedModule.isConnectedTo(this)) {

                TileEntity pullingTileEntity = world.getTileEntity(this.getDestination());
                TileEntity receivingTileEntity = world.getTileEntity(linkedModule.getDestination());

                if (pullingTileEntity != null && receivingTileEntity != null) {

                    Direction destinationFace = linkedModule.getAttachedFace();
                    Direction sourceFace = this.getAttachedFace();

                    pullingTileEntity.getCapability(this.capability, sourceFace).ifPresent(pulling -> {
                        receivingTileEntity.getCapability(this.capability, destinationFace).ifPresent(receiving -> {
                            this.onTransfer(pullingTileEntity, receivingTileEntity, linkedModule, pulling, receiving);
                        });
                    });
                }
            }
        }
    }

    protected abstract void onTransfer(TileEntity pullingTileEntity, TileEntity receivingTileEntity, ModuleTileBase linkedModule, C pulling, C receiving);
}