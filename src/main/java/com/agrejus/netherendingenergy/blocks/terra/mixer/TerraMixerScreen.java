package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.screen.ContainerScreenBase;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TerraMixerScreen extends ContainerScreenBase<TerraMixerContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_mixer_gui.png");
    private static final ResourceLocation MIXER_BUTTONS = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_mixer_buttons.png");
    private boolean field_214090_m;

    public TerraMixerScreen(TerraMixerContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name, 176, 166);
    }

    @Override
    protected void init() {
        super.init();
        this.xSize = 176;
        this.ySize = 166;
        this.field_214090_m = this.width < 379;
        // 18 = height
        // 19 = width
        // _3_ = width of still button
        // _4_ = bottom of still button
        // _7_ = height of hovered button? of button when hovered
        // _6_ = top of still button

        // Mix button
        this.addButton((new ImageButton(this.guiLeft + 20, this.guiTop + 48, 31, 14, 0, 0, 14, MIXER_BUTTONS, (p_214087_1_) -> {
            // on click callback
        })));

        // Clear Tank 1
        this.addButton((new ImageButton(this.guiLeft + 135, this.guiTop + 7, 9, 9, 0, 30, 9, MIXER_BUTTONS, (p_214087_1_) -> {

        })));

        // Clear Tank 2
        this.addButton((new ImageButton(this.guiLeft + 135, this.guiTop + 45, 9, 9, 0, 30, 9, MIXER_BUTTONS, (p_214087_1_) -> {

        })));
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        // Tool Tip - Here would be the place for a custom tool tip
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
/*        this.font.drawString(this.title.getFormattedText(), 8.0F, 6.0F, 4210752);
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8.0F, (float)(this.ySize - 96 + 2), 4210752);
        container.getVapor()*/
/*        int vaporAmount = container.getVapor();
        String text = String.format("Vapor of the Ordinary: %s mB", vaporAmount);
        drawString(Minecraft.getInstance().fontRenderer, text, 10, 10, 0xffffff);*/
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;

        // set the size of the GUI to slice off the right hand images
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

/*        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relativeX = (this.width - this.xSize) / 2;
        int relativeY = (this.height - this.ySize) / 2;
        this.blit(relativeX, relativeY, 0, 0, this.xSize, this.ySize);*/
    }
}
