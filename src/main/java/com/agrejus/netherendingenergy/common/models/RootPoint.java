package com.agrejus.netherendingenergy.common.models;

import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class RootPoint implements INBTSerializable<CompoundNBT> {
    private BlockPos position;
    private RootBud offshootBud;

    public RootPoint() {}

    public RootPoint(BlockPos position) {
        this.position = position;
    }

    public RootBud getOffshootBud() {
        return this.offshootBud;
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public void setOffshootBud(RootBud offshootBud) {
        this.offshootBud = offshootBud;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();

        CompoundNBT positionNbt = new CompoundNBT();
        NBTHelpers.writeToNBT(positionNbt, this.position);
        tag.put("position", positionNbt);

        if (this.offshootBud != null) {
            CompoundNBT offshootBud = this.offshootBud.serializeNBT();
            tag.put("offshoot_bud", offshootBud);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {

        CompoundNBT positionNbt = (CompoundNBT)nbt.get("position");
        this.position = NBTHelpers.readBlockPosFromNBT(positionNbt);

        if (nbt.contains("offshoot_bud")) {
            this.offshootBud = new RootBud();

            CompoundNBT budNbt = (CompoundNBT)nbt.get("offshoot_bud");
            this.offshootBud.deserializeNBT(budNbt);
        }
    }
}
