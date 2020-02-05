package com.agrejus.netherendingenergy.blocks.terra.collector;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.helpers.ClientUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.util.ArrayList;
import java.util.List;

public class TerraAcidCollectorScreen extends ContainerScreen<TerraAcidCollectorContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/terra_acid_collector_gui.png");
    private TerraAcidCollectorTile tile;

    public TerraAcidCollectorScreen(TerraAcidCollectorContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.tile = (TerraAcidCollectorTile)container.getTileEntity();
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
        int acidOutputAmount = container.getTankAmount(TerraAcidCollectorTile.OUTPUT_TANK_NAME);

        drawString(Minecraft.getInstance().fontRenderer, "Collecting Station", 6, 6, 0xffffff);

        String outputText = String.format("Output: %s mB", acidOutputAmount);
        drawString(Minecraft.getInstance().fontRenderer, outputText, 6, 16, 0xffffff);

        int acidInputAmount = container.getTankAmount(TerraAcidCollectorTile.INPUT_TANK_NAME);
        String inputText = String.format("Input: %s mB", acidInputAmount);
        drawString(Minecraft.getInstance().fontRenderer, inputText, 6, 24, 0xffffff);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int relativeX = (this.width - this.xSize) / 2;
        int relativeY = (this.height - this.ySize) / 2;

        this.blit(relativeX, relativeY, 0, 0, this.xSize, this.ySize);

        int outputTankAmount = (int) (59 * (tile.getOutputTank().getFluidAmount() / (float) tile.getOutputTank().getCapacity()));
        int inputTankAmount = (int) (59 * (tile.getInputTank().getFluidAmount() / (float) tile.getInputTank().getCapacity()));

        // Power
        int bottom = guiTop + 71;
        int outputHeight = guiTop + 12 + (59 - outputTankAmount);
        int outputLeft = guiLeft + 151;
        int outputRight = guiLeft + 168;

        // fill output
        fill(outputLeft, outputHeight, outputRight, bottom,  0xffe68f00);

        int inputHeight = guiTop + 12 + (59 - inputTankAmount);
        int inputLeft = guiLeft + 119;
        int inputRight = guiLeft + 136;

        // fill input
        fill(inputLeft, inputHeight, inputRight, bottom,  0xffb300e6);

        //B300E6
/*        StringTextComponent s = new StringTextComponent("FLUID");
        List<ITextComponent> t = new ArrayList<ITextComponent>();
        t.add(s);*/

        // Fluid
        //ClientUtils.handleGuiTank(tile.getOutputTank(), guiLeft + 112, guiTop + 21, 16, 47, 177, 31, 20, 51, mouseX, mouseY, "textures/gui/terra_acid_collector_gui.png", null);
    }
}
