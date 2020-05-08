package com.agrejus.netherendingenergy.blocks.terra.reactor.core;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.client.gui.button.NEEImageButton;
import com.agrejus.netherendingenergy.client.gui.screen.RedstoneActivatableScreen;
import com.agrejus.netherendingenergy.common.reactor.Injector;
import com.agrejus.netherendingenergy.common.rendering.Rect;
import com.agrejus.netherendingenergy.common.rendering.RectProgression;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.crafting.conditions.FalseCondition;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class TerraReactorCoreScreen extends RedstoneActivatableScreen<TerraReactorCoreContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_reactor_core_gui.png");
    private ResourceLocation STATS_GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_reactor_core_alt_gui.png");
    private boolean isInformationScreenVisible = false;

    private Rect acidDestination;

    private RectProgression heatSlice;
    private Rect heatDestination;

    private RectProgression energySlice;
    private Rect energyDestination;

    private RectProgression progressionSlice;
    private Rect progressionDestination;

    private NEEImageButton informationTabActive;
    private NEEImageButton informationTabInActive;
    private NEEImageButton reactorTabActive;
    private NEEImageButton reactorTabInActive;

    private NEEImageButton inactiveSlotOne;
    private NEEImageButton inactiveSlotTwo;
    private NEEImageButton inactiveSlotThree;
    private NEEImageButton inactiveSlotFour;

    private NEEImageButton activeSlotOne;
    private NEEImageButton activeSlotTwo;
    private NEEImageButton activeSlotThree;
    private NEEImageButton activeSlotFour;

    private Map<Direction, ArrayList<NEEImageButton>> injectorSlots = new HashMap<>();

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

        // Reactor Tab - Active
        this.reactorTabActive = this.addButton(new NEEImageButton(this.isInformationScreenVisible == false, guiLeft + 147, guiTop + 0, 29, 22, 0, 196, 0, GUI_BUTTONS, (button) -> {

            this.showSlotButtons();
        }));

        // Information Tab - Inactive
        this.informationTabInActive = this.addButton(new NEEImageButton(this.isInformationScreenVisible == false, guiLeft + 126, guiTop + 2, 21, 18, 0, 219, 0, GUI_BUTTONS, (button) -> {

            this.reactorTabActive.setVisibility(false);
            ((NEEImageButton) button).setVisibility(false);
            this.reactorTabInActive.setVisibility(true);
            this.informationTabActive.setVisibility(true);

            // remove slot buttons
            this.hideSlotButtons();

            this.isInformationScreenVisible = true;
            this.container.removePlayerInventory();
        }));

        // Reactor Tab - Inactive
        this.reactorTabInActive = this.addButton(new NEEImageButton(this.isInformationScreenVisible == true, guiLeft + 150, guiTop + 2, 25, 20, 30, 196, 0, GUI_BUTTONS, (button) -> {

            this.reactorTabActive.setVisibility(true);
            ((NEEImageButton) button).setVisibility(false);
            this.informationTabInActive.setVisibility(true);
            this.informationTabActive.setVisibility(false);

            this.isInformationScreenVisible = false;
            this.container.layoutInterfaceSlots();
            this.container.layoutAllPlayerSlots();
        }));

        // Information Tab - Active
        this.informationTabActive = this.addButton(new NEEImageButton(this.isInformationScreenVisible == true, guiLeft + 125, guiTop + 0, 26, 22, 22, 219, 0, GUI_BUTTONS, (button) -> {


        }));

        List<Injector> injectors = this.container.getInjectors();

        int size = injectors.size();
        for (int i = 0; i < size; i++) {

            Injector injector = injectors.get(i);
            Direction direction = injector.getFacing();

            switch (direction) {
                case NORTH: {
                    // North

                    this.activeSlotOne = addButton(new NEEImageButton(injector.isActive() == true, guiLeft + 28, guiTop + 31, 18, 18, 32, 0, 0, GUI_BUTTONS, (button) -> {
                        this.inactiveSlotOne.setVisibility(true);
                        this.activeSlotOne.setVisibility(false);
                        injector.getTileEntity().setActive(false);
                    }));
                    this.inactiveSlotOne = addButton(new NEEImageButton(injector.isActive() == false, guiLeft + 28, guiTop + 31, 18, 18, 32, 19, 0, GUI_BUTTONS, (button) -> {
                        this.inactiveSlotOne.setVisibility(false);
                        this.activeSlotOne.setVisibility(true);
                        injector.getTileEntity().setActive(true);
                    }));

                    injectorSlots.put(direction, new ArrayList<NEEImageButton>() {
                        {
                            add(activeSlotOne);
                            add(inactiveSlotOne);
                        }
                    });
                }
                break;
                case SOUTH: {
                    // South
                    this.activeSlotThree = addButton(new NEEImageButton(injector.isActive() == true, guiLeft + 28, guiTop + 71, 18, 18, 32, 0, 0, GUI_BUTTONS, (button) -> {
                        this.inactiveSlotThree.setVisibility(true);
                        this.activeSlotThree.setVisibility(false);
                        injector.getTileEntity().setActive(false);
                    }));

                    this.inactiveSlotThree = addButton(new NEEImageButton(injector.isActive() == false, guiLeft + 28, guiTop + 71, 18, 18, 32, 19, 0, GUI_BUTTONS, (button) -> {
                        this.inactiveSlotThree.setVisibility(false);
                        this.activeSlotThree.setVisibility(true);
                        injector.getTileEntity().setActive(true);
                    }));

                    injectorSlots.put(direction, new ArrayList<NEEImageButton>() {
                        {
                            add(activeSlotThree);
                            add(inactiveSlotThree);
                        }
                    });
                }
                break;
                case EAST: {
                    // East
                    this.activeSlotTwo = addButton(new NEEImageButton(injector.isActive() == true, guiLeft + 48, guiTop + 51, 18, 18, 32, 0, 0, GUI_BUTTONS, (button) -> {
                        this.inactiveSlotTwo.setVisibility(true);
                        this.activeSlotTwo.setVisibility(false);
                        injector.getTileEntity().setActive(false);
                    }));

                    this.inactiveSlotTwo = addButton(new NEEImageButton(injector.isActive() == false, guiLeft + 48, guiTop + 51, 18, 18, 32, 19, 0, GUI_BUTTONS, (button) -> {
                        this.inactiveSlotTwo.setVisibility(false);
                        this.activeSlotTwo.setVisibility(true);
                        injector.getTileEntity().setActive(true);
                    }));

                    injectorSlots.put(direction, new ArrayList<NEEImageButton>() {
                        {
                            add(inactiveSlotTwo);
                            add(activeSlotTwo);
                        }
                    });
                }
                break;
                case WEST: {
                    // West
                    this.activeSlotFour = addButton(new NEEImageButton(injector.isActive() == true, guiLeft + 8, guiTop + 51, 18, 18, 32, 0, 0, GUI_BUTTONS, (button) -> {
                        this.inactiveSlotFour.setVisibility(true);
                        this.activeSlotFour.setVisibility(false);
                        injector.getTileEntity().setActive(false);
                    }));

                    this.inactiveSlotFour = addButton(new NEEImageButton(injector.isActive() == false, guiLeft + 8, guiTop + 51, 18, 18, 32, 19, 0, GUI_BUTTONS, (button) -> {
                        this.inactiveSlotFour.setVisibility(false);
                        this.activeSlotFour.setVisibility(true);
                        injector.getTileEntity().setActive(true);
                    }));

                    injectorSlots.put(direction, new ArrayList<NEEImageButton>() {
                        {
                            add(activeSlotFour);
                            add(inactiveSlotFour);
                        }
                    });
                }
                break;
                default:
                    continue;
            }
        }
    }

    private void showSlotButtons() {
        List<Injector> injectors = this.container.getInjectors();

        int size = injectors.size();
        for (int i = 0; i < size; i++) {

            Injector injector = injectors.get(i);
            Direction direction = injector.getFacing();

            if (this.injectorSlots.containsKey(direction)) {
                ArrayList<NEEImageButton> buttons = this.injectorSlots.get(direction);

                NEEImageButton activeButton = buttons.get(0);
                NEEImageButton inactiveButton = buttons.get(1);

                activeButton.setVisibility(inactiveButton.getVisibility() == true);
                inactiveButton.setVisibility(inactiveButton.getVisibility() == false);
            }
        }
    }

    private void hideSlotButtons() {
        List<Injector> injectors = this.container.getInjectors();

        int size = injectors.size();
        for (int i = 0; i < size; i++) {

            Injector injector = injectors.get(i);
            Direction direction = injector.getFacing();

            if (this.injectorSlots.containsKey(direction)) {
                ArrayList<NEEImageButton> buttons = this.injectorSlots.get(direction);

                NEEImageButton activeButton = buttons.get(0);
                NEEImageButton inactiveButton = buttons.get(1);

                activeButton.setVisibility(false);
                inactiveButton.setVisibility(false);
            }
        }
    }

    @Override
    protected void renderHoverToolTip(List<String> tooltip, int mouseX, int mouseY, float partialTicks) {
        if (this.isInformationScreenVisible == false) {
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
        }

        super.renderHoverToolTip(tooltip, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawNoShadowString(Minecraft.getInstance().fontRenderer, "Terra Reactor", 6, 5, 0xffffff);

        if (this.isInformationScreenVisible == false) {
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
    }

    protected void drawMainGuiContainerBackgroundLayer() {
        // Draw Energy Progression
        float energyPercent = this.getProgressionPercent(this.container.getEnergyStored(), this.container.getMaxEnergyStored());
        int energyProgression = Math.round(this.energySlice.getHeight() * energyPercent);
        this.energySlice.setProgression(energyProgression);
        this.drawVerticalSliceWithProgressionUp(this.energyDestination, this.energySlice);

        // Draw Reactant Progression
        float progressionPercent = 1f - this.getProgressionPercent(this.container.getBurnItemTicksLeft(), this.container.getBurnItemTotalTicks());
        int currentProgression = progressionPercent == 1 ? 0 : Math.round(this.progressionSlice.getHeight() * progressionPercent);
        this.progressionSlice.setProgression(currentProgression);
        this.drawVerticalSliceWithProgressionUp(this.progressionDestination, this.progressionSlice);

        // Draw Heat Progression
        float heatPercent = this.getProgressionPercent(this.container.getHeat(), this.container.getMaxHeat());
        int heatProgression = Math.round(this.heatSlice.getHeight() * heatPercent);

        if (heatPercent > 1) {
            heatProgression = this.heatSlice.getHeight();
        }

        this.heatSlice.setProgression(heatProgression);
        this.drawVerticalSliceWithProgressionUp(this.heatDestination, this.heatSlice);

        // Draw Acid - Draw last otherwise colors will be goofy
        if (this.container.getAcidStored() > 0) {
            FluidStack stack = this.container.getFluid();
            fillVertical(acidDestination, acidDestination.getTop(), stack.getFluid().getAttributes().getColor());
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0f);
        if (this.isInformationScreenVisible == false) {
            this.minecraft.getTextureManager().bindTexture(GUI);
        } else {
            this.minecraft.getTextureManager().bindTexture(STATS_GUI);
        }

        int i = this.guiLeft;
        int j = this.guiTop;

        // set the size of the GUI to slice off the right hand images
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        if (this.isInformationScreenVisible == false) {
            this.drawMainGuiContainerBackgroundLayer();
        } else {
            this.minecraft.getTextureManager().bindTexture(STATS_GUI);
        }
    }
}
