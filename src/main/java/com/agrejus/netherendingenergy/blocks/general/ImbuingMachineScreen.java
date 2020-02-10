package com.agrejus.netherendingenergy.blocks.general;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class ImbuingMachineScreen extends ContainerScreen<ImbuingMachineContainer>  {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/imbuing_machine_gui.png");

    public ImbuingMachineScreen(ImbuingMachineContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        // Tool Tip - Here would be the place for a custom tool tip
        this.renderHoveredToolTip(mouseX, mouseY);

        this.drawEnergyBar(1000);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        drawString(Minecraft.getInstance().fontRenderer, "Test", 6, 15, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relativeX = (this.width - this.xSize) / 2;
        int relativeY = (this.height - this.ySize) / 2;
        this.blit(relativeX, relativeY, 0, 0, this.xSize, this.ySize);
    }

    private void drawEnergyBar(int energy) {

        //drawRect(guiLeft + 10, guiTop + 5, guiLeft + 112, guiTop + 15, 0xff555555);
        int percentage = energy * 100 / 10000;
        for (int i = 0 ; i < percentage ; i++) {

            vLine(guiLeft + 10 + 1 + i, guiTop + 5, guiTop + 14, i % 2 == 0 ? 0xffff0000 : 0xff000000);
        }
    }
}
