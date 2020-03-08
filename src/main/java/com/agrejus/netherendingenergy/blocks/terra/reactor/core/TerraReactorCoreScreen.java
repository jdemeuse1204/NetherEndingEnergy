package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.helpers.ResourceHelpers;
import com.agrejus.netherendingenergy.common.rendering.Rect;
import com.agrejus.netherendingenergy.common.screen.ContainerScreenBase;
import com.agrejus.netherendingenergy.fluids.AcidOfTheTearfulFluid;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.server.command.TextComponentHelper;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TerraReactorCoreScreen extends ContainerScreenBase<TerraReactorCoreContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_reactor_core_gui.png");
    private Rect energyLocation;
    private Rect acidLocation;

    public TerraReactorCoreScreen(TerraReactorCoreContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name, 176, 256);
    }

    @Override
    protected void init() {
        super.init();
        this.xSize = 176;
        this.ySize = 184;

        energyLocation = createRectBasedOnGui(16, 9, 20, 78);
        acidLocation = createRectBasedOnGui(11, 151, 168, 95);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        this.renderHoveredToolTip(mouseX, mouseY);

        List<String> tooltip = new ArrayList<>();
        if (this.isMouseOver(this.energyLocation, mouseX, mouseY)) {
            int amount = container.getEnergyStored();
            int tickAmount = container.getGeneratedEnergyPerTick();
            tooltip.add("Energy:");
            tooltip.add(String.format("%s RF", amount));
            tooltip.add(String.format("%s RF/t", tickAmount));
        }

        if (this.isMouseOver(this.acidLocation, mouseX, mouseY)) {
            int amount = container.getAcidStored();
            FluidStack stack = this.container.getFluid();
            if (stack.isEmpty() == false) {
                tooltip.add(stack.getDisplayName().getFormattedText());
            }
            tooltip.add(String.format("%s mB", amount));
        }

        if (!tooltip.isEmpty()) {
            this.renderTooltip(tooltip, mouseX, mouseY);
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawString(Minecraft.getInstance().fontRenderer, "Terra Reactor", 6, 5, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;

        // set the size of the GUI to slice off the right hand images
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        int energyStored = energyLocation.getComputedFillHeight(this.container.getMaxEnergyStored(), this.container.getEnergyStored());
        fillVertical(energyLocation, energyStored, 0xff8cff72);

        FluidStack stack = this.container.getFluid();
        int acidStored = acidLocation.getComputedFillHeight(this.container.getMaxAcidStored(), this.container.getAcidStored());
        fillVertical(acidLocation, acidStored, stack.getFluid().getAttributes().getColor());
    }
}
