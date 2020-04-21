package com.agrejus.netherendingenergy.blocks.general.botanistscodex;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.common.attributes.CausticBellAttributes;
import com.agrejus.netherendingenergy.common.screen.ContainerScreenBase;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BotanistsCodexScreen extends ContainerScreenBase<BotanistsCodexContainer> {

    private ResourceLocation GUI = new ResourceLocation(NetherEndingEnergy.MODID, "textures/gui/botanists_codex_gui.png");

    public BotanistsCodexScreen(BotanistsCodexContainer container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name, 176, 176);
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
        drawString(Minecraft.getInstance().fontRenderer, "Botanists Codex", 6, -10, 0xffffff);

        CausticBellAttributes attributes = this.container.getBellAttributes();

        if (attributes != null) {
            drawNoShadowString(Minecraft.getInstance().fontRenderer, String.format("Superior Trait: %s", attributes.getSuperiorTrait().getName()), 6, 28, this.guiDefaultFontColor);
            drawNoShadowString(Minecraft.getInstance().fontRenderer, String.format("Inferior Trait: %s", attributes.getInferiorTrait().getName()), 6, 38, this.guiDefaultFontColor);
            drawNoShadowString(Minecraft.getInstance().fontRenderer, String.format("Recessive Trait: %s", attributes.getRecessiveTrait().getName()), 6, 48, this.guiDefaultFontColor);
            drawNoShadowString(Minecraft.getInstance().fontRenderer, String.format("Yield: %s", attributes.getYield()), 6, 58, this.guiDefaultFontColor);
            drawNoShadowString(Minecraft.getInstance().fontRenderer, String.format("Strength: %s", attributes.getStrength()), 60, 58, this.guiDefaultFontColor);
            drawNoShadowString(Minecraft.getInstance().fontRenderer, String.format("Purity: %s", attributes.getPurity()), 6, 68, this.guiDefaultFontColor);
            drawNoShadowString(Minecraft.getInstance().fontRenderer, String.format("Burn Augment: %s", attributes.getBurnTimeAugmentRatio()), 60, 68, this.guiDefaultFontColor);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI);
        int i = this.guiLeft;
        int j = this.guiTop;

        // set the size of the GUI to slice off the right hand images
        this.blit(i, j, 0, 0, this.xSize, this.ySize);


    }
}
