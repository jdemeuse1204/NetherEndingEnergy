package com.agrejus.netherendingenergy.common.interfaces;

import com.agrejus.netherendingenergy.common.interfaces.ITickingTaskPayload;
import net.minecraft.tileentity.TileEntity;

import java.util.function.BiConsumer;

public interface ITickingTaskFunction<T extends TileEntity, U extends ITickingTaskPayload> extends BiConsumer<T, U> {

}
