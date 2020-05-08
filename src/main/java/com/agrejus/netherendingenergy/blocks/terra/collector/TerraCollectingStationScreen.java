package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.client.gui.screen.RedstoneActivatableScreen;
import com.agrejus.netherendingenergy.common.rendering.Rect;
import com.agrejus.netherendingenergy.common.rendering.RectProgression;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TerraCollectingStationScreen extends RedstoneActivatableScreen<TerraCollectingStationContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_collecting_station_gui.png");

    private Rect energyLocation;
    private Rect inputFluidLocation;
    private Rect outputFluidLocation;
    private RectProgression progressSliceLocation;
    private RectProgression energySliceLocation;
    private Rect progressDrawLocation;
    private Rect progressLocation;

    private RectProgression flowerSliceLocation;
    private Rect flowerLocation;
    private Rect flowerDrawLocation;

    private Rect processingStageOneDrawLocation;
    private RectProgression processingStageOneSliceLocation;

    private Rect processingStageTwoDrawLocation;
    private RectProgression processingStageTwoSliceLocation;

    private Rect processingStageThreeDrawLocation;
    private RectProgression processingStageThreeSliceLocation;

    private Rect processingLocation;

    public TerraCollectingStationScreen(TerraCollectingStationContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name, 176, 184);
    }

    @Override
    protected void init() {
        super.init();
        this.xSize = 176;
        this.ySize = 184;

        energyLocation = createRectBasedOnGui(27, 152, 167, 67);
        inputFluidLocation = createRectBasedOnGui(33, 70, 86, 65);
        outputFluidLocation = createRectBasedOnGui(33, 121, 137, 65);
        progressLocation = createRectBasedOnGui(75, 20, 35, 91);

        energySliceLocation = createProgressionSliceRect(18, this.defaultGuiScreenWidth + 1, 192, 58);

        progressSliceLocation = createProgressionSliceRect(88, this.defaultGuiScreenWidth, 191, 103);
        progressDrawLocation = createSliceDrawDestination(75, 20);

        flowerSliceLocation = createProgressionSliceRect(0, 177, 194, 17);
        flowerLocation = createRectBasedOnGui(32, 20, 37, 49);
        flowerDrawLocation = createSliceDrawDestination(32, 20);

        processingStageOneDrawLocation = createSliceDrawDestination(37, 92);
        processingStageOneSliceLocation = createProgressionSliceRect(61, this.defaultGuiScreenWidth, 185, 88);

        processingStageTwoDrawLocation = createSliceDrawDestination(36, 98);
        processingStageTwoSliceLocation = createProgressionSliceRect(60, 182, 199, 63);

        processingStageThreeDrawLocation = createSliceDrawDestination(39, 104);
        processingStageThreeSliceLocation = createProgressionSliceRect(63, 188, 199, 88);

        processingLocation = createRectBasedOnGui(35, 92, 115, 64);
    }

    @Override
    protected void renderHoverToolTip(List<String> tooltip, int mouseX, int mouseY, float partialTicks) {

        if (this.isMouseOver(this.outputFluidLocation, mouseX, mouseY)) {
            int amount = container.getOutputFluidAmount();
            tooltip.add("Acid of the Ordinary:");
            tooltip.add(String.format("%s mB", amount));
        }

        if (this.isMouseOver(this.flowerLocation, mouseX, mouseY)) {
            boolean hasBellPlanted = container.hasBellPlanted();

            if (hasBellPlanted == false) {
                tooltip.add("No Bell Planted");
            } else {
                float yield = container.getBellYield();
                float strength = container.getBellStrength();
                float purity = container.getBellPurity();
                tooltip.add(String.format("Yield: %s mB", yield));
                tooltip.add("Strength: " + Math.round(strength * 100) + "%");
                tooltip.add("Purity: " + Math.round(purity * 100) + "%");
            }
        }

        if (this.isMouseOver(this.inputFluidLocation, mouseX, mouseY)) {
            int amount = container.getInputFluidAmount();
            tooltip.add("Raw Acid:");
            tooltip.add(String.format("%s mB", amount));
        }

        if (this.isMouseOver(this.progressLocation, mouseX, mouseY)) {
            float amount = container.getCycleCollectionAmount();
            tooltip.add("Cycle Amount:");
            tooltip.add(String.format("%s mB", new DecimalFormat("#.###").format(amount)));
            tooltip.add("Cycle Length:");
            tooltip.add(String.format("%s ticks", this.container.getCollectionTotalTicks()));
        }

        if (this.isMouseOver(this.energyLocation, mouseX, mouseY)) {
            int amount = container.getEnergyStored();
            tooltip.add("Energy:");
            tooltip.add(getEnergyPerTick(this.container.getEnergyPerTick()));
            tooltip.add(String.format("%s RF", amount));
        }

        if (this.isMouseOver(this.processingLocation, mouseX, mouseY)) {
            int refineTicks = this.container.getRefineTicks();
            int totalRefineTicks = this.container.getTotalTicksToRefine();
            tooltip.add("Progress:");
            int progress = (int) (((float) (totalRefineTicks - refineTicks) / (float) totalRefineTicks) * 100);
            tooltip.add(progress + "%");
        }

        super.renderHoverToolTip(tooltip, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawString(Minecraft.getInstance().fontRenderer, "Terra Collecting Station", 6, 6, 0xffffff);
    }

    private int min(int a, int b) {
        int result = Math.min(a, b);
        if (result < 0) {
            return 0;
        }
        return result;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;

        // set the size of the GUI to slice off the right hand images
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        // Refine progression
        int refineTicks = this.container.getRefineTicks();

        if (refineTicks > 0) {
            int totalRefineTicks = this.container.getTotalTicksToRefine();
            int tick = totalRefineTicks - refineTicks;
            int split = totalRefineTicks / 3;

            drawVerticalSliceWithProgressionUp(min((tick - (split * 1)) + split, split), split, this.processingStageOneSliceLocation, this.processingStageOneDrawLocation);
            drawVerticalSliceWithProgressionRight(min((tick - (split * 2)) + split, split), split, this.processingStageTwoSliceLocation, this.processingStageTwoDrawLocation);
            drawVerticalSliceWithProgressionDown(min((tick - (split * 3)) + split, split), split, this.processingStageThreeSliceLocation, this.processingStageThreeDrawLocation);
        }

        // draw flower
        if (this.container.hasBellPlanted()) {
            this.blit(flowerDrawLocation.getLeft(),
                    flowerDrawLocation.getTop(),
                    flowerSliceLocation.getLeft(),
                    flowerSliceLocation.getTop(),
                    flowerSliceLocation.getWidth(),
                    flowerSliceLocation.getHeight());
        }

        // Draw Energy
        drawVerticalSliceWithProgressionUp(this.container.getEnergyStored(), this.container.getMaxEnergyStored(), this.energySliceLocation, this.energyLocation);

        // Draw Collection Progression
        int collectionTick = this.container.getCollectionTick();
        if (collectionTick > 0) {
            int ticksToCollect = this.container.getCollectionTotalTicks();
            drawVerticalSliceWithProgressionDown(collectionTick, ticksToCollect, this.progressSliceLocation, this.progressDrawLocation);
        }

        // Draw Input Fluid
        drawTankVertical(this.container.getInputFluidAmount(), this.container.getInputTankCapacity(), this.container.getInputTankFluidColor(), this.inputFluidLocation);

        // Draw Output Fluid
        drawTankVertical(this.container.getOutputFluidAmount(), this.container.getOutputTankCapacity(), this.container.getOutputTankFluidColor(), this.outputFluidLocation);
    }
}
