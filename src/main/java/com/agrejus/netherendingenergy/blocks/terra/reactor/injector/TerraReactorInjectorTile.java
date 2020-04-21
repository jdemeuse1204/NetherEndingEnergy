package com.agrejus.netherendingenergy.blocks.terra.reactor.injector;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.handlers.NonExtractingItemUsageStackHandler;
import com.agrejus.netherendingenergy.common.helpers.PotionsHelper;
import com.agrejus.netherendingenergy.common.reactor.ReactorBaseConfig;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class TerraReactorInjectorTile extends TileEntity {

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

    public TerraReactorInjectorTile() {
        super(ModBlocks.TERRA_REACTOR_INJECTOR_TILE);
    }

    private IItemHandler createHandler() {
        return new NonExtractingItemUsageStackHandler() {

            @Override
            protected void onContentsChanged(int slot) {
                BlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                markDirty();
            }

            @Override
            public int onGetUsagesForSlotWhenSet(int slot, @Nonnull ItemStack stack) {

                if (stack.isEmpty()) {
                    return 0;
                }

                Item item = stack.getItem();
                int usages = 0;

                if (item instanceof PotionItem) {
                    usages = ReactorBaseConfig.INSTANCE.getUsagesForPotion(stack);
                } else {
                    usages = ReactorBaseConfig.INSTANCE.getUsagesForItem(item);
                }

                return usages;
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {

                if (stack.isEmpty()) {
                    return false;
                }

                Item item = stack.getItem();

                if (item instanceof PotionItem) {
                    Potion potion = PotionUtils.getPotionFromItem(stack);
                    return ReactorBaseConfig.INSTANCE.canUsePotion(potion);
                }

                return ReactorBaseConfig.INSTANCE.canUseItem(item);
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

                // Burnable Items Only
                if (isItemValid(slot, stack) == false) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public void read(CompoundNBT tag) {
        CompoundNBT invTag = tag.getCompound("inv");

        handler.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        // when block is placed?
        handler.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("inv", compound);
        });

        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();

        handler.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("inv", compound);
        });

        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        CompoundNBT nbt = packet.getNbtCompound();

        CompoundNBT invTag = nbt.getCompound("inv");

        handler.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(invTag));
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }
}
