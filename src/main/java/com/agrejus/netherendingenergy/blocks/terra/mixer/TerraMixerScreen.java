package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.client.gui.screen.RedstoneActivatableScreen;
import com.agrejus.netherendingenergy.common.models.MixerRecipe;
import com.agrejus.netherendingenergy.common.rendering.Rect;
import com.agrejus.netherendingenergy.common.rendering.RectProgression;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class TerraMixerScreen extends RedstoneActivatableScreen<TerraMixerContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_mixer_gui.png");
    private Rect inputFluidLocation;
    private Rect inputFluidMixingLocation;
    private Rect outputFluidLocation;
    private Rect outputFluidMixingLocation;

    private RectProgression energySlice;
    private Rect energyDestination;

    private RectProgression progressionSlice;
    private Rect progressionDestination;

    private Rect choppingDestination;
    private RectProgression choppingSliceOne;
    private RectProgression choppingSliceTwo;
    private RectProgression choppingSliceThree;
    private RectProgression choppingSliceFour;
    private int counter = 0;

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

        choppingDestination = createRectBasedOnGui(28, 46, 99, 53);
        choppingSliceOne = createProgressionSliceRect(85, this.defaultGuiScreenWidth, 229, 110);
        choppingSliceTwo = createProgressionSliceRect(110, this.defaultGuiScreenWidth, 229, 135);
        choppingSliceThree = createProgressionSliceRect(135, this.defaultGuiScreenWidth, 229, 160);
        choppingSliceFour = createProgressionSliceRect(160, this.defaultGuiScreenWidth, 229, 185);

        progressionDestination = createRectBasedOnGui(58, 66, 79, 72);
        progressionSlice = createProgressionSliceRect(19, this.defaultGuiScreenWidth, 189, 33);

        energyDestination = createRectBasedOnGui(27, 152, 167, 67);
        energySlice = createProgressionSliceRect(44, this.defaultGuiScreenWidth + 1, 192, 84);

        inputFluidLocation = createRectBasedOnGui(27, 120, 136, 59);
        inputFluidMixingLocation = createRectBasedOnGui(33, 75, 91, 49);

        outputFluidLocation = createRectBasedOnGui(65, 120, 136, 97);
        outputFluidMixingLocation = createRectBasedOnGui(75, 66, 82, 91);

        // Clear Input Tank
        this.addButton((new ImageButton(this.guiLeft + 139, this.guiTop + 26, 9, 9, 0, 30, 0, GUI_BUTTONS, (button) -> {
            this.container.voidInputTank();
        })));

        // Clear Output Tank
        this.addButton((new ImageButton(this.guiLeft + 139, this.guiTop + 64, 9, 9, 0, 30, 0, GUI_BUTTONS, (button) -> {
            this.container.voidOutputTank();
        })));
    }

    @Override
    protected void renderHoverToolTip(List<String> tooltip, int mouseX, int mouseY, float partialTicks) {
        if (this.isMouseOver(this.inputFluidLocation, mouseX, mouseY)) {
            int amount = this.container.getInputFluidAmount();

            if (amount > 0) {
                FluidStack fluidStack = this.container.getInputTankFluid();
                tooltip.add(fluidStack.getDisplayName().getFormattedText());
            }
            tooltip.add(String.format("%s mB", amount));
        }

        if (this.isMouseOver(this.inputFluidMixingLocation, mouseX, mouseY)) {
            FluidStack stack = this.container.getInputTankFluid();

            tooltip.add(new StringTextComponent("Input Acid").getFormattedText());
            if (stack.getAmount() > 0) {
                tooltip.add(stack.getDisplayName().getFormattedText());
            } else {
                tooltip.add(new StringTextComponent("None").getFormattedText());
            }
        }

        if (this.isMouseOver(this.outputFluidMixingLocation, mouseX, mouseY)) {

            MixerRecipe currentRecipe = this.container.getCurrentRecipe();
            tooltip.add(new StringTextComponent("Acid Result").getFormattedText());

            if (currentRecipe == null) {
                tooltip.add(new StringTextComponent("None").getFormattedText());
            } else {
                FluidStack stack = new FluidStack(currentRecipe.getResultFluid(), 1);
                tooltip.add(stack.getDisplayName().getFormattedText());
            }
        }

        if (this.isMouseOver(this.energyDestination, mouseX, mouseY)) {
            int amount = container.getEnergyStored();
            int tickAmount = container.getEnergyPerTick();
            int maxStorage = container.getMaxEnergyStored();
            tooltip.add(String.format("%s / %s RF", amount, maxStorage));

            TextFormatting textFormatting = TextFormatting.WHITE;
            if (tickAmount > 0) {
                textFormatting = TextFormatting.GREEN;
            } else if (tickAmount < 0) {
                textFormatting = TextFormatting.RED;
            }

            tooltip.add(new StringTextComponent(textFormatting + String.format("%s RF/t", tickAmount)).getFormattedText());
        }

        if (this.isMouseOver(this.outputFluidLocation, mouseX, mouseY)) {
            int amount = this.container.getOutputFluidAmount();

            if (amount > 0) {
                FluidStack fluidStack = this.container.getOutputTankFluid();
                tooltip.add(fluidStack.getDisplayName().getFormattedText());
            }
            tooltip.add(String.format("%s mB", amount));
        }

        super.renderHoverToolTip(tooltip, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawNoShadowString(Minecraft.getInstance().fontRenderer, "Terra Mixer", 6, 5, 0xffffff);
        drawNoShadowString(Minecraft.getInstance().fontRenderer, "Spatial:", 7, 60, this.guiDefaultFontColor);
        drawNoShadowString(Minecraft.getInstance().fontRenderer, String.format("%s", this.container.currentPosition().getY()), 7, 70, this.guiDefaultFontColor);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;

        // set the size of the GUI to slice off the right hand images
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        // Draw Energy
        drawVerticalSliceWithProgressionUp(this.container.getEnergyStored(), this.container.getMaxEnergyStored(), this.energySlice, this.energyDestination);

        // Draw Collection Progression
        int collectionTick = this.container.getProcessingTicks();
        if (collectionTick > 0) {
            int ticksToCollect = this.container.getTotalProcessingTicks();
            drawVerticalSliceWithProgressionDown(collectionTick, ticksToCollect, this.progressionSlice, this.progressionDestination);
        }

        if (this.container.isChopping() == true) {
            counter++;
            if (counter <= 10) {
                drawSlice(this.choppingDestination, choppingSliceOne);
            } else if (counter > 10 && counter <= 20) {
                drawSlice(this.choppingDestination, choppingSliceTwo);
            } else if (counter > 20 && counter <= 30) {
                drawSlice(this.choppingDestination, choppingSliceThree);
            } else if (counter > 30 && counter < 40) {
                drawSlice(this.choppingDestination, choppingSliceFour);
            } else {
                drawSlice(this.choppingDestination, choppingSliceFour);
                counter = 0;
            }
        } else {
            counter = 0;
        }

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

            drawColoredRect(inputFluidMixingLocation.getLeft(), inputFluidMixingLocation.getTop(), inputFluidMixingLocation.getWidth(), inputFluidMixingLocation.getHeight(), color);
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

        MixerRecipe currentRecipe = this.container.getCurrentRecipe();

        int originHeight = 16;
        int originWidth = 16;
        int originLeft = 193;
        int destinationTop = this.guiTop + 33;
        int destinationLeft = this.guiLeft + 54;

        int destructibleItemTotalTicks = this.container.getDestructibleItemTotalTicks();
        int destructibleItemTicks = this.container.getDestructibleItemTotalTicks() - this.container.getDestructibleItemTicks();

        if (this.container.getDestructibleItemTotalTicks() != this.container.getDestructibleItemTicks()) {
            int step = destructibleItemTotalTicks / 5;
            int currentStep = Math.round(destructibleItemTicks / step) * step;
            float stepAmount = 64f / (float) destructibleItemTotalTicks;
            int currentHeight = Math.round(currentStep * stepAmount);
            int height = Math.min(currentHeight, 64);

            innerBlitOverlay(destinationLeft, destinationTop, originLeft, height, originWidth, originHeight);
        }

        if (currentRecipe != null) {
            drawColoredRect(outputFluidMixingLocation.getLeft(), outputFluidMixingLocation.getTop(), outputFluidMixingLocation.getWidth(), outputFluidMixingLocation.getHeight(), currentRecipe.getResultFluid().getAttributes().getColor());
        }
    }
}
