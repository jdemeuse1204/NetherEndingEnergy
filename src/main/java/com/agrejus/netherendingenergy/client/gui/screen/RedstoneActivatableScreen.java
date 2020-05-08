package com.agrejus.netherendingenergy.client.gui.screen;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.client.gui.button.NEEImageButton;
import com.agrejus.netherendingenergy.client.gui.container.RedstoneActivatableContainer;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import com.agrejus.netherendingenergy.common.rendering.Rect;
import com.agrejus.netherendingenergy.common.screen.ContainerScreenBase;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class RedstoneActivatableScreen<T extends RedstoneActivatableContainer> extends ContainerScreenBase<T> {

    private NEEImageButton redstoneActiveWithSignal;
    private NEEImageButton redstoneActibeWithoutSignal;
    private NEEImageButton redstoneAlwaysActive;
    private Rect redstoneButtonLocation;
    protected static final ResourceLocation GUI_BUTTONS = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/gui_buttons.png");

    public RedstoneActivatableScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn, int defaultGuiScreenWidth, int defaultGuiScreenHeight) {
        super(screenContainer, inv, titleIn, defaultGuiScreenWidth, defaultGuiScreenHeight);
    }

    protected int getLeft() {
        return this.guiLeft + 178;
    }

    protected int getTop() {
        return this.guiTop + 20;
    }

    protected int getButtonHeight() {
        return 14;
    }

    protected int getButtonWidth() {
        return 14;
    }

    protected int getRedstoneActiveWithSignalSliceTop() {
        return 106;
    }

    protected int getRedstoneActiveWithoutSignalSliceTop() {
        return 136;
    }

    protected int getRedstoneAlwaysActiveSliceTop() {
        return 166;
    }

    @Override
    protected void renderHoverToolTip(List<String> tooltip, int mouseX, int mouseY, float partialTicks) {

        if (this.isMouseOver(redstoneButtonLocation, mouseX, mouseY)) {
            RedstoneActivationType redstoneActivationType = this.container.getRedstoneActivationType();

            if (redstoneActivationType != null) {
                switch (redstoneActivationType) {
                    default:
                    case ALWAYS_ACTIVE:
                        tooltip.add("Always Active");
                        break;
                    case ACTIVE_WITHOUT_SIGNAL:
                        tooltip.add("Active Without Signal");
                        break;
                    case ACTIVE_WITH_SIGNAL:
                        tooltip.add("Active With Signal");
                        break;
                }
            }
        }

        super.renderHoverToolTip(tooltip, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void init() {
        super.init();

        this.redstoneButtonLocation = createRectBasedOnGui(20, 178, 192, 34);

        // Swap between redstone signals
        RedstoneActivationType redstoneActivationType = this.container.getRedstoneActivationType();
        this.redstoneActiveWithSignal = new NEEImageButton(redstoneActivationType == RedstoneActivationType.ACTIVE_WITH_SIGNAL, this.getLeft(), this.getTop(), this.getButtonHeight(), this.getButtonWidth(), 0, this.getRedstoneActiveWithSignalSliceTop(), 0, GUI_BUTTONS, (button) -> {
            ((NEEImageButton) button).setVisibility(false);
            this.redstoneActibeWithoutSignal.setVisibility(true);
            this.container.changeRedstoneActivationType(RedstoneActivationType.ACTIVE_WITHOUT_SIGNAL);
        });
        this.redstoneActibeWithoutSignal = new NEEImageButton(redstoneActivationType == RedstoneActivationType.ACTIVE_WITHOUT_SIGNAL, this.getLeft(), this.getTop(), this.getButtonHeight(), this.getButtonWidth(), 0, this.getRedstoneActiveWithoutSignalSliceTop(), 0, GUI_BUTTONS, (button) -> {
            ((NEEImageButton) button).setVisibility(false);
            this.redstoneAlwaysActive.setVisibility(true);
            this.container.changeRedstoneActivationType(RedstoneActivationType.ALWAYS_ACTIVE);
        });
        this.redstoneAlwaysActive = new NEEImageButton(redstoneActivationType == RedstoneActivationType.ALWAYS_ACTIVE, this.getLeft(), this.getTop(), this.getButtonHeight(), this.getButtonWidth(), 0, this.getRedstoneAlwaysActiveSliceTop(), 0, GUI_BUTTONS, (button) -> {
            ((NEEImageButton) button).setVisibility(false);
            this.redstoneActiveWithSignal.setVisibility(true);
            this.container.changeRedstoneActivationType(RedstoneActivationType.ACTIVE_WITH_SIGNAL);
        });

        // Redstone Always Active
        this.addButton(this.redstoneAlwaysActive);

        // Redstone Active With Signal
        this.addButton(this.redstoneActiveWithSignal);

        // Redstone Active Without Signal
        this.addButton(this.redstoneActibeWithoutSignal);
    }
}
