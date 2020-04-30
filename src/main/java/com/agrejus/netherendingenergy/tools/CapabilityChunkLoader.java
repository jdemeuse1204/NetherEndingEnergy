package com.agrejus.netherendingenergy.tools;

import com.agrejus.netherendingenergy.common.chunkloading.ChunkLoaderList;
import com.agrejus.netherendingenergy.common.interfaces.IChunkLoaderList;
import com.agrejus.netherendingenergy.common.interfaces.IVaporStorage;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityChunkLoader {
    @CapabilityInject(IChunkLoaderList.class)
    public static Capability<IChunkLoaderList> CHUNK_LOADER = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IChunkLoaderList.class, new ChunkLoaderList.Storage(), () -> new ChunkLoaderList(null));
    }
}
