package com.agrejus.netherendingenergy.blocks.terra.mixer.tasks;

import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerTile;
import com.agrejus.netherendingenergy.blocks.terra.mixer.tasks.payload.TerraMixerTaskPayload;
import com.agrejus.netherendingenergy.common.interfaces.ITickingTask;

public class ConsumeInventoryItemTask implements ITickingTask<TerraMixerTile, TerraMixerTaskPayload> {
    @Override
    public boolean run(TerraMixerTile tileEntity, TerraMixerTaskPayload payload) {
        return true;
    }
}