package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationTile;
import com.agrejus.netherendingenergy.common.Ratio;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTraitConfig;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import com.agrejus.netherendingenergy.setup.config.CausticBellConfig;
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

    // Statistics
    private int yield; // mB
    private Ratio strength;
    private Ratio purity;
    private Ratio burnTimeAugmentRatio;

    // Traits
    private CausticBellTrait superiorTrait;
    private CausticBellTrait inferiorTrait;
    private CausticBellTrait recessiveTrait;

    public CausticBellTile(CausticBellTrait superiorTrait, CausticBellTrait inferiorTrait, CausticBellTrait recessiveTrait) {
        this();

        if (this.superiorTrait == null) {
            this.superiorTrait = superiorTrait;
        }

        if (this.inferiorTrait == null) {
            this.inferiorTrait = inferiorTrait;
        }

        if (this.recessiveTrait == null) {
            this.recessiveTrait = recessiveTrait;
        }

        this.yield = CausticBellTraitConfig.getDefaultYield(this.superiorTrait);
        this.strength = CausticBellTraitConfig.getDefaultStrength(this.superiorTrait);
        this.purity = CausticBellTraitConfig.getDefaultPurity(this.superiorTrait);
        this.burnTimeAugmentRatio = CausticBellTraitConfig.getDefaultBurnTimeAugment(this.superiorTrait);
    }

    public CausticBellTile() {
        super(ModBlocks.CAUSTIC_BELL_TILE);

        this.superiorTrait = null;
        this.inferiorTrait = null;
        this.recessiveTrait = null;

        // Default
        yield = 1;
        strength = new Ratio(1, 1);
        purity = new Ratio(1, 1); // for every 6 you get 5
        burnTimeAugmentRatio = new Ratio(1, 1);
    }

    public CausticBellTrait getSuperiorTrait() {
        return this.superiorTrait;
    }

    public CausticBellTrait getInferiorTrait() {
        return this.inferiorTrait;
    }

    public CausticBellTrait getRecessiveTrait() {
        return this.recessiveTrait;
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

            List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, this.getRenderBoundingBox().grow(1.5D, 1.5D, 1.5D));

            entities.forEach(w -> {
                if (isNoxious()) {
                    w.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200));
                }

            });
            counter = 20;
        }
    }

    public boolean isNoxious() {
        return this.superiorTrait == CausticBellTrait.NOXIOUS;
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);

        CompoundNBT strengthNBT = tag.getCompound("strength");
        this.strength.deserializeNBT(strengthNBT);

        CompoundNBT purityNBT = tag.getCompound("purity");
        this.purity.deserializeNBT(purityNBT);

        this.yield = tag.getInt("yield");

        CausticBellTrait[] traits = CausticBellTrait.values();
        String superiorTrait = tag.getString("superior_trait");
        String inferiorTrait = tag.getString("inferior_trait");
        String recessiveTrait = tag.getString("recessive_trait");

        for (CausticBellTrait trait : traits) {
            if (trait.getName() == superiorTrait) {
                this.superiorTrait = trait;
            }

            if (trait.getName() == inferiorTrait) {
                this.inferiorTrait = trait;
            }

            if (trait.getName() == recessiveTrait) {
                this.recessiveTrait = trait;
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        try {
            CompoundNBT strengthNBT = this.strength.serializeNBT();
            tag.put("strength", strengthNBT);

            CompoundNBT purityNBT = this.purity.serializeNBT();
            tag.put("purity", purityNBT);

            tag.putInt("yield", this.yield);

            tag.putString("superior_trait", this.superiorTrait.getName());
            tag.putString("inferior_trait", this.inferiorTrait.getName());
            tag.putString("recessive_trait", this.recessiveTrait.getName());
        } catch (Exception e) {
            if (1 == 1) {

            }
        }

        return super.write(tag);
    }
}
