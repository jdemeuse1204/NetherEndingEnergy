package com.agrejus.netherendingenergy.client.gui.button;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ToggleButton {

    private final NEEImageButton activeButton;
    private final NEEImageButton inactiveButton;

    public ToggleButton(NEEImageButton activeButton, NEEImageButton inactiveButton) {
        this.activeButton = activeButton;
        this.inactiveButton = inactiveButton;
    }

    public void setVisibility(boolean activeButtonVisibility, boolean inactiveButtonVisibility) {
        this.activeButton.setVisibility(activeButtonVisibility);
        this.inactiveButton.setVisibility(inactiveButtonVisibility);
    }
}
