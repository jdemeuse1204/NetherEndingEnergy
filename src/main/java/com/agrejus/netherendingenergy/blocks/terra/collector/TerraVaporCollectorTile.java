package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import com.agrejus.netherendingenergy.common.interfaces.FunctionBlockPosition;
import com.agrejus.netherendingenergy.common.interfaces.IVaporStorage;
import com.agrejus.netherendingenergy.tools.CapabilityVapor;
import com.agrejus.netherendingenergy.tools.CustomVaporStorage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TerraVaporCollectorTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private LazyOptional<IFluidHandler> fluid = LazyOptional.of(this::createFluid);
    private List<CausticBellTile> surroundingCausticBells;
    private int counter;
    private final TerraVaporCollectorBlock block;

    private final int maxVaporCapacity = 400000;
    private final int maxVaporTransfer = 10000;
    private final int efficency = 65;
    private FluidTank tank = new FluidTank(10000) {
        @Override
        protected void onContentsChanged() {
            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
            markDirty();
        }
    };

    public TerraVaporCollectorTile() {
        super(ModBlocks.TERRA_VAPOR_COLLECTOR_TILE);
        this.block = null;
    }

    public FluidTank getTank() {
        return tank;
    }

    private IVaporStorage createVapor() {
        return new CustomVaporStorage(maxVaporCapacity, maxVaporTransfer);
    }

    private IFluidHandler createFluid() {
            return new FluidTank(10000) {
            @Override
            protected void onContentsChanged() {
                BlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                markDirty();
            }
        };
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
                //val lazyCapa = heldItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                //tank.fill(new FluidStack(Fluids.LAVA.getStillFluid(), 50), IFluidHandler.FluidAction.EXECUTE);
                markDirty();
/*                // going from 1 to 0, will reset next, do operation here

                // Collect Gas here
                // get block positions.  Only check every 20 ticks for performance reasons
                surroundingCausticBells = getSurroundingCausticBells();

                for (CausticBellTile bell : surroundingCausticBells) {

                    int strength = bell.getStrength();
                    int yield = bell.getYield(); // mB
                    int purity = bell.getPurity();
                    int toAdd = (strength * yield) * purity;

                    vaporStorage.ifPresent(w -> ((CustomVaporStorage) w).addVapor(toAdd));
                }*/
            }

        }

        if (counter <= 0) {
            // Start of the operation

            counter = 100;
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


    /*    CompoundNBT energyTag = tag.getCompound("vapor");
        // Save energy when block is broken
        vaporStorage.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));*/
        super.read(tag);

        tank.readFromNBT(tag.getCompound("tank"));
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        // Write energy when block is placed
/*        vaporStorage.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("vapor", compound);
        });*/

        CompoundNBT tankNBT = new CompoundNBT();
        tank.writeToNBT(tankNBT);
        tag.put("tank", tankNBT);

        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT nbtTag = super.getUpdateTag();
        CompoundNBT tankNBT = new CompoundNBT();
        tank.writeToNBT(tankNBT);
        nbtTag.put("tank", tankNBT);
        return nbtTag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        tank.readFromNBT(packet.getNbtCompound().getCompound("tank"));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

/*        if (cap == CapabilityVapor.VAPOR) {
            return vaporStorage.cast();
        }*/

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T) tank);
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
