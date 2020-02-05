package com.agrejus.netherendingenergy.common.helpers;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.*;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;

public class ClientUtils {
    public static final AxisAlignedBB standardBlockAABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    static HashMap<String, ResourceLocation> resourceMap = new HashMap<String, ResourceLocation>();
    public static TextureAtlasSprite[] destroyBlockIcons = new TextureAtlasSprite[10];


    public static void handleGuiTank(IFluidTank tank, int x, int y, int w, int h, int oX, int oY, int oW, int oH, int mX, int mY, String originalTexture, List<ITextComponent> tooltip) {
        handleGuiTank(tank.getFluid(), tank.getCapacity(), x, y, w, h, oX, oY, oW, oH, mX, mY, originalTexture, tooltip);
    }

    public static void handleGuiTank(FluidStack fluid, int capacity, int x, int y, int w, int h, int oX, int oY, int oW, int oH, int mX, int mY, String originalTexture, List<ITextComponent> tooltip) {
        if (tooltip == null) {
            if (fluid != null && fluid.getFluid() != null) {
                int fluidHeight = (int) (h * (fluid.getAmount() / (float) capacity));
                drawRepeatedFluidSprite(fluid, x, y + h - fluidHeight, w, fluidHeight);
                bindTexture(originalTexture);
                GlStateManager.color3f(1, 1, 1);
            }
            int xOff = (w - oW) / 2;
            int yOff = (h - oH) / 2;
            drawTexturedRect(x + xOff, y + yOff, oW, oH, 256f, oX, oX + oW, oY, oY + oH);
        } else {
            if (mX >= x && mX < x + w && mY >= y && mY < y + h)
                addFluidTooltip(fluid, tooltip, capacity);
        }
    }

    public static void addFluidTooltip(FluidStack fluid, List<ITextComponent> tooltip, int tankCapacity) {
        if (!fluid.isEmpty())
            tooltip.add(fluid.getDisplayName().setStyle(
                    new Style().setColor(fluid.getFluid().getAttributes().getRarity(fluid).color)));
        else
            tooltip.add(new TranslationTextComponent("gui.immersiveengineering.empty"));

/*        if(fluid.getFluid() instanceof IEFluid)
            ((IEFluid)fluid.getFluid()).addTooltipInfo(fluid, null, tooltip);*/

        if (mc().gameSettings.advancedItemTooltips && !fluid.isEmpty()) {
            if (!Screen.hasShiftDown())
                tooltip.add(new TranslationTextComponent("holdShiftForInfo"));
            else {
                Style darkGray = new Style().setColor(TextFormatting.DARK_GRAY);
                //TODO translation keys
                tooltip.add(new StringTextComponent("Fluid Registry: " + fluid.getFluid().getRegistryName()).setStyle(darkGray));
                tooltip.add(new StringTextComponent("Density: " + fluid.getFluid().getAttributes().getDensity(fluid)).setStyle(darkGray));
                tooltip.add(new StringTextComponent("Temperature: " + fluid.getFluid().getAttributes().getTemperature(fluid)).setStyle(darkGray));
                tooltip.add(new StringTextComponent("Viscosity: " + fluid.getFluid().getAttributes().getViscosity(fluid)).setStyle(darkGray));
                tooltip.add(new StringTextComponent("NBT Data: " + fluid.getTag()).setStyle(darkGray));
            }
        }

        Style gray = new Style().setColor(TextFormatting.GRAY);
        if (tankCapacity > 0)
            tooltip.add(new StringTextComponent(fluid.getAmount() + "/" + tankCapacity + "mB").setStyle(gray));
        else
            tooltip.add(new StringTextComponent(fluid.getAmount() + "mB").setStyle(gray));
    }


    public static void drawRepeatedFluidSprite(FluidStack fluid, float x, float y, float w, float h) {
        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE.toString());
        TextureAtlasSprite sprite = getSprite(fluid.getFluid().getAttributes().getStill(fluid));
        int col = fluid.getFluid().getAttributes().getColor(fluid);
        GlStateManager.color3f((col >> 16 & 255) / 255.0f, (col >> 8 & 255) / 255.0f, (col & 255) / 255.0f);
        int iW = sprite.getWidth();
        int iH = sprite.getHeight();
        if (iW > 0 && iH > 0)
            drawRepeatedSprite(x, y, w, h, iW, iH, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
    }

    public static Minecraft mc() {
        return Minecraft.getInstance();
    }

    public static void bindTexture(String path) {
        mc().getTextureManager().bindTexture(getResource(path));
    }

    public static ResourceLocation getResource(String path) {
        ResourceLocation rl = resourceMap.containsKey(path) ? resourceMap.get(path) : new ResourceLocation(path);
        if (!resourceMap.containsKey(path))
            resourceMap.put(path, rl);
        return rl;
    }

    public static void drawTexturedRect(int x, int y, int w, int h, float picSize, int... uv) {
        double[] d_uv = new double[]{uv[0] / picSize, uv[1] / picSize, uv[2] / picSize, uv[3] / picSize};
        drawTexturedRect(x, y, w, h, d_uv);
    }

    public static void drawTexturedRect(float x, float y, float w, float h, double... uv) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x, y + h, 0).tex(uv[0], uv[3]).endVertex();
        worldrenderer.pos(x + w, y + h, 0).tex(uv[1], uv[3]).endVertex();
        worldrenderer.pos(x + w, y, 0).tex(uv[1], uv[2]).endVertex();
        worldrenderer.pos(x, y, 0).tex(uv[0], uv[2]).endVertex();
        tessellator.draw();
    }

    public static TextureAtlasSprite getSprite(ResourceLocation rl) {
        return mc().getTextureMap().getSprite(rl);
    }

    public static void drawRepeatedSprite(float x, float y, float w, float h, int iconWidth, int iconHeight, float uMin, float uMax, float vMin, float vMax) {
        int iterMaxW = (int) (w / iconWidth);
        int iterMaxH = (int) (h / iconHeight);
        float leftoverW = w % iconWidth;
        float leftoverH = h % iconHeight;
        float leftoverWf = leftoverW / (float) iconWidth;
        float leftoverHf = leftoverH / (float) iconHeight;
        float iconUDif = uMax - uMin;
        float iconVDif = vMax - vMin;
        for (int ww = 0; ww < iterMaxW; ww++) {
            for (int hh = 0; hh < iterMaxH; hh++)
                drawTexturedRect(x + ww * iconWidth, y + hh * iconHeight, iconWidth, iconHeight, uMin, uMax, vMin, vMax);
            drawTexturedRect(x + ww * iconWidth, y + iterMaxH * iconHeight, iconWidth, leftoverH, uMin, uMax, vMin, (vMin + iconVDif * leftoverHf));
        }
        if (leftoverW > 0) {
            for (int hh = 0; hh < iterMaxH; hh++)
                drawTexturedRect(x + iterMaxW * iconWidth, y + hh * iconHeight, leftoverW, iconHeight, uMin, (uMin + iconUDif * leftoverWf), vMin, vMax);
            drawTexturedRect(x + iterMaxW * iconWidth, y + iterMaxH * iconHeight, leftoverW, leftoverH, uMin, (uMin + iconUDif * leftoverWf), vMin, (vMin + iconVDif * leftoverHf));
        }
    }
}
