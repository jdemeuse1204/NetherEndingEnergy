package com.agrejus.netherendingenergy.common.interfaces;

import net.minecraft.tileentity.TileEntity;

public interface ITickingTask<T extends TileEntity, U extends ITickingTaskPayload> {
    boolean run(T tileEntity, U payload);
}
