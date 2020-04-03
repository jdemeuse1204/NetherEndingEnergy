package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.models.MixerRecipe;
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
    private Rect inputFluidMixingLocation;
    private Rect outputFluidLocation;
    private Rect outputFluidMixingLocation;

    private RectProgression energySlice;
    private Rect energyDestination;

    private RectProgression progressionSlice;
    private Rect progressionDestination;

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

        progressionDestination = createRectBasedOnGui(59, 71, 84, 73);
        progressionSlice = createProgressionSliceRect(19, this.defaultGuiScreenWidth, 190, 33);

        energyDestination = createRectBasedOnGui(27, 152, 167, 67);
        energySlice = createProgressionSliceRect(44, this.defaultGuiScreenWidth + 1, 192, 84);

        inputFluidLocation = createRectBasedOnGui(27, 120, 135, 59);
        inputFluidMixingLocation = createRectBasedOnGui(33, 75, 91, 49);

        outputFluidLocation = createRectBasedOnGui(65, 120, 135, 97);
        outputFluidMixingLocation = createRectBasedOnGui(75, 66, 82, 91);

        // Redstone button!

        //this.container.changeRedstoneActivationType();

        // Tab button
        this.addButton((new ImageButton(this.guiLeft + 125, this.guiTop, 51, 22, 0, 60, 0, MIXER_BUTTONS, (p_214087_1_) -> {
            // on click callback
        })));

        // Clear Input Tank
        this.addButton((new ImageButton(this.guiLeft + 139, this.guiTop + 26, 9, 9, 0, 30, 0, MIXER_BUTTONS, (p_214087_1_) -> {
            this.container.voidInputTank();
        })));

        // Clear Output Tank
        this.addButton((new ImageButton(this.guiLeft + 139, this.guiTop + 64, 9, 9, 0, 30, 0, MIXER_BUTTONS, (p_214087_1_) -> {
            this.container.voidOutputTank();
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
            drawShadowCenteredOverlayString(Minecraft.getInstance().fontRenderer, String.format("%s", destructibleItemTicks), 50, 32, 0xffffff);
        }

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

        // Processing Progression
/*        this.blit(this.progressionDestination.getLeft(),
                this.progressionDestination.getTop(),
                this.progressionSlice.getLeft(),
                this.progressionSlice.getBottom(),
                this.progressionSlice.getWidth(),
                this.progressionSlice.getHeight());*/

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

        int x0 = this.guiLeft + 68;
        int y0 = this.guiTop + 45;
        int x1 = this.guiLeft + 71;
        int y1 = this.guiTop + 49;
        drawGradientRect(x0, y0, x1, y1, 0xff4CE500,   0xffE84444);

        if (currentRecipe != null) {
            drawColoredRect(outputFluidMixingLocation.getLeft(), outputFluidMixingLocation.getTop(), outputFluidMixingLocation.getWidth(), outputFluidMixingLocation.getHeight(), currentRecipe.getResultFluid().getAttributes().getColor());
        }
    }
}
