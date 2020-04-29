package com.agrejus.netherendingenergy.common.screen;

import com.agrejus.netherendingenergy.common.rendering.Rect;
import com.agrejus.netherendingenergy.common.rendering.RectProgression;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.antlr.v4.runtime.misc.Interval;
import org.lwjgl.opengl.GL11;

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

    protected void drawShadowCenteredOverlayString(FontRenderer renderer, String text, int left, int top, int color) {

        String s = text == null ? String.valueOf(1) : text;
        GlStateManager.disableLighting();
        GlStateManager.disableDepthTest();
        GlStateManager.disableBlend();
        renderer.drawStringWithShadow(s, (float) (left + 19 - 2 - renderer.getStringWidth(s)), (float) (top + 6 + 3), color);
        GlStateManager.enableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableDepthTest();

        GlStateManager.enableBlend();
    }

    protected void drawNoShadowString(FontRenderer renderer, String text, int left, int top, int color) {
        renderer.drawString(text, (float) left, (float) top, color);
    }

    protected void drawNoShadowCenteredString(FontRenderer renderer, String text, int left, int top, int color) {
        renderer.drawString(text, (float) (left - renderer.getStringWidth(text) / 2), (float) top, color);
    }

    public static void drawGradientRect(int x0, int y0, int x1, int y1, int color0, int color1) {
        float alpha0 = (color0 >> 24 & 255) / 255.0F;
        float blue0 = (color0 >> 16 & 255) / 255.0F;
        float green0 = (color0 >> 8 & 255) / 255.0F;
        float red0 = (color0 & 255) / 255.0F;
        float alpha1 = (color1 >> 24 & 255) / 255.0F;
        float blue1 = (color1 >> 16 & 255) / 255.0F;
        float green1 = (color1 >> 8 & 255) / 255.0F;
        float red1 = (color1 & 255) / 255.0F;
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(x1, y0, 0).color(blue0, green0, red0, alpha0).endVertex();
        worldrenderer.pos(x0, y0, 0).color(blue0, green0, red0, alpha0).endVertex();
        worldrenderer.pos(x0, y1, 0).color(blue1, green1, red1, alpha1).endVertex();
        worldrenderer.pos(x1, y1, 0).color(blue1, green1, red1, alpha1).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableTexture();
    }

    protected String getEnergyPerTick(int amount) {
        if (amount > 0) {
            return String.format(TextFormatting.GREEN + "%s RF/t", amount);
        }

        if (amount < 0) {
            return String.format(TextFormatting.RED + "%s RF/t", amount);
        }

        return String.format("%s RF/t", amount);
    }

    public void drawColoredRect(int x, int y, int w, int h, int color) {
        GlStateManager.disableTexture();
        GlStateManager.enableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

        int red = color >> 16 & 255;
        int green = color >> 8 & 255;
        int blue = color & 255;
        int alpha = color >> 24 & 255;

        buffer.pos(x, y + h, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + w, y + h, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x + w, y, 0).color(red, green, blue, alpha).endVertex();
        buffer.pos(x, y, 0).color(red, green, blue, alpha).endVertex();

        tessellator.draw();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableTexture();
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
    protected void drawVerticalSliceWithProgressionUp(Rect destination, RectProgression sliceLocation) {
        this.blit(destination.getLeft(),
                destination.getTop() + sliceLocation.getHeight() - sliceLocation.getProgression(),
                sliceLocation.getLeft(),
                sliceLocation.getBottom() - sliceLocation.getProgression(),
                sliceLocation.getWidth(),
                sliceLocation.getProgression());
    }

    public void drawVerticalSliceWithProgressionRight(int amount, int total, RectProgression slice, Rect destination) {
        float energyPercent = this.getProgressionPercent(amount, total);
        int energyProgression = slice.getWidth() - Math.round(slice.getWidth() * energyPercent);
        slice.setProgression(energyProgression);
        this.drawVerticalSliceWithProgressionRight(destination, slice);
    }

    protected void drawVerticalSliceWithProgressionRight(Rect destination, RectProgression sliceLocation) {
        int destinationLeft = destination.getLeft();
        int destinationTop = destination.getTop();
        int sliceLeft = sliceLocation.getLeft();
        int sliceTop = sliceLocation.getTop();
        int sliceWidth = sliceLocation.getWidth() - sliceLocation.getProgression();
        int sliceHeight = sliceLocation.getHeight();

        this.blit(destinationLeft,
                destinationTop,
                sliceLeft,
                sliceTop,
                sliceWidth,
                sliceHeight);
    }

    protected void drawVerticalSliceWithProgressionDown(Rect destination, RectProgression sliceLocation) {
        int destinationLeft = destination.getLeft();
        int destinationTop = destination.getTop();
        int sliceLeft = sliceLocation.getLeft();
        int sliceTop = sliceLocation.getTop();
        int sliceWidth = sliceLocation.getWidth();
        int sliceHeight = sliceLocation.getProgression();

        this.blit(destinationLeft,
                destinationTop,
                sliceLeft,
                sliceTop,
                sliceWidth,
                sliceHeight);
    }

    public void drawVerticalSliceWithProgressionUp(int amount, int total, RectProgression slice, Rect destination) {
        float energyPercent = this.getProgressionPercent(amount, total);
        int energyProgression = Math.round(slice.getHeight() * energyPercent);
        slice.setProgression(energyProgression);
        this.drawVerticalSliceWithProgressionUp(destination, slice);
    }

    public void drawVerticalSliceWithProgressionDown(int amount, int total, RectProgression slice, Rect destination) {
        float percent = this.getProgressionPercent(amount, total);
        int progression = Math.round(slice.getHeight() * percent);
        slice.setProgression(progression);
        this.drawVerticalSliceWithProgressionDown(destination, slice);
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

    protected void drawTankVertical(int fluidAmount, int tankCapacity, int color, Rect location) {

        if (fluidAmount > 0) {
            int inputTankCapacity = tankCapacity;
            int inputFillHeight = location.getComputedFillHeight(inputTankCapacity, fluidAmount);

            int totalHeight = location.getTop() + location.getHeight();
            int h = totalHeight - inputFillHeight;
            int y = totalHeight - h;
            drawColoredRect(location.getLeft(), y, location.getWidth(), h, color);
        }
    }

    protected void innerBlitOverlay(int p_blit_1_, int p_blit_2_, int p_blit_3_, int p_blit_4_, int p_blit_5_, int p_blit_6_) {
        innerBlitOverlay(p_blit_1_, p_blit_2_, this.blitOffset, (float) p_blit_3_, (float) p_blit_4_, p_blit_5_, p_blit_6_, 256, 256);
    }

    protected void innerBlitOverlay(int p_blit_0_, int p_blit_1_, int p_blit_2_, float p_blit_3_, float p_blit_4_, int p_blit_5_, int p_blit_6_, int p_blit_7_, int p_blit_8_) {
        innerBlitOverlay(p_blit_0_, p_blit_0_ + p_blit_5_, p_blit_1_, p_blit_1_ + p_blit_6_, p_blit_2_, p_blit_5_, p_blit_6_, p_blit_3_, p_blit_4_, p_blit_8_, p_blit_7_);
    }

    protected void innerBlitOverlay(int p_innerBlit_0_, int p_innerBlit_1_, int p_innerBlit_2_, int p_innerBlit_3_, int p_innerBlit_4_, int p_innerBlit_5_, int p_innerBlit_6_, float p_innerBlit_7_, float p_innerBlit_8_, int p_innerBlit_9_, int p_innerBlit_10_) {
        innerBlitOverlay(p_innerBlit_0_, p_innerBlit_1_, p_innerBlit_2_, p_innerBlit_3_, p_innerBlit_4_, (p_innerBlit_7_ + 0.0F) / (float) p_innerBlit_9_, (p_innerBlit_7_ + (float) p_innerBlit_5_) / (float) p_innerBlit_9_, (p_innerBlit_8_ + 0.0F) / (float) p_innerBlit_10_, (p_innerBlit_8_ + (float) p_innerBlit_6_) / (float) p_innerBlit_10_);
    }

    protected void innerBlitOverlay(int p_innerBlit_0_, int p_innerBlit_1_, int p_innerBlit_2_, int p_innerBlit_3_, int p_innerBlit_4_, float p_innerBlit_5_, float p_innerBlit_6_, float p_innerBlit_7_, float p_innerBlit_8_) {

        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableDepthTest();
        GlStateManager.translatef(0, 0, 900);
        this.blitOffset = 900;
        this.itemRenderer.zLevel = 900;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) p_innerBlit_0_, (double) p_innerBlit_3_, (double) p_innerBlit_4_).tex((double) p_innerBlit_5_, (double) p_innerBlit_8_).endVertex();
        bufferbuilder.pos((double) p_innerBlit_1_, (double) p_innerBlit_3_, (double) p_innerBlit_4_).tex((double) p_innerBlit_6_, (double) p_innerBlit_8_).endVertex();
        bufferbuilder.pos((double) p_innerBlit_1_, (double) p_innerBlit_2_, (double) p_innerBlit_4_).tex((double) p_innerBlit_6_, (double) p_innerBlit_7_).endVertex();
        bufferbuilder.pos((double) p_innerBlit_0_, (double) p_innerBlit_2_, (double) p_innerBlit_4_).tex((double) p_innerBlit_5_, (double) p_innerBlit_7_).endVertex();
        tessellator.draw();

        this.blitOffset = 0;
        this.itemRenderer.zLevel = 0;
        GlStateManager.translatef(0, 0, -300);
        GlStateManager.enableLighting();
        GlStateManager.disableDepthTest();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableRescaleNormal();
    }

/*    RenderHelper.setBlockTextureSheet();
        RenderHelper.resetColor();
    blit(x, y, blitOffset, 16, 16, icon);*/
}
