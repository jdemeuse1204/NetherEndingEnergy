package com.agrejus.netherendingenergy.tools;

import com.agrejus.netherendingenergy.common.interfaces.IVaporStorage;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityVapor {

    @CapabilityInject(IVaporStorage.class)
    public static Capability<IVaporStorage> VAPOR = null;

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IVaporStorage.class, new Capability.IStorage<IVaporStorage>()
                {
                    @Override
                    public INBT writeNBT(Capability<IVaporStorage> capability, IVaporStorage instance, Direction side)
                    {
                        return new IntNBT(instance.getVaporStored());
                    }

                    @Override
                    public void readNBT(Capability<IVaporStorage> capability, IVaporStorage instance, Direction side, INBT nbt)
                    {
                        if (!(instance instanceof VaporStorage))
                            throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
                        ((VaporStorage)instance).vapor = ((IntNBT)nbt).getInt();
                    }
                },
                () -> new VaporStorage(1000));
    }
}
