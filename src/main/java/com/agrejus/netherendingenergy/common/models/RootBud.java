package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.enumeration.RootType;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class RootBud {

    private RootType rootType;
    private final Direction growthDirection;
    private IRoot root;
    private final Direction sideOfTrunkDirection;
    private final BlockPos location;

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

    public void writeAllButRootToNBT(CompoundNBT tag) {

        tag.putString("RootType", rootType.toString());
        tag.putString("GrowthDirection", growthDirection.toString());

        if (sideOfTrunkDirection != null) {
            tag.putString("SideOfTrunkDirection", sideOfTrunkDirection.toString());
        }

        CompoundNBT locationNBT = new CompoundNBT();
        NBTHelpers.writeToNBT(locationNBT, location);
        tag.put("Location", locationNBT);
    }
}
