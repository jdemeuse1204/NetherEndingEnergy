package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.interfaces.IVaporStorage;
import com.agrejus.netherendingenergy.tools.CapabilityVapor;
import com.agrejus.netherendingenergy.tools.CustomVaporStorage;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.animation.TimeValues;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLEnvironment;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TerraMixerTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private final int spatialModifier = 2;// change by 2 every 16 blocks above sea level
    private final int blockRatio = 16;
    private LazyOptional<IVaporStorage> vaporStorage = LazyOptional.of(this::createVapor);
    private int counter;

    private final int maxVaporCapacity = 4000;
    private final int maxVaporTransfer = 100;
    private final int efficency = 65;

    private final IAnimationStateMachine asm;
    private final LazyOptional<IAnimationStateMachine> asmCap;

    public TerraMixerTile() {
        super(ModBlocks.TERRA_MIXER_TILE);

        if (FMLEnvironment.dist == Dist.CLIENT) {
            asm = ModelLoaderRegistry.loadASM(new ResourceLocation(NetherEndingEnergy.MODID, "asms/block/terra_mixer.json"), ImmutableMap.of());
            asmCap = LazyOptional.of(() -> asm);
        } else {
            asm = null;
            asmCap = LazyOptional.empty();
        }
    }

    private IVaporStorage createVapor() {
        return new CustomVaporStorage(maxVaporCapacity, maxVaporTransfer);
    }

    @Override
    public boolean hasFastRenderer() {
        return true;
    }

    @Override
    public void tick() {

/*        // Block only works in the overworld
        if (world.isRemote || world.dimension.getType() != DimensionType.OVERWORLD) {
            return;
        }


        // (Dimension Height - Block Height) / 16) * spatial modifier
        int modifier = ((world.dimension.getActualHeight() - block.getY()) / blockRatio) * spatialModifier;

        System.out.println(this.block);

        if (counter > 0) {
            //System.out.println(surroundingCausticBells.size());
            counter--;
            if (counter <= 0) {
                // going from 1 to 0, will reset next, do operation here

                // Collect Gas here
                // get block positions.  Only check every 20 ticks for performance reasons

            }
            markDirty();
        }

        if (counter <= 0) {
            // Start of the operation

            counter = 20;
        }*/
    }

    @Override
    public void read(CompoundNBT tag) {
        // When block is broken?


        CompoundNBT energyTag = tag.getCompound("vapor");
        // Save energy when block is broken
        vaporStorage.ifPresent(w -> ((INBTSerializable<CompoundNBT>) w).deserializeNBT(energyTag));
        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        // Write energy when block is placed
        vaporStorage.ifPresent(w -> {
            CompoundNBT compound = ((INBTSerializable<CompoundNBT>) w).serializeNBT();
            tag.put("vapor", compound);
        });
        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == CapabilityVapor.VAPOR) {
            return vaporStorage.cast();
        }

        if (cap == CapabilityAnimation.ANIMATION_CAPABILITY) {
            return asmCap.cast();
            //return CapabilityAnimation.ANIMATION_CAPABILITY.orEmpty(cap, asmCap);
        }

        return super.getCapability(cap, side);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(getType().getRegistryName().getPath());
    }

    @Nullable
    @Override
    public Container createMenu(int worldId, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new TerraMixerContainer(worldId, world, pos, playerInventory, playerEntity);
    }
}
