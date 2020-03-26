package com.agrejus.netherendingenergy.blocks.terra.mixer;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.interfaces.IVaporStorage;
import com.agrejus.netherendingenergy.tools.CapabilityVapor;
import com.agrejus.netherendingenergy.tools.CustomEnergyStorage;
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
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TerraMixerTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private int counter;

    private final int efficency = 65;
    private LazyOptional<IEnergyStorage> energy = LazyOptional.of(this::createEnergy);

    private IEnergyStorage createEnergy() {
        return new CustomEnergyStorage(1000000, 0, 20000);
    }

/*    private final IAnimationStateMachine asm;
    private final LazyOptional<IAnimationStateMachine> asmCap;*/

    public TerraMixerTile() {
        super(ModBlocks.TERRA_MIXER_TILE);

/*        if (FMLEnvironment.dist == Dist.CLIENT) {
            asm = ModelLoaderRegistry.loadASM(new ResourceLocation(NetherEndingEnergy.MODID, "asms/block/terra_mixer.json"), ImmutableMap.of());
            asmCap = LazyOptional.of(() -> asm);
        } else {
            asm = null;
            asmCap = LazyOptional.empty();
        }*/
    }

/*    @Override
    public boolean hasFastRenderer() {
        return true;
    }*/

    @Override
    public void tick() {

    }

    @Override
    public void read(CompoundNBT tag) {

        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {


        return super.write(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

/*        if (cap == CapabilityAnimation.ANIMATION_CAPABILITY) {
            return asmCap.cast();
            //return CapabilityAnimation.ANIMATION_CAPABILITY.orEmpty(cap, asmCap);
        }*/

        if (cap == CapabilityEnergy.ENERGY && side != null && side == Direction.DOWN) {
            return energy.cast();
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
