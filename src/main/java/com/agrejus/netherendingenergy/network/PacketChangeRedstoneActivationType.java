package com.agrejus.netherendingenergy.network;

import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import com.agrejus.netherendingenergy.common.interfaces.IRedstoneActivatable;
import com.agrejus.netherendingenergy.network.base.ServerMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChangeRedstoneActivationType extends ServerMessage {
    private final BlockPos pos;
    private final RedstoneActivationType redstoneActivationType;

    public PacketChangeRedstoneActivationType(PacketBuffer buf) {

        this.pos = buf.readBlockPos();
        String name = buf.readString();

        if (name != null) {
            this.redstoneActivationType = RedstoneActivationType.get(name);
        } else {
            this.redstoneActivationType = null;
        }
    }

    @Override
    public void writeBytes(PacketBuffer buf) {
        buf.writeBlockPos(this.pos);
        if (this.redstoneActivationType != null) {
            buf.writeString(redstoneActivationType.getName());
        }
    }

    public PacketChangeRedstoneActivationType(BlockPos pos, RedstoneActivationType redstoneActivationType) {
        this.pos = pos;
        this.redstoneActivationType = redstoneActivationType;
    }

    public void handleWork(Supplier<NetworkEvent.Context> ctx, World world) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null && tileEntity instanceof IRedstoneActivatable && this.redstoneActivationType != null) {

            IRedstoneActivatable entity = (IRedstoneActivatable) tileEntity;
            entity.setRedstoneActivationType(this.redstoneActivationType);
            tileEntity.markDirty();
        }
    }
}
