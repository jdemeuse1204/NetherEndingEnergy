package com.agrejus.netherendingenergy.blocks.terra.reactor.injector;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.helpers.PotionsHelper;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Potion;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TerraReactorInjectorTile extends TileEntity {

    private LazyOptional<IItemHandler> handler = LazyOptional.of(this::createHandler);

    private static Map<Potion, Integer> potionUsages;
    private static List<Potion> allowedPotions;

    public TerraReactorInjectorTile() {
        super(ModBlocks.TERRA_REACTOR_INJECTOR_TILE);
    }

    private IItemHandler createHandler() {
        return new ItemStackHandler(1) {

            @Override
            protected void onContentsChanged(int slot) {
                BlockState state = world.getBlockState(pos);
                world.notifyBlockUpdate(pos, state, state, 3);
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == Items.DRAGON_BREATH || PotionsHelper.any(TerraReactorInjectorTile.getAllowedPotions(), stack);
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

    public static List<Potion> getAllowedPotions() {
        if (allowedPotions == null) {
            Map<Potion, Integer> usages = getPotionUsages();

            allowedPotions = new ArrayList<>(usages.keySet());
        }
        return allowedPotions;
    }

    public static Map<Potion, Integer> getPotionUsages() {
        if (potionUsages == null) {
            potionUsages = new HashMap<>();

            potionUsages.put(Potions.FIRE_RESISTANCE, 100);
            potionUsages.put(Potions.LONG_FIRE_RESISTANCE, 300);

            potionUsages.put(Potions.HEALING, 100);
            potionUsages.put(Potions.STRONG_HEALING, 300);

            potionUsages.put(Potions.REGENERATION, 50);
            potionUsages.put(Potions.LONG_REGENERATION, 100);
            potionUsages.put(Potions.STRONG_REGENERATION, 300);

            potionUsages.put(Potions.STRENGTH, 50);
            potionUsages.put(Potions.LONG_STRENGTH, 100);
            potionUsages.put(Potions.STRONG_STRENGTH, 300);

            potionUsages.put(Potions.SWIFTNESS, 50);
            potionUsages.put(Potions.LONG_SWIFTNESS, 100);
            potionUsages.put(Potions.STRONG_SWIFTNESS, 300);

            potionUsages.put(Potions.POISON, 50);
            potionUsages.put(Potions.LONG_POISON, 100);
            potionUsages.put(Potions.STRONG_POISON, 300);

            potionUsages.put(Potions.SLOWNESS, 50);
            potionUsages.put(Potions.LONG_SLOWNESS, 100);
            potionUsages.put(Potions.STRONG_SLOWNESS, 300);

            potionUsages.put(Potions.WEAKNESS, 100);
            potionUsages.put(Potions.LONG_WEAKNESS, 300);
        }

        return potionUsages;
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

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }
}
