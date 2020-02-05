package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.Capabilities;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.soil.CausticImbuedSoil;
import com.agrejus.netherendingenergy.blocks.soil.ImbueMaterial;
import com.agrejus.netherendingenergy.blocks.terra.collector.TerraAcidCollectorTile;
import com.agrejus.netherendingenergy.common.tank.NEEFluidTank;
import com.agrejus.netherendingenergy.tools.GrowthMediumValues;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class CausticBellTile extends TileEntity implements ITickableTileEntity {

    private int strength;
    private int yield;
    private int purity;

    private int counter;

    // MOVE THESE INTO COLLECTOR?
    // WHY IS COLLECTOR TE NULL?
    private HashMap<ImbueMaterial, Integer> yieldBlockValues = new HashMap<ImbueMaterial, Integer>() {{
        // Overworld = 30
        put(ImbueMaterial.DIAMOND, 1);
        put(ImbueMaterial.LAPIS, 4);
        put(ImbueMaterial.EMERALD, 1);
        put(ImbueMaterial.CRYSTALIZED_VITROL, 1);

        // Nether = 45
        put(ImbueMaterial.NETHER_BRICK, 4);
        put(ImbueMaterial.NETHER_QUARTZ, 3);
        put(ImbueMaterial.GLOWSTONE, 8);
        put(ImbueMaterial.OILY_HEART, 7);

        // End = 70
        put(ImbueMaterial.PURPUR, 13);
        put(ImbueMaterial.SHULKER_BLOCK, 16);
        put(ImbueMaterial.CHOROUS_FLOWER, 13);
        put(ImbueMaterial.ENIGMATIC_GROWTH, 16);
    }};

    private HashMap<ImbueMaterial, Integer> strengthBlockValues = new HashMap<ImbueMaterial, Integer>() {{
        // Overworld = 30
        put(ImbueMaterial.DIAMOND, 1);
        put(ImbueMaterial.LAPIS, 2);
        put(ImbueMaterial.EMERALD, 1);
        put(ImbueMaterial.CRYSTALIZED_VITROL, 1);

        // Nether = 45
        put(ImbueMaterial.NETHER_BRICK, 4);
        put(ImbueMaterial.NETHER_QUARTZ, 3);
        put(ImbueMaterial.GLOWSTONE, 8);
        put(ImbueMaterial.OILY_HEART, 7);

        // End = 70
        put(ImbueMaterial.PURPUR, 13);
        put(ImbueMaterial.SHULKER_BLOCK, 16);
        put(ImbueMaterial.CHOROUS_FLOWER, 13);
        put(ImbueMaterial.ENIGMATIC_GROWTH, 16);
    }};

    private HashMap<ImbueMaterial, Integer> purityBlockValues = new HashMap<ImbueMaterial, Integer>() {{
        // Overworld = 30
        put(ImbueMaterial.DIAMOND, 1);
        put(ImbueMaterial.LAPIS, 2);
        put(ImbueMaterial.EMERALD, 1);
        put(ImbueMaterial.CRYSTALIZED_VITROL, 1);

        // Nether = 45
        put(ImbueMaterial.NETHER_BRICK, 4);
        put(ImbueMaterial.NETHER_QUARTZ, 3);
        put(ImbueMaterial.GLOWSTONE, 8);
        put(ImbueMaterial.OILY_HEART, 7);

        // End = 70
        put(ImbueMaterial.PURPUR, 13);
        put(ImbueMaterial.SHULKER_BLOCK, 16);
        put(ImbueMaterial.CHOROUS_FLOWER, 13);
        put(ImbueMaterial.ENIGMATIC_GROWTH, 16);
    }};

    public CausticBellTile() {
        super(ModBlocks.CAUSTIC_BELL_TILE);
    }

    public int getStrength() {
        return strength;
    }

    public int getYield() {
        return yield;
    }

    public int getPurity() {
        return purity;
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

            if (tileBelow instanceof TerraAcidCollectorTile) {
                TerraAcidCollectorTile collector = (TerraAcidCollectorTile) tileBelow;

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
                }

                counter = 20;
            }
        }
    }

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

    @Override
    public void read(CompoundNBT tag) {

        this.strength = tag.getInt("strength");
        this.yield = tag.getInt("yield");
        this.purity = tag.getInt("purity");

        super.read(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {

        tag.putInt("strength", this.strength);

        tag.putInt("yield", this.yield);

        tag.putInt("purity", this.purity);

        return super.write(tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();

        tag.putInt("strength", this.strength);

        tag.putInt("yield", this.yield);

        tag.putInt("purity", this.purity);

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

        this.strength = nbt.getInt("strength");
        this.yield = nbt.getInt("yield");
        this.purity = nbt.getInt("purity");
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {

        if (cap == Capabilities.GROWTH_MEDIUM_CAPABILITY) {
            return LazyOptional.of(() -> (T) new GrowthMediumValues(this.strength, this.yield, this.purity));
        }

        return super.getCapability(cap, side);
    }
}
