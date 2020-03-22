package com.agrejus.netherendingenergy.blocks.general.botanistscodex;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.Ratio;
import com.agrejus.netherendingenergy.common.attributes.CausticBellAttributes;
import com.agrejus.netherendingenergy.common.container.InventoryContainerBase;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BotanistsCodexContainer extends InventoryContainerBase<BotanistsCodexTile> {
    private IIntArray tracking;

    // Exists on both server and client
    // Has slots of inventory and their links
    public BotanistsCodexContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        this(id, world, pos, playerInventory, playerEntity, new IntArray(1));
    }

    public BotanistsCodexContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity, IIntArray intArray) {
        super(ModBlocks.BOTANISTS_CODEX_CONTAINER, id, world, pos, playerInventory, playerEntity);

        // abstract furnace passes inventory in
        this.tileEntity = (BotanistsCodexTile) world.getTileEntity(pos);
        this.playerEntity = playerEntity;
        this.playerInventory = new InvWrapper(playerInventory);

        tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(w -> {
            addSlot(new SlotItemHandler(w, 0, 8, 8));
        });

        // where is the top left slot? This is the player inventory
        layoutPlayerInventorySlots(7, 84);

        tracking = intArray;

        trackIntArray(intArray);
    }

    private ItemStack getCodexItemStack() {
        return this.tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(w -> w.getStackInSlot(0)).orElse(ItemStack.EMPTY);
    }

    public CausticBellAttributes getBellAttributes() {
        ItemStack stack = this.getCodexItemStack();
        if (stack == ItemStack.EMPTY || stack.getItem() == Items.AIR) {
            return null;
        }

        CompoundNBT tag = stack.getTag();
        INBT tagMap = tag.get("BlockEntityTag");
        if (tagMap == null) {
            return null;
        }
        CompoundNBT tileEntityTag = (CompoundNBT)tagMap;

        String superior = tileEntityTag.getString("superior");
        String inferior = tileEntityTag.getString("inferior");
        String recessive = tileEntityTag.getString("recessive");
        CausticBellTrait superiorTrait = CausticBellTrait.DORMANT;
        CausticBellTrait inferiorTrait = CausticBellTrait.DORMANT;
        CausticBellTrait recessiveTrait = CausticBellTrait.DORMANT;

        for (CausticBellTrait trait : CausticBellTrait.VALUES) {
            if (superior.equals(trait.getName())) {
                superiorTrait = trait;
            }

            if (inferior.equals(trait.getName())) {
                inferiorTrait = trait;
            }

            if (recessive.equals(trait.getName())) {
                recessiveTrait = trait;
            }
        }

        Ratio strength = new Ratio(1,1);
        Ratio purity = new Ratio(1,1);

        CompoundNBT strengthNBT = tileEntityTag.getCompound("strength");
        strength.deserializeNBT(strengthNBT);

        CompoundNBT purityNBT = tileEntityTag.getCompound("purity");
        purity.deserializeNBT(purityNBT);

        int yield = tileEntityTag.getInt("yield");

        return new CausticBellAttributes(superiorTrait, inferiorTrait, recessiveTrait, strength, purity, new Ratio(1,1), yield);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return canInteractWith(ModBlocks.BOTANISTS_CODEX_BLOCK);
    }
}
