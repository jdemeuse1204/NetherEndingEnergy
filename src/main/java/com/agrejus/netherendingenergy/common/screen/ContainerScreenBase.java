package com.agrejus.netherendingenergy.common.screen;

import com.agrejus.netherendingenergy.common.rendering.Rect;
import com.agrejus.netherendingenergy.common.rendering.RectProgression;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.antlr.v4.runtime.misc.Interval;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Period;

@OnlyIn(Dist.CLIENT)
public abstract class ContainerScreenBase<T extends Container> extends ContainerScreen<T> {

    protected final int guiDefaultFontColor = 0xff404040;
    protected final int defaultGuiScreenHeight;
    protected final int defaultGuiScreenWidth;

    public ContainerScreenBase(T screenContainer, PlayerInventory inv, ITextComponent titleIn, int defaultGuiScreenWidth, int defaultGuiScreenHeight) {
        super(screenContainer, inv, titleIn);
        this.defaultGuiScreenHeight = defaultGuiScreenHeight;
        this.defaultGuiScreenWidth = defaultGuiScreenWidth;
    }

    protected float getProgressionPercent(int current, int total) {
        if (total == 0) {
            return 0f;
        }

        return (float) current / (float) total;
    }
//GlStateManager.translatef(0.0F, 0.0F, 32.0F);
//      this.blitOffset = 200;
//      this.itemRenderer.zLevel = 200.0F;
//      net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
//      if (font == null) font = this.font;
//      this.itemRenderer.renderItemAndEffectIntoGUI(stack, x, y);
//      this.itemRenderer.renderItemOverlayIntoGUI(font, stack, x, y - (this.draggedStack.isEmpty() ? 0 : 8), altText);
//      this.blitOffset = 0;
//      this.itemRenderer.zLevel = 0.0F;

    protected void drawShadowCenteredOverlayString(FontRenderer renderer, String text, int left, int top, int color) {

        String s = text == null ? String.valueOf(1) : text;
        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        GlStateManager.disableBlend();
        renderer.drawStringWithShadow(s, (float)(left + 19 - 2 - renderer.getStringWidth(s)), (float)(top + 6 + 3), color);
        GlStateManager.enableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepthTest();
        // Fixes opaque cooldown overlay a bit lower
        // TODO: check if enabled blending still screws things up down the line.
        GlStateManager.enableBlend();
    }
    protected void drawNoShadowString(FontRenderer renderer, String text, int left, int top, int color) {
        renderer.drawString(text, (float) left, (float) top, color);
    }

    protected void drawNoShadowCenteredString(FontRenderer renderer, String text, int left, int top, int color) {
        renderer.drawString(text, (float) (left - renderer.getStringWidth(text) / 2), (float) top, color);
    }

    protected String getEnergyDisplayAmount(int amount) {

        if (amount >= 1000000) {
            float aggregatedAmount = (float) amount / 100000;
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            return String.format("%sM", format.format(aggregatedAmount));
        }

        if (amount >= 10000) {
            float aggregatedAmount = (float) amount / 1000;
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(0);
            return String.format("%sK", format.format(aggregatedAmount));
        }

        return String.format("%s", amount);
    }

    protected String getDisplayTimeFromTicks(int ticks) {
        long seconds = ticks / 20;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        if (hours > 0) {
            return String.format("%s:%s:%s", String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds));
        }

        if (minutes > 0) {
            return String.format("%s:%s", String.format("%02d", minutes), String.format("%02d", seconds % 60));
        }

        return String.format("%s:%s", String.format("%02d", minutes), String.format("%02d", seconds));
    }

    protected String getBucketDisplayAmount(int amount) {

        if (amount >= 10000) {
            float aggregatedAmount = (float) amount / 10;
            DecimalFormat format = new DecimalFormat();
            format.setMaximumFractionDigits(1);
            return String.format("%s B", format.format(aggregatedAmount));
        }

        return String.format("%s mB", amount);
    }

    // From Furnace
    // k = 13
    // 36 = top of the destination
    // 12 = bottom of the slice
    // 14 = width of the slice
    // 56 = left edge of the destination
    // 176 = screen width
    // k = return this.field_217064_e.get(0) * 13 / i;
    // this.blit(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
    protected void drawVerticalSliceWithProgression(Rect destination, RectProgression sliceLocation) {
        this.blit(destination.getLeft(),
                destination.getTop() + sliceLocation.getHeight() - sliceLocation.getProgression(),
                sliceLocation.getLeft(),
                sliceLocation.getBottom() - sliceLocation.getProgression(),
                sliceLocation.getWidth(),
                sliceLocation.getProgression());
    }

    protected void drawOverlaySlice(Rect origin, Rect destination) {
        this.blit(destination.getLeft(), destination.getTop(), origin.getLeft(), origin.getTop(), origin.getRight(), origin.getBottom());
    }

    protected void fillVertical(Rect rect, int top, int color) {
        fill(rect.getLeft(), top, rect.getRight(), rect.getBottom(), color);
    }

    protected RectProgression createProgressionSliceRect(int top, int left, int right, int bottom) {
        return new RectProgression(top, left, right, bottom);
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
