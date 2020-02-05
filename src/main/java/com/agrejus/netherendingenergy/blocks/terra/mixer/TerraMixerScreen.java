package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class TerraMixerScreen extends ContainerScreen<TerraMixerContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_mixer_gui.png");

    public TerraMixerScreen(TerraMixerContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    // render, drawGuiContainerForegroundLayer, drawGuiContainerBackgroundLayer is standard code

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
        int vaporAmount = container.getVapor();
        String text = String.format("Vapor of the Ordinary: %s mB", vaporAmount);
        drawString(Minecraft.getInstance().fontRenderer, text, 10, 10, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relativeX = (this.width - this.xSize) / 2;
        int relativeY = (this.height - this.ySize) / 2;
        this.blit(relativeX, relativeY, 0, 0, this.xSize, this.ySize);
    }
}
