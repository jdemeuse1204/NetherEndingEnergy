package com.agrejus.netherendingenergy;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {
    @CapabilityInject(IInventoryState.class)
    public static Capability<IInventoryState> INVENTORY_STATE = null;
    @CapabilityInject(ISlotlessItemHandler.class)
    public static Capability<ISlotlessItemHandler> SLOTLESS_ITEMHANDLER = null;
}
