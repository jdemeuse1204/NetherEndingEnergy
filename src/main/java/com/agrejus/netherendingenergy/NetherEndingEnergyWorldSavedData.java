package com.agrejus.netherendingenergy;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

public class NetherEndingEnergyWorldSavedData extends WorldSavedData {

    private static final String DATA_NAME = String.format("%s_%s", NetherEndingEnergy.MODID, "wireless");
    private List<String> wirelessTransferModuleChannels;

    public NetherEndingEnergyWorldSavedData() {
        super(DATA_NAME);
        wirelessTransferModuleChannels = new ArrayList<>();
    }

    public NetherEndingEnergyWorldSavedData(String s) {
        super(s);
        wirelessTransferModuleChannels = new ArrayList<>();
    }

    public List<String> getWirelessTransferModuleChannels() {
        return wirelessTransferModuleChannels;
    }

    @Override
    public void read(CompoundNBT nbt) {

        for (int i = 0; i < 10000; i++) {
            String key = "wireless_transfer_module_channel" + i;
            if (nbt.contains(key)== false) {
                break;
            }
            String channel = nbt.getString(key);
            wirelessTransferModuleChannels.add(channel);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {

        int size = wirelessTransferModuleChannels.size();

        for (int i = 0; i < size; i++) {
            String key = "wireless_transfer_module_channel" + i;
            String channel = wirelessTransferModuleChannels.get(i);
            compound.putString(key, channel);
        }

        return compound;
    }

    public static NetherEndingEnergyWorldSavedData get(World world) {

        if (!(world instanceof ServerWorld)) {
            return new NetherEndingEnergyWorldSavedData();
        }

        DimensionSavedDataManager storage = ((ServerWorld) world).getSavedData();
        return storage.getOrCreate(NetherEndingEnergyWorldSavedData::new, DATA_NAME);
    }
}
