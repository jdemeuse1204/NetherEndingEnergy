package com.agrejus.netherendingenergy.common.factories;

import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerTile;
import com.agrejus.netherendingenergy.blocks.terra.mixer.tasks.TryRemoveCurrentRecipeTask;
import com.agrejus.netherendingenergy.blocks.terra.mixer.tasks.payload.TerraMixerTaskPayload;
import com.agrejus.netherendingenergy.common.interfaces.ITickingTask;

import java.util.ArrayList;
import java.util.List;

public class TickingTaskFactory {
    public static List<ITickingTask<TerraMixerTile, TerraMixerTaskPayload>> createServerTasks(TerraMixerTile tile) {
        return new ArrayList<ITickingTask<TerraMixerTile, TerraMixerTaskPayload>>() {
            {add(new TryRemoveCurrentRecipeTask());}
        };
    }
}
