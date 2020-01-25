package com.agrejus.netherendingenergy.blocks;

import com.agrejus.netherendingenergy.common.interfaces.FunctionBlockPosition;
import com.agrejus.netherendingenergy.common.interfaces.IVaporStorage;
import com.agrejus.netherendingenergy.tools.CapabilityVapor;
import com.agrejus.netherendingenergy.tools.CustomVaporStorage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TerraVaporCollectorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private LazyOptional<IVaporStorage> vaporStorage = LazyOptional.of(this::createVapor);
    private List<CausticBellTile> surroundingCausticBells;
    private int counter;
    private final int maxVaporCapacity = 4000;
    private final int maxVaporTransfer = 100;

    // Follow AbstractFurnaceTileEntity
    public TerraVaporCollectorTile() {
        super(ModBlocks.TERRA_VAPOR_COLLECTOR_TILE);
    }

    private IVaporStorage createVapor() {
        return new CustomVaporStorage(maxVaporCapacity, maxVaporTransfer);
    }

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        if (counter > 0) {
            //System.out.println(surroundingCausticBells.size());
            counter--;
            if (counter <= 0) {
                // going from 1 to 0, will reset next, do operation here

                // Collect Gas here
                // get block positions.  Only check every 20 ticks for performance reasons
                surroundingCausticBells = getSurroundingCausticBells();

                for (CausticBellTile bell : surroundingCausticBells) {

                    float strength = bell.getStrength();
                    float yield = bell.getYield(); // mB

                    int toAdd = (int) (strength * yield);
                    vaporStorage.ifPresent(w -> ((CustomVaporStorage) w).addVapor(toAdd));
                }
            }
            markDirty();
        }

        if (counter <= 0) {
            // Start of the operation

            counter = 20;
        }
    }

    private List<CausticBellTile> getSurroundingCausticBells() {
        List<CausticBellTile> bells = new ArrayList<CausticBellTile>();
        List<TileEntity> surroundingBlocks = getSurroundingTiles();

        for (TileEntity tileEntity : surroundingBlocks) {
            if (tileEntity instanceof CausticBellTile) {
                bells.add((CausticBellTile) tileEntity);
            }
        }

        return bells;
    }

    private List<TileEntity> getSurroundingTiles() {
        List<TileEntity> tileEntities = new ArrayList<TileEntity>();
        List<FunctionBlockPosition> result = new ArrayList<FunctionBlockPosition>();

        result.add(() -> this.pos.down());
        result.add(() -> this.pos.up());
        result.add(() -> this.pos.east());
        result.add(() -> this.pos.west());
        result.add(() -> this.pos.north());
        result.add(() -> this.pos.south());

        result.add(() -> this.pos.north().east());
        result.add(() -> this.pos.east().south());
        result.add(() -> this.pos.south().west());
        result.add(() -> this.pos.west().north());

        result.add(() -> this.pos.down().north().east());
        result.add(() -> this.pos.down().east().south());
        result.add(() -> this.pos.down().south().west());
        result.add(() -> this.pos.down().west().north());

        result.add(() -> this.pos.up().north().east());
        result.add(() -> this.pos.up().east().south());
        result.add(() -> this.pos.up().south().west());
        result.add(() -> this.pos.up().west().north());

        result.add(() -> this.pos.down().north());
        result.add(() -> this.pos.down().east());
        result.add(() -> this.pos.down().south());
        result.add(() -> this.pos.down().west());

        result.add(() -> this.pos.up().north());
        result.add(() -> this.pos.up().east());
        result.add(() -> this.pos.up().south());
        result.add(() -> this.pos.up().west());

        for (FunctionBlockPosition func : result) {
            BlockPos pos = func.operation();

            if (pos == null) {
                continue;
            }

            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity == null) {
                continue;
            }

            tileEntities.add(tileEntity);
        }

        return tileEntities;
    }

    @Override
    public void read(CompoundNBT tag) {
        // When block is broken?


        CompoundNBT energyTag = tag.getCompound("vapor");
        // Save energy when block is broken
        vaporStorage.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        // Write energy when block is placed
        vaporStorage.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("vapor", compound);
        });
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityVapor.VAPOR) {
            return vaporStorage.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int worldId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new TerraVaporCollectorContainer(worldId, world, pos, playerInventory, playerEntity);
    }
}
