package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.client.gui.screen.RedstoneActivatableScreen;
import com.agrejus.netherendingenergy.common.rendering.Rect;
import com.agrejus.netherendingenergy.common.screen.ContainerScreenBase;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TerraCollectingStationScreen extends RedstoneActivatableScreen<TerraCollectingStationContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_collecting_station_gui.png");

    private Rect energyLocation;
    private Rect inputFluidLocation;
    private Rect outputFluidLocation;
    private Rect progressSliceLocation;
    private Rect progressDrawLocation;

    public TerraCollectingStationScreen(TerraCollectingStationContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name, 176,176);
    }

    @Override
    protected void init() {
        super.init();

        energyLocation = createRectBasedOnGui(17, 9, 20, 63);
        inputFluidLocation = createRectBasedOnGui(11, 119, 136, 71);
        outputFluidLocation = createRectBasedOnGui(11, 151, 168, 71);

        progressSliceLocation = createSliceRect(0, this.defaultGuiScreenWidth, 0, 16);
        progressDrawLocation = createSliceDrawDestination(39, 78);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        // Tool Tip - Here would be the place for a custom tool tip
        this.renderHoveredToolTip(mouseX, mouseY);

        List<String> tooltip = new ArrayList<>();
        if (this.isMouseOver(this.outputFluidLocation, mouseX, mouseY)) {
            int amount = container.getOutputFluidAmount();
            tooltip.add("Output:");
            tooltip.add(String.format("%s mB", amount));
        }

        if (this.isMouseOver(this.inputFluidLocation, mouseX, mouseY)) {
            int amount = container.getInputFluidAmount();
            tooltip.add("Input:");
            tooltip.add(String.format("%s mB", amount));
        }

        if (this.isMouseOver(this.energyLocation, mouseX, mouseY)) {
            int amount = container.getEnergyStored();
            tooltip.add("Energy:");
            tooltip.add(String.format("%s FE", amount));
        }

        if (!tooltip.isEmpty()) {
            this.renderTooltip(tooltip, mouseX, mouseY);
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawString(Minecraft.getInstance().fontRenderer, "Terra Collecting Station", 6, 6, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;

        // set the size of the GUI to slice off the right hand images
        this.blit(i, j, 0, 0, this.xSize, this.ySize);


        // 176,14 is where the image is from
        // 16 is the height of the image
        // 24 is the width, comes from getCookProgressionScaled
        // 79 is the left location where the image should be
        // 34 is the top location where the image should be
        //this.blit(i + 78, j + 39, 176, 0, progression + 1, 16);

        int progression = this.container.getProcessProgressionScaled();
        progressSliceLocation.setRight(progression);

        drawOverlaySlice(progressSliceLocation, progressDrawLocation);

        // fill energy
        int energyStored = energyLocation.getComputedFillHeight(this.container.getMaxEnergyStored(), this.container.getEnergyStored());
        fillVertical(energyLocation, energyStored, 0xff8cff72);

        // fill output
        int outputFillHeight = outputFluidLocation.getComputedFillHeight(this.container.getOutputTankCapacity(), this.container.getOutputFluidAmount());
        fillVertical(outputFluidLocation, outputFillHeight, 0xffe68f00);

        // fill input
        int inputFillHeight = inputFluidLocation.getComputedFillHeight(this.container.getInputTankCapacity(), this.container.getInputFluidAmount());
        fillVertical(inputFluidLocation, inputFillHeight, 0xffb300e6);
    }
}
