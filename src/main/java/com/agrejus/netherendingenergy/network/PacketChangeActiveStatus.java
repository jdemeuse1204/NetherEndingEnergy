package com.agrejus.netherendingenergy.network;

import com.agrejus.netherendingenergy.common.interfaces.IActivatable;
import com.agrejus.netherendingenergy.network.base.ServerMessage;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketChangeActiveStatus  extends ServerMessage {
    private final BlockPos pos;
    private final boolean isActive;

    public PacketChangeActiveStatus(PacketBuffer buf) {

        this.pos = buf.readBlockPos();
        this.isActive = buf.readBoolean();
    }

    @Override
    public void writeBytes(PacketBuffer buf) {
        buf.writeBlockPos(this.pos);
        buf.writeBoolean(this.isActive);
    }

    public PacketChangeActiveStatus(BlockPos pos, boolean isActive) {
        this.pos = pos;
        this.isActive = isActive;
    }

    public void handleWork(Supplier<NetworkEvent.Context> ctx, World world) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null && tileEntity instanceof IActivatable) {

            IActivatable entity = (IActivatable) tileEntity;
            entity.setActive(this.isActive);
            tileEntity.markDirty();
        }
    }
}
