package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.enumeration.RootType;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class RootBud implements INBTSerializable<CompoundNBT> {

    private RootType rootType;
    private Direction growthDirection;
    private IRoot root;
    private Direction sideOfTrunkDirection;
    private BlockPos location;

    public RootBud() {

    }

    public RootBud(Direction growthDirection, BlockPos location, RootType rootType, @Nullable Direction sideOfTrunkDirection) {
        this.growthDirection = growthDirection;
        this.rootType = rootType;
        this.sideOfTrunkDirection = sideOfTrunkDirection;
        this.location = location;
    }

    public RootType getRootType() {
        return this.rootType;
    }

    public IRoot getRoot() {
        return this.root;
    }

    public void setRoot(IRoot root) {
        this.root = root;
    }

    public Direction getGrowthDirection() {
        return this.growthDirection;
    }

    public Direction getSideOfTrunkDirection() {
        return sideOfTrunkDirection;
    }

    public BlockPos getLocation() {
        return this.location;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        tag.putString("root_type", rootType.getName());
        tag.putString("growth_direction", growthDirection.getName());

        if (sideOfTrunkDirection != null) {
            tag.putString("side_of_trunk_direction", sideOfTrunkDirection.getName());
        }

        CompoundNBT locationNBT = new CompoundNBT();
        NBTHelpers.writeToNBT(locationNBT, location);
        tag.put("location", locationNBT);

        if (root != null) {
            tag.put("root", this.root.serializeNBT());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

        CompoundNBT locationNbt = (CompoundNBT)nbt.get("location");
        this.location = NBTHelpers.readBlockPosFromNBT(locationNbt);
        this.rootType = RootType.get(nbt.getString("root_type"));
        this.growthDirection = Direction.byName(nbt.getString("growth_direction"));

        if (nbt.contains("side_of_trunk_direction")) {
            this.sideOfTrunkDirection = Direction.byName(nbt.getString("side_of_trunk_direction"));
        }

        if (nbt.contains("root")) {
            CompoundNBT rootNbt = (CompoundNBT)nbt.get("root");

            if (this.rootType == RootType.MAIN_TRUNK) {
                this.root = new MainTrunkRoot();
            } else {
                this.root = new OffshootRoot();
            }

            this.root.deserializeNBT(rootNbt);
        }
    }
}
