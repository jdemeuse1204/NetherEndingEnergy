package com.agrejus.netherendingenergy.blocks.terra.mixer.tasks;

import com.agrejus.netherendingenergy.blocks.terra.mixer.TerraMixerTile;
import com.agrejus.netherendingenergy.blocks.terra.mixer.tasks.payload.TerraMixerTaskPayload;
import com.agrejus.netherendingenergy.common.interfaces.ITickingTask;

public class TryRemoveCurrentRecipeTask implements ITickingTask<TerraMixerTile, TerraMixerTaskPayload> {
    @Override
    public boolean run(TerraMixerTile tileEntity, TerraMixerTaskPayload payload) {
/*        if (payload.getInputFluidStack().getAmount() == 0 || this.destructibleItemTicks == 0) {
            this.currentRecipe = null;
        }*/
        return true;
    }
}
