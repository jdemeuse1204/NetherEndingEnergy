package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.rendering.Rect;
import com.agrejus.netherendingenergy.common.rendering.RectProgression;
import com.agrejus.netherendingenergy.common.screen.ContainerScreenBase;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TerraReactorCoreScreen extends ContainerScreenBase<TerraReactorCoreContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_reactor_core_gui.png");

    private Rect acidDestination;

    private RectProgression heatSlice;
    private Rect heatDestination;

    private RectProgression energySlice;
    private Rect energyDestination;

    private RectProgression progressionSlice;
    private Rect progressionDestination;

    public TerraReactorCoreScreen(TerraReactorCoreContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name, 176, 185);
    }

    @Override
    protected void init() {
        super.init();
        this.xSize = 176;
        this.ySize = 184;

        acidDestination = createRectBasedOnGui(30, 90, 106, 46);

        heatDestination = createRectBasedOnGui(40, 120, 135, 80);
        heatSlice = createProgressionSliceRect(55, this.defaultGuiScreenWidth, 191, 95);

        energyDestination = createRectBasedOnGui(40, 150, 165, 80);
        energySlice = createProgressionSliceRect(14, this.defaultGuiScreenWidth, 191, 54);

        progressionDestination = createRectBasedOnGui(67, 72, 84, 79);
        progressionSlice = createProgressionSliceRect(0, this.defaultGuiScreenWidth, 190, 13);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        this.renderHoveredToolTip(mouseX, mouseY);

        List<String> tooltip = new ArrayList<>();

        if (this.isMouseOver(this.energyDestination, mouseX, mouseY)) {
            int amount = container.getEnergyStored();
            int tickAmount = container.getGeneratedEnergyPerTick();
            int maxStorage = container.getMaxEnergyStored();
            tooltip.add(String.format("%s / %s RF", amount, maxStorage));

            if (tickAmount > 0) {
                tooltip.add(new StringTextComponent(TextFormatting.GREEN + String.format("%s RF/t", tickAmount)).getFormattedText());
            } else if (tickAmount < 0) {
                tooltip.add(new StringTextComponent(TextFormatting.RED + String.format("-%s RF/t", tickAmount)).getFormattedText());
            } else {
                tooltip.add(String.format("%s RF/t", tickAmount));
            }
        }

        if (this.isMouseOver(this.acidDestination, mouseX, mouseY)) {
            int amount = container.getAcidStored();

            if (amount > 0) {
                FluidStack stack = this.container.getFluid();
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
        drawNoShadowString(Minecraft.getInstance().fontRenderer, "Terra Reactor", 6, 5, 0xffffff);

        String acidAmount = this.getBucketDisplayAmount(this.container.getAcidStored());
        drawNoShadowCenteredString(Minecraft.getInstance().fontRenderer, acidAmount, 94, 50, 0xff000000);

        String energyAmount = this.getEnergyDisplayAmount(this.container.getEnergyStored());
        drawNoShadowCenteredString(Minecraft.getInstance().fontRenderer, energyAmount, 157, 84, 0xff000000);
        drawNoShadowCenteredString(Minecraft.getInstance().fontRenderer, "RF", 157, 30, 0xff000000);

        int heat = this.container.getHeat();
        drawNoShadowCenteredString(Minecraft.getInstance().fontRenderer, String.format("%sHu", heat), 127, 30, 0xff000000);
        drawNoShadowCenteredString(Minecraft.getInstance().fontRenderer, "Temp", 128, 84, 0xff000000);

        String timeLeft = this.getDisplayTimeFromTicks(this.container.getBurnItemTicksLeft());
        drawNoShadowCenteredString(Minecraft.getInstance().fontRenderer, timeLeft, 95, 84, 0xff000000);


        int injectorOneUsesLeft = this.container.getUsesLeftInjectorOne();
        if (injectorOneUsesLeft > 0) {
            drawShadowCenteredOverlayString(Minecraft.getInstance().fontRenderer, String.format("%s", injectorOneUsesLeft), 31, 34, 0xffffff);
        }

        int injectorTwoUsesLeft = this.container.getUsesLeftInjectorTwo();
        if (injectorTwoUsesLeft > 0) {
            drawShadowCenteredOverlayString(Minecraft.getInstance().fontRenderer, String.format("%s", injectorTwoUsesLeft), 11, 54, 0xffffff);
        }

        int injectorThreeUsesLeft = this.container.getUsesLeftInjectorThree();
        if (injectorThreeUsesLeft > 0) {
            drawShadowCenteredOverlayString(Minecraft.getInstance().fontRenderer, String.format("%s", injectorThreeUsesLeft), 31, 74, 0xffffff);
        }

        int injectorFourUsesLeft = this.container.getUsesLeftInjectorFour();
        if (injectorFourUsesLeft > 0) {
            drawShadowCenteredOverlayString(Minecraft.getInstance().fontRenderer, String.format("%s", injectorFourUsesLeft), 51, 54, 0xffffff);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0f);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;

        // set the size of the GUI to slice off the right hand images
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        // Draw Energy Progression
        float energyPercent = this.getProgressionPercent(this.container.getEnergyStored(), this.container.getMaxEnergyStored());
        int energyProgression = Math.round(this.energySlice.getHeight() * energyPercent);
        this.energySlice.setProgression(energyProgression);
        this.drawVerticalSliceWithProgression(this.energyDestination, this.energySlice);

        // Draw Reactant Progression
        float progressionPercent = 1f - this.getProgressionPercent(this.container.getBurnItemTicksLeft(), this.container.getBurnItemTotalTicks());
        int currentProgression = progressionPercent == 1 ? 0 : Math.round(this.progressionSlice.getHeight() * progressionPercent);
        this.progressionSlice.setProgression(currentProgression);
        this.drawVerticalSliceWithProgression(this.progressionDestination, this.progressionSlice);

        // Draw Heat Progression
        float heatPercent = this.getProgressionPercent(this.container.getHeat(), this.container.getMaxHeat());
        int heatProgression = Math.round(this.heatSlice.getHeight() * heatPercent);
        this.heatSlice.setProgression(heatProgression);
        this.drawVerticalSliceWithProgression(this.heatDestination, this.heatSlice);

        // Draw Acid - Draw last otherwise colors will be goofy
        if (this.container.getAcidStored() > 0) {
            FluidStack stack = this.container.getFluid();
            fillVertical(acidDestination, acidDestination.getTop(), stack.getFluid().getAttributes().getColor());
        }
    }
}
