package com.agrejus.netherendingenergy.client.gui.button;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class NEEImageButton extends ImageButton {
    private boolean isVisible;
    private int savedHeight;

    public NEEImageButton(boolean isVisible, int p_i51134_1_, int p_i51134_2_, int p_i51134_3_, int p_i51134_4_, int p_i51134_5_, int p_i51134_6_, int p_i51134_7_, ResourceLocation p_i51134_8_, Button.IPressable p_i51134_9_) {
        super(p_i51134_1_, p_i51134_2_, p_i51134_3_, p_i51134_4_, p_i51134_5_, p_i51134_6_, p_i51134_7_, p_i51134_8_, 256, 256, p_i51134_9_);
        this.setVisibility(isVisible);
    }

    public NEEImageButton(boolean isVisible, int p_i51135_1_, int p_i51135_2_, int p_i51135_3_, int p_i51135_4_, int p_i51135_5_, int p_i51135_6_, int p_i51135_7_, ResourceLocation p_i51135_8_, int p_i51135_9_, int p_i51135_10_, Button.IPressable p_i51135_11_) {
        super(p_i51135_1_, p_i51135_2_, p_i51135_3_, p_i51135_4_, p_i51135_5_, p_i51135_6_, p_i51135_7_, p_i51135_8_, p_i51135_9_, p_i51135_10_, p_i51135_11_, "");
        this.setVisibility(isVisible);
    }

    public NEEImageButton(boolean isVisible, int p_i51136_1_, int p_i51136_2_, int p_i51136_3_, int p_i51136_4_, int p_i51136_5_, int p_i51136_6_, int p_i51136_7_, ResourceLocation p_i51136_8_, int p_i51136_9_, int p_i51136_10_, Button.IPressable p_i51136_11_, String p_i51136_12_) {
        super(p_i51136_1_, p_i51136_2_, p_i51136_3_, p_i51136_4_, p_i51136_5_, p_i51136_6_, p_i51136_7_, p_i51136_8_, p_i51136_9_, p_i51136_10_, p_i51136_11_, p_i51136_12_);
        this.setVisibility(isVisible);
    }

    public void setVisibility(boolean isVisible) {
        this.isVisible = isVisible;

        if (isVisible == false) {
            this.savedHeight = height;
            this.setHeight(0);
            return;
        }

        if (this.height == 0 && this.savedHeight != 0) {
            this.setHeight(this.savedHeight);
        }
    }

    public boolean getVisibility() {
        return this.isVisible;
    }

    @Override
    public void renderButton(int x, int y, float mouseButton) {
        if (this.isVisible) {
            super.renderButton(x, y, mouseButton);
        }
    }
}
