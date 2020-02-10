package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraCollectingStationTile;
import com.agrejus.netherendingenergy.common.Ratio;
import com.agrejus.netherendingenergy.common.helpers.NBTHelpers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class CausticBellTile extends TileEntity implements ITickableTileEntity {

    private int counter;

    private int yield; // mB
    private Ratio strength;
    private Ratio purity;
    private Ratio burnTimeAugmentRatio;

    private int operationalSpeedTicks = 200; // 5 seconds

    public CausticBellTile() {
        super(ModBlocks.CAUSTIC_BELL_TILE);

        // Default
        yield = 0;
        strength =  new Ratio(1, 1);
        purity =  new Ratio(6, 5);
        burnTimeAugmentRatio = new Ratio(1,1);
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

            // Start of the operation
            BlockPos position = getPos().down();
            TileEntity tileBelow = world.getTileEntity(position);

            if (tileBelow instanceof TerraCollectingStationTile) {
/*                TerraAcidCollectorTile collector = (TerraAcidCollectorTile) tileBelow;

                boolean hasGrowthMediumChanged = this.hasGrowthMediumChanged(collector);

                Item growthMediumItem = collector.getGrowthMedium();
                Block growthMedium = Block.getBlockFromItem(growthMediumItem);

                if (growthMedium instanceof CausticImbuedSoil) {
                    CausticImbuedSoil soil = (CausticImbuedSoil) growthMedium;

                    ImbueMaterial material = soil.getImbueMaterial();

                    this.strength = getStrengthFromBlock(material);
                    this.yield = getYieldFromBlock(material);
                    this.purity = getPurityFromBlock(material);

                } else if (growthMediumItem == Items.DIRT) {
                    this.strength = 1;
                    this.yield = 1;
                    this.purity = 1;
                } else {
                    this.strength = 0;
                    this.yield = 0;
                    this.purity = 0;
                }

                if (hasGrowthMediumChanged) {
                    BlockState state = world.getBlockState(pos);
                    world.notifyBlockUpdate(pos, state, state, 3);
                    markDirty();
                }*/

                counter = 20;
            }
        }
    }

/*
    private boolean hasGrowthMediumChanged(TerraAcidCollectorTile collector) {
        Item growthMediumItem = collector.getGrowthMedium();
        Block growthMedium = Block.getBlockFromItem(growthMediumItem);

        if (growthMedium instanceof CausticImbuedSoil) {
            CausticImbuedSoil soil = (CausticImbuedSoil) growthMedium;
            ImbueMaterial material = soil.getImbueMaterial();

            return strength != getStrengthFromBlock(material) ||
                    yield != getYieldFromBlock(material) ||
                    purity != getPurityFromBlock(material);
        }

        if (growthMedium == Blocks.DIRT) {
            return strength != 1 ||
                    yield != 1 ||
                    purity != 1;
        }

        return strength != 0 ||
                yield != 0 ||
                purity != 0;
    }

    private int getPurityFromBlock(ImbueMaterial material) {

        if (purityBlockValues.containsKey(material)) {
            return purityBlockValues.get(material);
        }

        return 0;
    }

    private int getYieldFromBlock(ImbueMaterial material) {

        if (yieldBlockValues.containsKey(material)) {
            return yieldBlockValues.get(material);
        }

        return 0;
    }

    private int getStrengthFromBlock(ImbueMaterial material) {

        if (strengthBlockValues.containsKey(material)) {
            return strengthBlockValues.get(material);
        }

        return 0;
    }
*/

    @Override
    public void read(CompoundNBT tag) {

        NBTHelpers.deserializeNBT(this.strength, tag, "strength");
        NBTHelpers.deserializeNBT(this.yield , tag, "yield");
        NBTHelpers.deserializeNBT(this.purity, tag, "purity");

        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        NBTHelpers.serializeNBTAndPut(this.strength, tag, "strength");
        NBTHelpers.serializeNBTAndPut(this.yield, tag, "yield");
        NBTHelpers.serializeNBTAndPut(this.purity, tag, "purity");

        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();

        NBTHelpers.serializeNBTAndPut(this.strength, tag, "strength");
        NBTHelpers.serializeNBTAndPut(this.yield, tag, "yield");
        NBTHelpers.serializeNBTAndPut(this.purity, tag, "purity");

        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {

        CompoundNBT tag = packet.getNbtCompound();

        NBTHelpers.deserializeNBT(this.strength, tag, "strength");
        NBTHelpers.deserializeNBT(this.yield , tag, "yield");
        NBTHelpers.deserializeNBT(this.purity, tag, "purity");
    }
}
