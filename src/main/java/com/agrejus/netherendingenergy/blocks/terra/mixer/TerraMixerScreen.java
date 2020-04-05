package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.client.gui.button.NEEImageButton;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
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
    private static final ResourceLocation MIXER_BUTTONS = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/gui_buttons.png");
    private Rect inputFluidLocation;
    private Rect inputFluidMixingLocation;
    private Rect outputFluidLocation;
    private Rect outputFluidMixingLocation;

    private RectProgression energySlice;
    private Rect energyDestination;

    private NEEImageButton redstoneActiveWithSignal;
    private NEEImageButton redstoneActibeWithoutSignal;
    private NEEImageButton redstoneAlwaysActive;

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

        inputFluidLocation = createRectBasedOnGui(27, 120, 136, 59);
        inputFluidMixingLocation = createRectBasedOnGui(33, 75, 91, 49);

        outputFluidLocation = createRectBasedOnGui(65, 120, 136, 97);
        outputFluidMixingLocation = createRectBasedOnGui(75, 66, 82, 91);

        // Swap between redstone signals
        RedstoneActivationType redstoneActivationType = this.container.getRedstoneActivationType();
        this.redstoneActiveWithSignal = new NEEImageButton(redstoneActivationType == RedstoneActivationType.ACTIVE_WITH_SIGNAL, this.guiLeft + 178, this.guiTop + 20, 14, 14, 0, 106, 0, MIXER_BUTTONS, (button) -> {
            ((NEEImageButton) button).setVisibility(false);
            this.redstoneActibeWithoutSignal.setVisibility(true);
            this.container.changeRedstoneActivationType(RedstoneActivationType.ACTIVE_WITHOUT_SIGNAL);
        });
        this.redstoneActibeWithoutSignal= new NEEImageButton(redstoneActivationType == RedstoneActivationType.ACTIVE_WITHOUT_SIGNAL, this.guiLeft + 178, this.guiTop + 20, 14, 14, 0, 136, 0, MIXER_BUTTONS, (button) -> {
            ((NEEImageButton) button).setVisibility(false);
            this.redstoneAlwaysActive.setVisibility(true);
            this.container.changeRedstoneActivationType(RedstoneActivationType.ALWAYS_ACTIVE);
        });
        this.redstoneAlwaysActive = new NEEImageButton(redstoneActivationType == RedstoneActivationType.ALWAYS_ACTIVE, this.guiLeft + 178, this.guiTop + 20, 14, 14, 0, 166, 0, MIXER_BUTTONS, (button) -> {
            ((NEEImageButton) button).setVisibility(false);
            this.redstoneActiveWithSignal.setVisibility(true);
            this.container.changeRedstoneActivationType(RedstoneActivationType.ACTIVE_WITH_SIGNAL);
        });

        // Redstone Always Active
        this.addButton(this.redstoneAlwaysActive);

        // Redstone Active With Signal
        this.addButton(this.redstoneActiveWithSignal);

        // Redstone Active Without Signal
        this.addButton( this.redstoneActibeWithoutSignal);

        // Clear Input Tank
        this.addButton((new ImageButton(this.guiLeft + 139, this.guiTop + 26, 9, 9, 0, 30, 0, MIXER_BUTTONS, (button) -> {
            /*            ((ImageButton)button).*/
            this.container.voidInputTank();
        })));

        // Clear Output Tank
        this.addButton((new ImageButton(this.guiLeft + 139, this.guiTop + 64, 9, 9, 0, 30, 0, MIXER_BUTTONS, (button) -> {
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

        int originHeight = 16;
        int originWidth = 16;
        int originLeft = 193;
        int destinationTop = this.guiTop + 33;
        int destinationLeft = this.guiLeft + 54;

        int destructibleItemTotalTicks = this.container.getDestructibleItemTotalTicks();
        int destructibleItemTicks = this.container.getDestructibleItemTotalTicks() - this.container.getDestructibleItemTicks();

        if (destructibleItemTotalTicks > 0) {
            int step = destructibleItemTotalTicks / 5;
            int currentStep = Math.round(destructibleItemTicks / step) * step;
            float stepAmount = 64f / (float) destructibleItemTotalTicks;
            int currentHeight = Math.round(currentStep * stepAmount);
            int height = Math.min(currentHeight, 64);

            test(destinationLeft, destinationTop, originLeft, height, originWidth, originHeight);
        }

        if (currentRecipe != null) {
            drawColoredRect(outputFluidMixingLocation.getLeft(), outputFluidMixingLocation.getTop(), outputFluidMixingLocation.getWidth(), outputFluidMixingLocation.getHeight(), currentRecipe.getResultFluid().getAttributes().getColor());
        }
    }

    private void test(int p_blit_1_, int p_blit_2_, int p_blit_3_, int p_blit_4_, int p_blit_5_, int p_blit_6_) {
        test2(p_blit_1_, p_blit_2_, this.blitOffset, (float) p_blit_3_, (float) p_blit_4_, p_blit_5_, p_blit_6_, 256, 256);
    }

    public void test2(int p_blit_0_, int p_blit_1_, int p_blit_2_, float p_blit_3_, float p_blit_4_, int p_blit_5_, int p_blit_6_, int p_blit_7_, int p_blit_8_) {
        innerBlitx(p_blit_0_, p_blit_0_ + p_blit_5_, p_blit_1_, p_blit_1_ + p_blit_6_, p_blit_2_, p_blit_5_, p_blit_6_, p_blit_3_, p_blit_4_, p_blit_8_, p_blit_7_);
    }

    private void innerBlitx(int p_innerBlit_0_, int p_innerBlit_1_, int p_innerBlit_2_, int p_innerBlit_3_, int p_innerBlit_4_, int p_innerBlit_5_, int p_innerBlit_6_, float p_innerBlit_7_, float p_innerBlit_8_, int p_innerBlit_9_, int p_innerBlit_10_) {
        innerBlit2(p_innerBlit_0_, p_innerBlit_1_, p_innerBlit_2_, p_innerBlit_3_, p_innerBlit_4_, (p_innerBlit_7_ + 0.0F) / (float) p_innerBlit_9_, (p_innerBlit_7_ + (float) p_innerBlit_5_) / (float) p_innerBlit_9_, (p_innerBlit_8_ + 0.0F) / (float) p_innerBlit_10_, (p_innerBlit_8_ + (float) p_innerBlit_6_) / (float) p_innerBlit_10_);
    }

    protected void innerBlit2(int p_innerBlit_0_, int p_innerBlit_1_, int p_innerBlit_2_, int p_innerBlit_3_, int p_innerBlit_4_, float p_innerBlit_5_, float p_innerBlit_6_, float p_innerBlit_7_, float p_innerBlit_8_) {

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


}
