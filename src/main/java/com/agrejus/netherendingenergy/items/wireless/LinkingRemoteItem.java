package com.agrejus.netherendingenergy.items.wireless;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.RegistryNames;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class LinkingRemoteItem extends Item {

    private BlockPos origin;
    private BlockPos destination;

    public LinkingRemoteItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(NetherEndingEnergy.setup.itemGroup));
        setRegistryName(RegistryNames.LINKING_REMOTE);
    }

    public void setPoint(BlockPos pos, PlayerEntity player) {
        if (this.origin == null) {
            this.origin = pos;
            player.sendStatusMessage(new StringTextComponent(String.format("Origin   X: %s, Y: %s, Z: %s", pos.getX(), pos.getY(), pos.getZ())), false);
            return;
        }

        this.destination = pos;
    }

    public void clear() {
        this.origin = null;
        this.destination = null;
    }

    public void setOrigin(BlockPos origin) {
        this.origin = origin;
    }

    public void setDestination(BlockPos destination) {
        this.destination = destination;
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public BlockPos getDestination() {
        return destination;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {

        if (playerIn.isSneaking()) {
            this.clear();
            return new ActionResult<>(ActionResultType.PASS, playerIn.getHeldItem(handIn));
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
