package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.rendering.Rect;
import com.agrejus.netherendingenergy.common.rendering.RectProgression;
import com.agrejus.netherendingenergy.common.screen.ContainerScreenBase;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class TerraMixerScreen extends ContainerScreenBase<TerraMixerContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_mixer_gui.png");
    private static final ResourceLocation MIXER_BUTTONS = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_mixer_buttons.png");
    private Rect inputFluidLocation;
    private Rect outputFluidLocation;
    private RectProgression energySlice;
    private Rect energyDestination;

    public TerraMixerScreen(TerraMixerContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name, 176, 184);
    }

    @Override
    protected void init() {
        super.init();
        this.xSize = 176;
        this.ySize = 184;
        // 18 = height
        // 19 = width
        // _3_ = width of still button
        // _4_ = bottom of still button
        // _7_ = height of hovered button? of button when hovered ? Offset?
        // _6_ = top of still button

        energyDestination = createRectBasedOnGui(27, 152, 167, 67);
        energySlice = createProgressionSliceRect(44, this.defaultGuiScreenWidth + 1, 192, 84);

        inputFluidLocation = createRectBasedOnGui(27, 116, 132, 59);
        outputFluidLocation = createRectBasedOnGui(65, 116, 132, 97);

        // Tab button
        this.addButton((new ImageButton(this.guiLeft + 125, this.guiTop, 51, 22, 0, 60, 0, MIXER_BUTTONS, (p_214087_1_) -> {
            // on click callback
        })));

        // Clear Input Tank
        this.addButton((new ImageButton(this.guiLeft + 135, this.guiTop + 26, 9, 9, 0, 30, 0, MIXER_BUTTONS, (p_214087_1_) -> {
            this.container.voidInputTank();
        })));

        // Clear Tank 2
        this.addButton((new ImageButton(this.guiLeft + 135, this.guiTop + 64, 9, 9, 0, 30, 9, MIXER_BUTTONS, (p_214087_1_) -> {

        })));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        this.renderHoveredToolTip(mouseX, mouseY);

        List<String> tooltip = new ArrayList<>();

        if (this.isMouseOver(this.inputFluidLocation, mouseX, mouseY)) {
            int amount = this.container.getInputFluidAmount();

            if (amount > 0) {
                FluidStack fluidStack = this.container.getInputTankFluid();
                tooltip.add(fluidStack.getDisplayName().getFormattedText());
            }
            tooltip.add(String.format("%s mB", amount));
        }

        if (this.isMouseOver(this.energyDestination, mouseX, mouseY)) {
            int amount = container.getEnergyStored();
            int tickAmount = container.getEnergyPerTick();
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

        if (this.isMouseOver(this.outputFluidLocation, mouseX, mouseY)) {
            int amount = this.container.getOutputFluidAmount();

           if (amount > 0) {
                FluidStack fluidStack = this.container.getOutputTankFluid();
                tooltip.add(fluidStack.getDisplayName().getFormattedText());
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
        drawNoShadowString(Minecraft.getInstance().fontRenderer, "Terra Mixer", 6, 5, 0xffffff);
        int destructibleItemTicks = this.container.getDestructibleItemTicks();

        if (destructibleItemTicks > 0) {
            drawNoShadowString(Minecraft.getInstance().fontRenderer, String.format("%s", destructibleItemTicks), 7, 53, this.guiDefaultFontColor);
        }

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
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


        // fill input
        int inputTankAmount = this.container.getInputFluidAmount();

        if (inputTankAmount > 0) {
            int inputTankCapacity = this.container.getInputTankCapacity();
            int inputFillHeight = inputFluidLocation.getComputedFillHeight(inputTankCapacity, inputTankAmount);
            int color = this.container.getInputTankFluidColor();

            int totalHeight = inputFluidLocation.getTop() + inputFluidLocation.getHeight();
            int h = totalHeight - inputFillHeight;
            int y = totalHeight - h;
            drawColoredRect(inputFluidLocation.getLeft(), y, inputFluidLocation.getWidth(), h, color);
        }

        int outputTankAmount = this.container.getOutputFluidAmount();

        if (outputTankAmount > 0) {
            int outputTankCapacity = this.container.getOutputTankCapacity();
            int outputFillHeight = outputFluidLocation.getComputedFillHeight(outputTankCapacity, outputTankAmount);
            int color = this.container.getOutputTankFluidColor();

            int totalHeight = outputFluidLocation.getTop() + outputFluidLocation.getHeight();
            int h = totalHeight - outputFillHeight;
            int y = totalHeight - h;
            drawColoredRect(outputFluidLocation.getLeft(), y, outputFluidLocation.getWidth(), h, color);
        }
    }
}
