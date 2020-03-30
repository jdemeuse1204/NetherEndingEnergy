package com.agrejus.netherendingenergy.common.helpers;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

public class NBTHelpers {
    public static <T> CompoundNBT serializeNBT(T value) {
        return ((INBTSerializable<CompoundNBT>)value).serializeNBT();
    }

    public static <T> void serializeNBTAndPut(T value, CompoundNBT tag, String key) {
        CompoundNBT nbt = ((INBTSerializable<CompoundNBT>)value).serializeNBT();

        tag.put(key, nbt);
    }

    public static <T> void deserializeNBT(T value, CompoundNBT tag, String key) {
        CompoundNBT nbtTag = tag.getCompound(key);

        ((INBTSerializable<CompoundNBT>) value).deserializeNBT(nbtTag);
    }

    public static void putNBTInt(int value, CompoundNBT tag, String key) {
        tag.putInt(key, value);
    }

    public static int getNBTInt(CompoundNBT tag, String key) {
        return tag.getInt(key);
    }

    public static void writeToNBT(CompoundNBT tag, BlockPos pos) {
        tag.putInt("X", pos.getX());
        tag.putInt("Y", pos.getY());
        tag.putInt("Z", pos.getZ());
    }
}
