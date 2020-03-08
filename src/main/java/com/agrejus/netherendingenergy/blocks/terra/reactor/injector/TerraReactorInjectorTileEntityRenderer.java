package com.agrejus.netherendingenergy.blocks.terra.reactor.injector;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock;
import com.agrejus.netherendingenergy.common.handlers.NonExtractingItemUsageStackHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.CapabilityItemHandler;

public class TerraReactorInjectorTileEntityRenderer extends TileEntityRenderer<TerraReactorInjectorTile> {

    @Override
    public void render(TerraReactorInjectorTile tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {

        GlStateManager.pushMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.color4f(1, 1, 1, 1);
        GlStateManager.disableBlend();

        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {

            NonExtractingItemUsageStackHandler handler = (NonExtractingItemUsageStackHandler)w;
            ItemStack stack = handler.getStackInSlot(0);
            BlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
            //|| state.get(TerraReactorCoreBlock.FORMED) == TerraReactorPartIndex.UNFORMED
            if (stack.getItem() == Items.AIR || stack.getCount() == 0 ) {
                return;
            }

            long time = System.currentTimeMillis();
            float angle = (time / 100) % 360;
            GlStateManager.translatef((float) x + .5f, (float) y + .5f, (float) z + .5f);
            GlStateManager.rotatef(angle, 0f, 1f, 0f);
            GlStateManager.scalef(.5f, .5f, .5f);

            bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(stack, tileEntity.getWorld(), null);
            itemRenderer.renderItem(stack, ibakedmodel);
        });

        GlStateManager.popMatrix();
    }
}
