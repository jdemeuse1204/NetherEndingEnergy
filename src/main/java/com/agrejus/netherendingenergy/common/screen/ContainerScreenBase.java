package com.agrejus.netherendingenergy.common.screen;

import com.agrejus.netherendingenergy.common.rendering.Rect;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class ContainerScreenBase<T extends Container> extends ContainerScreen<T> {

    protected final int defaultGuiScreenHeight;
    protected final int defaultGuiScreenWidth;

    public ContainerScreenBase(T screenContainer, PlayerInventory inv, ITextComponent titleIn, int defaultGuiScreenWidth, int defaultGuiScreenHeight) {
        super(screenContainer, inv, titleIn);
        this.defaultGuiScreenHeight = defaultGuiScreenHeight;
        this.defaultGuiScreenWidth = defaultGuiScreenWidth;
    }

    protected void drawOverlaySlice(Rect origin, Rect destination) {
        this.blit(destination.getLeft(), destination.getTop(), origin.getLeft(), origin.getTop(), origin.getRight(), origin.getBottom());
    }

    protected void fillVertical(Rect rect, int top, int color) {
        fill(rect.getLeft(), top, rect.getRight(), rect.getBottom(), color);
    }

    protected Rect createSliceRect(int top, int left, int right, int bottom) {
        return new Rect(top, left, right, bottom);
    }

    protected Rect createRectBasedOnGui(int top, int left, int right, int bottom) {
        return new Rect(guiTop + top, guiLeft + left, guiLeft + right, guiTop + bottom);
    }

    protected Rect createSliceDrawDestination(int top, int left) {
        return new Rect(guiTop + top, guiLeft + left, 0, 0);
    }

    protected boolean isMouseOver(Rect rect, int mouseX, int mouseY) {
        return mouseX >= (rect.getLeft()) &&
                mouseX <= (rect.getRight()) &&
                mouseY >= (rect.getTop()) &&
                mouseY <= (rect.getBottom());
    }

/*    RenderHelper.setBlockTextureSheet();
        RenderHelper.resetColor();
    blit(x, y, blitOffset, 16, 16, icon);*/
}
