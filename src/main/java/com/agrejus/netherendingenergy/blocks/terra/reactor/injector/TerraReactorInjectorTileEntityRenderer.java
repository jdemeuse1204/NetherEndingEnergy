package com.agrejus.netherendingenergy.blocks.terra.reactor.injector;

import com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorPartIndex;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock;
import com.agrejus.netherendingenergy.common.handlers.NonExtractingItemUsageStackHandler;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraftforge.items.CapabilityItemHandler;

public class TerraReactorInjectorTileEntityRenderer extends TileEntityRenderer<TerraReactorInjectorTile> {

    @Override
    public void render(TerraReactorInjectorTile tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {

        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {

            NonExtractingItemUsageStackHandler handler = (NonExtractingItemUsageStackHandler) w;
            ItemStack stack = handler.getStackInSlot(0);
            BlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
            if (stack.getItem() == Items.AIR || stack.getCount() == 0 || state.get(TerraReactorCoreBlock.FORMED) == TerraReactorPartIndex.UNFORMED) {
                return;
            }

            GlStateManager.disableRescaleNormal();
            GlStateManager.enableBlend();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.pushMatrix();

            long time = System.currentTimeMillis();
            float angle = (time / 100) % 360;

            switch(state.get(BlockStateProperties.FACING)) {
                case NORTH:
                    GlStateManager.translatef((float) x + .5f, (float) y + .5f, (float) z + .8f);
                    break;
                case SOUTH:
                    GlStateManager.translatef((float) x + .5f, (float) y + .5f, (float) z + .2f);
                    break;
                case EAST:
                    GlStateManager.translatef((float) x + .2f, (float) y + .5f, (float) z + .5f);
                    break;
                case WEST:
                    GlStateManager.translatef((float) x + .8f, (float) y + .5f, (float) z + .5f);
                    break;
                default:
                    GlStateManager.translatef((float) x + .5f, (float) y + .5f, (float) z + .5f);
                    break;
            }

            GlStateManager.rotatef(angle, 0f, 1f, 0f);
            GlStateManager.scalef(.4f, .4f, .4f);

            bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(stack, tileEntity.getWorld(), null);
            itemRenderer.renderItem(stack, ibakedmodel);

            GlStateManager.popMatrix();
        });
    }
}