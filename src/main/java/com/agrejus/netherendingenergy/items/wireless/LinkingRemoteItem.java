package com.agrejus.netherendingenergy.items.wireless;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.general.wireless.ModuleTileBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

public class LinkingRemoteItem extends Item {

    private TileEntity source;

    public LinkingRemoteItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(NetherEndingEnergy.setup.itemGroup));
        setRegistryName(RegistryNames.LINKING_REMOTE);
    }

    private void clear() {
        this.source = null;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();

        if (world.isRemote) {
            return ActionResultType.PASS;
        }

        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        BlockPos position = context.getPos();
        TileEntity tileEntity = context.getWorld().getTileEntity(position);

        if (tileEntity == null || (tileEntity instanceof ModuleTileBase) == false) {
            return ActionResultType.FAIL;
        }

        if (hand == Hand.OFF_HAND) {
            ((ModuleTileBase) tileEntity).setLinkedBlockPosition(null);
            ((ModuleTileBase) tileEntity).setSource(false);
            ((ModuleTileBase) tileEntity).updateBlock();
            player.sendStatusMessage(new StringTextComponent("Module link cleared"), false);
            return ActionResultType.FAIL;
        }

        if (this.source == null) {
            this.source = tileEntity;

            if (((ModuleTileBase) this.source).getLinkedBlockPosition() != null) {
                player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Source block is already linked"), false);
                return ActionResultType.FAIL;
            }

            player.sendStatusMessage(new StringTextComponent(String.format("Source   X: %s, Y: %s, Z: %s", position.getX(), position.getY(), position.getZ())), false);
            return ActionResultType.PASS;
        }

        if (tileEntity.getClass().equals(this.source.getClass()) == false) {
            player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + String.format("Cannot link two different types", position.getX(), position.getY(), position.getZ())), false);
            return ActionResultType.FAIL;
        }

        ModuleTileBase source = (ModuleTileBase) this.source;
        ModuleTileBase destination = (ModuleTileBase) tileEntity;

        if (position.equals(source.getPos())) {
            player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + String.format("Cannot link to itself", position.getX(), position.getY(), position.getZ())), false);
            return ActionResultType.FAIL;
        }

        if (destination.getLinkedBlockPosition() != null) {
            player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Destination block is already linked"), false);
            return ActionResultType.FAIL;
        }

        source.setLinkedBlockPosition(destination.getPos());
        source.setSource(true);
        source.updateBlock();

        destination.setLinkedBlockPosition(source.getPos());
        destination.setSource(false);
        destination.updateBlock();

        this.source = null;

        player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Link Successful"), false);
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        if (worldIn.isRemote) {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }

        if (playerIn.isSneaking()) {
            this.clear();
            playerIn.sendStatusMessage(new StringTextComponent("Linking Cleared"), false);
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
