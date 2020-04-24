package com.agrejus.netherendingenergy.items.wireless;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.common.enumeration.TransferMode;
import com.agrejus.netherendingenergy.common.interfaces.ILinkableTile;
import com.agrejus.netherendingenergy.items.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class LinkingRemoteItem extends Item {

    private ILinkableTile source;

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

        if (tileEntity == null || (tileEntity instanceof ILinkableTile) == false) {
            return ActionResultType.FAIL;
        }

        ILinkableTile linkableTileEntity = (ILinkableTile) tileEntity;

        if (hand == Hand.OFF_HAND) {
            linkableTileEntity.clearLinks();
            linkableTileEntity.setLinkMode(TransferMode.NONE);
            linkableTileEntity.updateTile();
            player.sendStatusMessage(new StringTextComponent("Module link cleared"), false);
            return ActionResultType.FAIL;
        }

        if (this.source == null) {
            this.source = linkableTileEntity;

            if (this.source.totalLinks() >= this.source.maxAllowedLinks()) {
                player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Source block is already linked"), false);
                return ActionResultType.FAIL;
            }

            player.sendStatusMessage(new StringTextComponent(String.format("Source   X: %s, Y: %s, Z: %s", position.getX(), position.getY(), position.getZ())), false);
            return ActionResultType.PASS;
        }

        // Make sure modules are the same
        if (linkableTileEntity.getClass().equals(this.source.getClass()) == false) {
            player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + String.format("Cannot link two different types", position.getX(), position.getY(), position.getZ())), false);
            return ActionResultType.FAIL;
        }

        ILinkableTile destination = linkableTileEntity;

        BlockPos sourcePosition = this.source.getPos();
        if (position.equals(sourcePosition)) {
            player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + String.format("Cannot link to itself", position.getX(), position.getY(), position.getZ())), false);
            return ActionResultType.FAIL;
        }

        if (destination.totalLinks() > 0) {
            player.sendStatusMessage(new StringTextComponent(TextFormatting.YELLOW + "Destination block is already linked"), false);
            return ActionResultType.FAIL;
        }

        if (this.source.hasLink(destination.getPos())) {
            player.sendStatusMessage(new StringTextComponent(TextFormatting.YELLOW + "Destination block is already linked to source"), false);
            return ActionResultType.FAIL;
        }

        source.addLink(destination.getPos());
        source.setLinkMode(TransferMode.SEND);
        source.updateTile();

        destination.addLink(source.getPos());
        destination.setLinkMode(TransferMode.RECEIVE);
        destination.updateTile();

        this.source = null;

        player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Link Successful"), false);
        return ActionResultType.PASS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        if (worldIn.isRemote) {
            return new ActionResult<>(ActionResultType.FAIL, playerIn.getHeldItem(handIn));
        }

        if (playerIn.isSneaking() && playerIn.getHeldItemMainhand().getItem() == ModItems.LINKING_REMOTE) {
            this.clear();
            playerIn.sendStatusMessage(new StringTextComponent("Linking Cleared"), false);
            return new ActionResult<>(ActionResultType.FAIL, playerIn.getHeldItem(handIn));
        }

        return new ActionResult<>(ActionResultType.FAIL, playerIn.getHeldItem(handIn));
    }
}
