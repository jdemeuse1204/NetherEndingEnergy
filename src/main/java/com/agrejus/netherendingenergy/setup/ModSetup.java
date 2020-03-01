package com.agrejus.netherendingenergy.setup;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModSetup {

    public static ItemGroup itemGroup = new ItemGroup(NetherEndingEnergy.MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.CAUSTIC_BELL_BLOCK);
        }
    };

    public void init() {

    }
}
