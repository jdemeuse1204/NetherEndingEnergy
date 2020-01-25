package com.agrejus.netherendingenergy.tileentity;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import java.lang.ref.WeakReference;

public class TileInterface extends CyclopsTileEntity {

    @Delegate
    private final ITickingTile tickingTileComponent = new TickingTileComponent(this);

    @NBTPersist
    @Getter
    private Vec3i corePosition = null;
    private WeakReference<TileColossalChest> coreReference = new WeakReference<TileColossalChest>(null);

    public TileInterface() {
        super(RegistryEntries.TILE_ENTITY_INTERFACE);
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, Direction facing) {
        TileColossalChest core = getCore();
        if (core != null) {
            LazyOptional<T> t = core.getCapability(capability, facing);
            if (t.isPresent()) {
                return t;
            }
        }
        return super.getCapability(capability, facing);
    }

    public void setCorePosition(Vec3i corePosition) {
        this.corePosition = corePosition;
        coreReference = new WeakReference<TileColossalChest>(null);
    }

    protected TileColossalChest getCore() {
        if(corePosition == null) {
            return null;
        }
        if (coreReference.get() == null) {
            coreReference = new WeakReference<TileColossalChest>(
                    TileHelpers.getSafeTile(getWorld(), new BlockPos(corePosition), TileColossalChest.class).orElse(null));
        }
        return coreReference.get();
    }

}
