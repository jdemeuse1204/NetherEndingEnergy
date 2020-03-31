package com.agrejus.netherendingenergy.common.tileentity;

import com.agrejus.netherendingenergy.common.interfaces.ITickingTask;
import com.agrejus.netherendingenergy.common.interfaces.ITickingTaskPayload;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.List;

public abstract class TickingTaskTileEntity<T extends TileEntity, U extends ITickingTaskPayload> extends TileEntity implements ITickableTileEntity {

    private List<ITickingTask<T, U>> clientTasks = this.getClientTasks();
    private List<ITickingTask<T, U>> serverTasks = this.getServerTasks();

    public TickingTaskTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    protected abstract T getEntity();

    protected abstract U getPayload();

    protected abstract List<ITickingTask<T, U>> getServerTasks();

    protected abstract List<ITickingTask<T, U>> getClientTasks();

    @Override
    public void tick() {

        U payload = this.getPayload();
        T entity = this.getEntity();
        List<ITickingTask<T, U>> tasks;

        if (world.isRemote) {
            tasks = this.clientTasks;
        } else {
            tasks = this.serverTasks;
        }

        int size = tasks.size();

        for (int i = 0; i < size; i++) {
            ITickingTask<T, U> task = tasks.get(i);
            if (task.run(entity, payload) == false) {
                break;
            }
        }
    }
}
