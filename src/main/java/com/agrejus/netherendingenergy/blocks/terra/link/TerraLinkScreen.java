package com.agrejus.netherendingenergy.blocks.terra.link;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.blocks.general.botanistscodex.BotanistsCodexContainer;
import com.agrejus.netherendingenergy.client.gui.screen.RedstoneActivatableScreen;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CRenameItemPacket;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class TerraLinkScreen extends RedstoneActivatableScreen<TerraLinkContainer> implements IContainerListener {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_mixer_gui.png");
    private TextFieldWidget nameField;

    public TerraLinkScreen(TerraLinkContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn, 176, 176);
    }

    @Override
    protected void init() {
        super.init();
        this.xSize = 176;
        this.ySize = 184;

        TerraLinkTile link = this.container.getLink();

        if (link == null) {
            // No Links
            return;
        }

        BlockPos[] links = link.getLinks();

        for (int i = 0; i < links.length; i++) {
            this.addButton((new ImageButton(this.guiLeft + (this.xSize / 2) - 0, this.guiTop + (i * 10), 9, 9, 0, 30, 0, GUI_BUTTONS, (button) -> {

            })));
        }

        int lvt_1_1_ = (this.width - this.xSize) / 2;
        int lvt_2_1_ = (this.height - this.ySize) / 2;
        this.nameField = new TextFieldWidget(this.font, lvt_1_1_ + 62, lvt_2_1_ + 24, 103, 12, I18n.format("container.repair", new Object[0]));
        this.nameField.setCanLoseFocus(false);
        this.nameField.changeFocus(true);
        this.nameField.setTextColor(-1);
        this.nameField.setDisabledTextColour(-1);
        this.nameField.setEnableBackgroundDrawing(false);
        this.nameField.setMaxStringLength(35);
        this.nameField.func_212954_a(this::onUpdateName);
        this.children.add(this.nameField);
        ((TerraLinkContainer)this.container).addListener(this);
        this.setFocusedDefault(this.nameField);
    }

    private void onUpdateName(String p_214075_1_) {
        if (!p_214075_1_.isEmpty()) {
            String lvt_2_1_ = p_214075_1_;
            Slot lvt_3_1_ = ((TerraLinkContainer)this.container).getSlot(0);
            if (lvt_3_1_ != null && lvt_3_1_.getHasStack() && !lvt_3_1_.getStack().hasDisplayName() && p_214075_1_.equals(lvt_3_1_.getStack().getDisplayName().getString())) {
                lvt_2_1_ = "";
            }

           // ((TerraLinkContainer)this.container).updateItemName(lvt_2_1_);
            //this.minecraft.player.connection.sendPacket(new CRenameItemPacket(lvt_2_1_));
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        this.renderHoveredToolTip(mouseX, mouseY);

        List<String> tooltip = new ArrayList<>();


        if (!tooltip.isEmpty()) {
            this.renderTooltip(tooltip, mouseX, mouseY);
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawNoShadowString(Minecraft.getInstance().fontRenderer, "Terra Link", 6, 5, 0xffffff);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;


    }

    @Override
    public void sendAllContents(Container container, NonNullList<ItemStack> nonNullList) {
        this.sendSlotContents(container, 0, container.getSlot(0).getStack());
    }

    @Override
    public void sendSlotContents(Container container, int i, ItemStack itemStack) {

    }

    @Override
    public void sendWindowProperty(Container container, int i, int i1) {

    }
}
