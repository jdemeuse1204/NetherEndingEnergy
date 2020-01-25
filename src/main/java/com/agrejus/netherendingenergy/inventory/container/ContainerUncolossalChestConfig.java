package com.agrejus.netherendingenergy.inventory.container;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerUncolossalChestConfig extends GuiConfig<ContainerUncolossalChest> {

    public ContainerUncolossalChestConfig() {
        super(ColossalChests._instance,
                "uncolossal_chest",
                eConfig -> new ContainerType<>(ContainerUncolossalChest::new));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public <U extends Screen & IHasContainer<ContainerUncolossalChest>> ScreenManager.IScreenFactory<ContainerUncolossalChest, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenUncolossalChest::new);
    }

}
