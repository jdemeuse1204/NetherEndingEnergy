package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.common.helpers.ResourceHelpers;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class TerraAcidCollectorTileEntityRenderer extends TileEntityRenderer<TerraAcidCollectorTile> {
    public static final float TANK_THICKNESS = 0.06f;
    private static final float TANK_MAX_HEIGHT = .58f;

    public TerraAcidCollectorTileEntityRenderer() {
    }

    @Override
    public void render(TerraAcidCollectorTile tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.color4f(1, 1, 1, 1);
        GlStateManager.disableBlend();
        GlStateManager.translatef((float) x, (float) y, (float) z);

        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        render(tileEntity);

        GlStateManager.popMatrix();
    }

    private void render(TerraAcidCollectorTile collector) {
        renderGrowthMedium(collector);
        renderIntakeFluid(collector);
        renderOutputFluid(collector);
    }

    private void renderGrowthMedium(TerraAcidCollectorTile collector) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();

        Block growthMedium = collector.getGrowthMediumBlock();

        if (growthMedium == null) {
            return;
        }

        String iconLocation = ResourceHelpers.resolveBlockPath(growthMedium);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(iconLocation);

        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

        GlStateManager.color4f(1, 1, 1, .5f);
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();
        float u2 = sprite.getMaxU();
        float v2 = sprite.getMaxV();

        float value = .375f;

        // Top - x,y,z
        renderer.pos(value, .99, value).tex(u1, v1).color(255, 255, 255, 128).endVertex();
        renderer.pos(value, .99, 1 - value).tex(u1, v2).color(255, 255, 255, 128).endVertex();
        renderer.pos(1 - value, .99, 1 - value).tex(u2, v2).color(255, 255, 255, 128).endVertex();
        renderer.pos(1 - value, .99, value).tex(u2, v1).color(255, 255, 255, 128).endVertex();

        tessellator.draw();

        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
    }

    private void renderOutputFluid(TerraAcidCollectorTile collector) {
        if (collector == null) {
            return;
        }

        FluidStack fluid = new FluidStack(Fluids.LAVA.getStillFluid(), collector.getOutputTank().getFluidAmount());
        if (fluid == null) {
            return;
        }

        Fluid renderFluid = fluid.getFluid();
        if (renderFluid == null) {
            return;
        }

        int fluidAmount = fluid.getAmount();

        float scale = (TANK_MAX_HEIGHT - TANK_THICKNESS / 2 - TANK_THICKNESS) * fluidAmount / (collector.getOutputTank().getCapacity());

        if (scale > 0.0f) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder renderer = tessellator.getBuffer();
            ResourceLocation still = new ResourceLocation("netherendingenergy:block/firsttile");
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(still.toString());

            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

            GlStateManager.color4f(1, 1, 1, .5f);
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

            float u1 = sprite.getMinU();
            float v1 = sprite.getMinV();
            float u2 = sprite.getMaxU();
            float v2 = sprite.getMaxV();

            float x1 = .65f;
            float x2 = .65f;
            float x3 = .01f;
            float x4 = .01f;
            float z1 = .01f;
            float z2 = .43f;
            float z3 = .43f;
            float z4 = .01f;

            int alpha = 128;

            // Top - x,y,z
            renderer.pos(x1, scale + TANK_THICKNESS, z1).tex(u1, v1).color(255, 255, 255, alpha).endVertex();
            renderer.pos(x2, scale + TANK_THICKNESS, 1 - z2).tex(u1, v2).color(255, 255, 255, alpha).endVertex();
            renderer.pos(1 - x3, scale + TANK_THICKNESS, 1 - z3).tex(u2, v2).color(255, 255, 255, alpha).endVertex();
            renderer.pos(1 - x4, scale + TANK_THICKNESS, z4).tex(u2, v1).color(255, 255, 255, alpha).endVertex();

            // depth
            float sideX1 = .01f;
            float sideX2 = .01f;
            float sideX3 = .01f;
            float sideX4 = .01f;

            float sideZ1 = .40f;
            float sideZ2 = .40f;
            float sideZ3 = 0;
            float sideZ4 = 0;

            // Side
            renderer.pos(1 - sideX1, scale + TANK_THICKNESS, 1 - sideZ1).tex(u2, v1).color(255, 255, 255, 128).endVertex();
            renderer.pos(1 - sideX2, TANK_THICKNESS, 1 - sideZ2).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            renderer.pos(1 - sideX3, TANK_THICKNESS, sideZ3).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            renderer.pos(1 - sideX4, scale + TANK_THICKNESS, sideZ4).tex(u1, v1).color(255, 255, 255, 128).endVertex();

            // Spout
            float spoutX1 = .13f;
            float spoutX2 = .13f;
            float spoutX3 = .13f;
            float spoutX4 = .13f;

            // Spout - left side going to right
            float spoutZ1 = .58f;
            float spoutZ2 = .58f;

            // Spout - right side going left
            float spoutZ3 = .33f;
            float spoutZ4 = .33f;

/*            renderer.pos(spoutX1, .5, spoutZ1).tex(u1, v1).color(255, 255, 255, alpha).endVertex();
            renderer.pos(spoutX2, TANK_THICKNESS, spoutZ2).tex(u1, v2).color(255, 255, 255, alpha).endVertex();
            renderer.pos(spoutX3, TANK_THICKNESS, 1 - spoutZ3).tex(u2, v2).color(255, 255, 255, alpha).endVertex();
            renderer.pos(spoutX4, .5, 1 - spoutZ4).tex(u2, v1).color(255, 255, 255, alpha).endVertex();*/

            // POURING

            // Width
            // left side going right
            float spoutRightX1 = .13f;
            float spoutRightX2 = .13f;

            // right side going left
            float spoutRightX3 = .75f;
            float spoutRightX4 = .75f;

            // height
            float spoutRightZ1 = .33f;
            float spoutRightZ2 = .33f;
            float spoutRightZ3 = .33f;
            float spoutRightZ4 = .33f;

/*
            renderer.pos(spoutRightX1, .5, 1 - spoutRightZ1).tex(u1, v1).color(255, 255, 255, 128).endVertex();
            renderer.pos(spoutRightX2, .05, 1 - spoutRightZ2).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            renderer.pos(1 - spoutRightX3, .05, 1 - spoutRightZ3).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            renderer.pos(1 - spoutRightX4, .5, 1 - spoutRightZ4).tex(u2, v1).color(255, 255, 255, 128).endVertex();
*/


            tessellator.draw();

            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        }
    }

    private void renderIntakeFluid(TerraAcidCollectorTile collector) {
        if (collector == null) {
            return;
        }

        FluidStack fluid = new FluidStack(Fluids.LAVA.getStillFluid(), collector.getInputTank().getFluidAmount());
        if (fluid == null) {
            return;
        }

        Fluid renderFluid = fluid.getFluid();
        if (renderFluid == null) {
            return;
        }

        int fluidAmount = fluid.getAmount();

        float scale = (TANK_MAX_HEIGHT - TANK_THICKNESS / 2 - TANK_THICKNESS) * fluidAmount / (collector.getInputTank().getCapacity());

        if (scale > 0.0f) {
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder renderer = tessellator.getBuffer();
            ResourceLocation still = new ResourceLocation("netherendingenergy:block/firstblock");
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureMap().getAtlasSprite(still.toString());

            net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();

            GlStateManager.color4f(1, 1, 1, .5f);
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

            float u1 = sprite.getMinU();
            float v1 = sprite.getMinV();
            float u2 = sprite.getMaxU();
            float v2 = sprite.getMaxV();

            float x1 = .01f;
            float x2 = .01f;
            float x3 = .63f;
            float x4 = .63f;
            float z1 = .5f;
            float z2 = 0;
            float z3 = 0;
            float z4 = .5f;

            int alpha = 128;

            // Top - x,y,z
            renderer.pos(x1, scale + TANK_THICKNESS, z1).tex(u1, v1).color(255, 255, 255, alpha).endVertex();
            renderer.pos(x2, scale + TANK_THICKNESS, 1 - z2).tex(u1, v2).color(255, 255, 255, alpha).endVertex();
            renderer.pos(1 - x3, scale + TANK_THICKNESS, 1 - z3).tex(u2, v2).color(255, 255, 255, alpha).endVertex();
            renderer.pos(1 - x4, scale + TANK_THICKNESS, z4).tex(u2, v1).color(255, 255, 255, alpha).endVertex();

            // depth
            float sideX1 = .01f;
            float sideX2 = .01f;
            float sideX3 = .01f;
            float sideX4 = .01f;

            float sideZ1 = .5f;
            float sideZ2 = .5f;
            float sideZ3 = 0;
            float sideZ4 = 0;

            // Side
            renderer.pos(sideX1, scale + TANK_THICKNESS, sideZ1).tex(u1, v1).color(255, 255, 255, alpha).endVertex();
            renderer.pos(sideX2, TANK_THICKNESS, sideZ2).tex(u1, v2).color(255, 255, 255, alpha).endVertex();
            renderer.pos(sideX3, TANK_THICKNESS, 1 - sideZ3).tex(u2, v2).color(255, 255, 255, alpha).endVertex();
            renderer.pos(sideX4, scale + TANK_THICKNESS, 1 - sideZ4).tex(u2, v1).color(255, 255, 255, alpha).endVertex();

            // Spout
            float spoutX1 = .13f;
            float spoutX2 = .13f;
            float spoutX3 = .13f;
            float spoutX4 = .13f;

            // Spout - left side going to right
            float spoutZ1 = .58f;
            float spoutZ2 = .58f;

            // Spout - right side going left
            float spoutZ3 = .33f;
            float spoutZ4 = .33f;

            renderer.pos(spoutX1, .5, spoutZ1).tex(u1, v1).color(255, 255, 255, alpha).endVertex();
            renderer.pos(spoutX2, TANK_THICKNESS, spoutZ2).tex(u1, v2).color(255, 255, 255, alpha).endVertex();
            renderer.pos(spoutX3, TANK_THICKNESS, 1 - spoutZ3).tex(u2, v2).color(255, 255, 255, alpha).endVertex();
            renderer.pos(spoutX4, .5, 1 - spoutZ4).tex(u2, v1).color(255, 255, 255, alpha).endVertex();

            // POURING

            // Width
            // left side going right
            float spoutRightX1 = .13f;
            float spoutRightX2 = .13f;

            // right side going left
            float spoutRightX3 = .75f;
            float spoutRightX4 = .75f;

            // height
            float spoutRightZ1 = .33f;
            float spoutRightZ2 = .33f;
            float spoutRightZ3 = .33f;
            float spoutRightZ4 = .33f;

            renderer.pos(spoutRightX1, .5, 1 - spoutRightZ1).tex(u1, v1).color(255, 255, 255, 128).endVertex();
            renderer.pos(spoutRightX2, .05, 1 - spoutRightZ2).tex(u1, v2).color(255, 255, 255, 128).endVertex();
            renderer.pos(1 - spoutRightX3, .05, 1 - spoutRightZ3).tex(u2, v2).color(255, 255, 255, 128).endVertex();
            renderer.pos(1 - spoutRightX4, .5, 1 - spoutRightZ4).tex(u2, v1).color(255, 255, 255, 128).endVertex();


            tessellator.draw();

            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        }
    }
}
