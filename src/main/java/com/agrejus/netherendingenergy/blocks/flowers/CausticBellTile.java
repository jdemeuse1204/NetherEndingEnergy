package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationTile;
import com.agrejus.netherendingenergy.common.Ratio;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IEntityReader;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.List;

public class CausticBellTile extends TileEntity implements ITickableTileEntity {

    private int counter;

    private int yield; // mB
    private Ratio strength;
    private Ratio purity;
    private Ratio burnTimeAugmentRatio;

    public CausticBellTile() {
        super(ModBlocks.CAUSTIC_BELL_TILE);

        // Default
        yield = 5;
        strength = new Ratio(1, 1);
        purity = new Ratio(5, 6); // for every 6 you get 5
        burnTimeAugmentRatio = new Ratio(1, 1);
    }

    public int getYield() {
        return yield;
    }

    public Ratio getStrengthRatio() {
        return strength;
    }

    public Ratio getPurityRatio() {
        return purity;
    }

    public Ratio getBurnTimeAugmentRatio() {
        return burnTimeAugmentRatio;
    }

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        if (counter > 0) {
            counter--;
        }

        if (counter <= 0) {

            List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, this.getRenderBoundingBox().grow(1.5D,1.5D,1.5D));

            entities.forEach(w -> {
                if (isNoxious()) {
                    w.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200));
                }

            });
            counter = 20;
        }
    }

    public boolean isNoxious() {
        return this.getBlockState().get(CausticBellBlock.DOMINANT_TRAIT) == CausticBellTrait.NOXIOUS;
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);

        CompoundNBT strengthNBT = tag.getCompound("strength");
        this.strength.deserializeNBT(strengthNBT);

        CompoundNBT purityNBT = tag.getCompound("purity");
        this.purity.deserializeNBT(purityNBT);

        this.yield = tag.getInt("yield");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);

        CompoundNBT strengthNBT = this.strength.serializeNBT();
        tag.put("strength", strengthNBT);

        CompoundNBT purityNBT = this.purity.serializeNBT();
        tag.put("purity", purityNBT);

        tag.putInt("yield", this.yield);

        return tag;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();

        return write(tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {

        CompoundNBT tag = packet.getNbtCompound();

        read(tag);
    }
}
