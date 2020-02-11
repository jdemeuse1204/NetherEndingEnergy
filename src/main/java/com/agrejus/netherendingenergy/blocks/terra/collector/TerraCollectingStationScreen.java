package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.helpers.ClientUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TerraCollectingStationScreen extends ContainerScreen<TerraCollectingStationContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_collecting_station_gui.png");

    public TerraCollectingStationScreen(TerraCollectingStationContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        // Tool Tip - Here would be the place for a custom tool tip
        this.renderHoveredToolTip(mouseX, mouseY);

        List<String> tooltip = new ArrayList<>();
        if (mouseX > guiLeft + 151 && mouseX < guiLeft + 165 && mouseY > guiTop + 12 && mouseY < guiTop + 71){
            int amount = container.getOutputFluidAmount();
            tooltip.add("Output:");
            tooltip.add(String.format("%s mB", amount));
        }

        if (mouseX > guiLeft + 119 && mouseX < guiLeft + 136 && mouseY > guiTop + 12 && mouseY < guiTop + 71){
            int amount = container.getInputFluidAmount();
            tooltip.add("Input:");
            tooltip.add(String.format("%s mB", amount));
        }

        if (!tooltip.isEmpty()) {
            this.renderTooltip(tooltip, mouseX, mouseY);
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        drawString(Minecraft.getInstance().fontRenderer, "Collecting Station", 6, 6, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;

        // set the size of the GUI to slice off the right hand images
        this.blit(i, j, 0, 0, this.xSize, this.ySize);

        int l = this.container.getProcessProgressionScaled();
        // 176,14 is where the image is from
        // 16 is the height of the image
        // 24 is the width, comes from getCookProgressionScaled
        // 79 is the left location where the image should be
        // 34 is the top location where the image should be
        this.blit(i + 78, j + 39, 176, 0, l + 1, 16);

        int outputTankAmount = (int) (59 * (this.container.getOutputFluidAmount() / (float) this.container.getOutputTankCapacity()));
        int inputTankAmount = (int) (59 * (this.container.getInputFluidAmount() / (float) this.container.getInputTankCapacity()));

        int bottom = guiTop + 71;
        int outputHeight = guiTop + 12 + (59 - outputTankAmount);
        int outputLeft = guiLeft + 151;
        int outputRight = guiLeft + 168;

        // fill output
        fill(outputLeft, outputHeight, outputRight, bottom, 0xffe68f00);

        int inputHeight = guiTop + 12 + (59 - inputTankAmount);
        int inputLeft = guiLeft + 119;
        int inputRight = guiLeft + 136;

        // fill input
        fill(inputLeft, inputHeight, inputRight, bottom, 0xffb300e6);
    }
}
