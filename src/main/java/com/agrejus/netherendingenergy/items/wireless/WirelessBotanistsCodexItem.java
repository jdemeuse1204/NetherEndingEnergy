package com.agrejus.netherendingenergy.items.wireless;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.RegistryNames;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellTile;
import com.agrejus.netherendingenergy.common.enumeration.TransferMode;
import com.agrejus.netherendingenergy.common.interfaces.ILinkableTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class WirelessBotanistsCodexItem extends Item {

    public WirelessBotanistsCodexItem() {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(NetherEndingEnergy.setup.itemGroup));
        setRegistryName(RegistryNames.WIRELESS_BOTANISTS_CODEX);
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

        if (tileEntity == null || (tileEntity instanceof CausticBellTile) == false) {
            return ActionResultType.FAIL;
        }

        CausticBellTile bell = (CausticBellTile)tileEntity;

        bell.identifyInferiorTrait();
        bell.identifySuperiorTrait();
        bell.identifyRecessiveTrait();

        player.sendStatusMessage(new StringTextComponent("Superior Trait: " + bell.getSuperiorTrait().getName()), false);
        player.sendStatusMessage(new StringTextComponent("Inferior Trait: " + bell.getInferiorTrait().getName()), false);
        player.sendStatusMessage(new StringTextComponent("Recessive Trait: " + bell.getRecessiveTrait().getName()), false);
        return ActionResultType.PASS;
    }
}