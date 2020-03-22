package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.NetherEndingEnergyBlockStateProperties;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTraitConfig;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.models.MainTrunkRoot;
import com.agrejus.netherendingenergy.common.models.OffshootBud;
import com.agrejus.netherendingenergy.common.models.OffshootRoot;
import com.agrejus.netherendingenergy.common.models.RootPoint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.*;

import static com.agrejus.netherendingenergy.common.models.MainTrunkRoot.*;

public class CausticBellTile extends TileEntity implements ITickableTileEntity {

    private static ArrayList<Direction> spreadableDirections = new ArrayList<Direction>() {
        {
            add(Direction.NORTH);
            add(Direction.SOUTH);
            add(Direction.EAST);
            add(Direction.WEST);

            add(Direction.NORTH);
            add(Direction.SOUTH);
            add(Direction.EAST);
            add(Direction.WEST);

            add(Direction.NORTH);
            add(Direction.SOUTH);
            add(Direction.EAST);
            add(Direction.WEST);

            add(Direction.NORTH);
            add(Direction.SOUTH);
            add(Direction.EAST);
            add(Direction.WEST);

            add(Direction.NORTH);
            add(Direction.SOUTH);
            add(Direction.EAST);
            add(Direction.WEST);

            add(Direction.NORTH);
            add(Direction.SOUTH);
            add(Direction.EAST);
            add(Direction.WEST);

            add(Direction.NORTH);
            add(Direction.SOUTH);
            add(Direction.EAST);
            add(Direction.WEST);

            add(Direction.NORTH);
            add(Direction.SOUTH);
            add(Direction.EAST);
            add(Direction.WEST);
        }
    };
    private int spreadCounter;
    private int counter;

    // Statistics
    private float yield;
    private float strength;
    private float purity;
    private float pHLevel;
    private int stageAdvanceTimeTicks;
    private int baseTendrilLength = 6;
    private int maxTendrilLength = 32;
    private Map<Direction, MainTrunkRoot> mainTrunkRoots;

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

        this.yield = CausticBellTraitConfig.getRandomYield(this.superiorTrait);
        this.strength = CausticBellTraitConfig.getRandomStrength(this.superiorTrait);
        this.purity = CausticBellTraitConfig.getRandomPurity(this.superiorTrait);

        // add to NBT
        this.stageAdvanceTimeTicks = CausticBellTraitConfig.getStageAdvanceTimeTicks(this.superiorTrait);
        this.pHLevel = CausticBellTraitConfig.getpHLevel(this.superiorTrait);
    }

    public CausticBellTile() {
        super(ModBlocks.CAUSTIC_BELL_TILE);

        yield = 1f;
        strength = 1f;
        purity = 1f;
        mainTrunkRoots = new HashMap<>();
    }

    public void setSuperiorTrait(CausticBellTrait superiorTrait) {
        this.superiorTrait = superiorTrait;
    }

    public CausticBellTrait getSuperiorTrait() {
        return this.superiorTrait;
    }

    public void setInferiorTrait(CausticBellTrait inferiorTrait) {
        this.inferiorTrait = inferiorTrait;
    }

    public CausticBellTrait getInferiorTrait() {
        return this.inferiorTrait;
    }

    public void setRecessiveTrait(CausticBellTrait recessiveTrait) {
        this.recessiveTrait = recessiveTrait;
    }

    public CausticBellTrait getRecessiveTrait() {
        return this.recessiveTrait;
    }

    public void setYield(int yield) {
        this.yield = yield;
    }

    public float getYield() {
        return yield;
    }

    public void setStrengthRatio(float strength) {
        this.strength = strength;
    }

    public float getStrengthRatio() {
        return strength;
    }

    public void setPurityRatio(float purity) {
        this.purity = purity;
    }

    public float getPurityRatio() {
        return purity;
    }

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        if (counter == 0) {
            counter = 20;
        }

        //this.stageAdvanceTimeTicks
        if (spreadCounter >= 5) {
            spreadCounter = 0; // reset
            this.trySpread();
        }

        if (counter == 20) {
            this.tryPoisonSurroundingEntities();
        }

        --counter;
        ++spreadCounter;
    }

    private void trySpread() {

        BlockPos startingPosition = pos.down();
        BlockState startingState = world.getBlockState(startingPosition);
        Block startingBlock = startingState.getBlock();

        // Only spread if the below block is caustic dirt
        if (startingBlock == ModBlocks.CAUSTIC_DIRT_BLOCK) {

            int causticStage = startingState.get(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5);

            // is the current block saturated?
            // If not raise level
            if (causticStage < 5) {
                BlockState nextStateState = startingState.with(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5, ++causticStage);
                world.setBlockState(startingPosition, nextStateState, 3);
                return;
            }

            // get base direction to spread
            Direction startingDirection = getRandomDirection();
            this.tryCreateTendrilRoot(startingDirection, startingPosition);
        }
    }

    private OffshootRoot plotOffshootRoot(OffshootRoot offshootRoot, Direction travelingDirection) {

        for (int i = 1; i < offshootRoot.size(); i++) {
            BlockPos nextInlinePosition = offshootRoot.getNextPosition(i - 1);
            BlockPos startingPosition = offshootRoot.get(0).getPosition();
            int nextDeviation = BlockHelpers.getDeviation(nextInlinePosition, startingPosition, travelingDirection);

            if (nextDeviation < OffshootRoot.MIN_DEVIATION) {

                int nextDeviationAmount = BlockHelpers.deviate(offshootRoot.getSideOfMainTrunkDirection());
                Direction deviationDirection = offshootRoot.getSideOfMainTrunkDirection();
                // deviation is in the wrong direction
                BlockPos deviationBlockPosition = BlockHelpers.deviateBlockPosition(nextInlinePosition, nextDeviationAmount, deviationDirection);
                offshootRoot.plotPoint(deviationBlockPosition, i);
                continue;
            }

            // try and deviate
            int secondaryDeviation = NetherEndingEnergy.random.nextInt(3) - 1;

            if (secondaryDeviation != 0) {

                // Get deviation direction
                Direction deviationDirection = BlockHelpers.getDeviationDirection(secondaryDeviation, travelingDirection);
                BlockPos deviationBlockPosition = BlockHelpers.deviateBlockPosition(nextInlinePosition, secondaryDeviation, deviationDirection);

                // can we deviate?
                int deviation = BlockHelpers.getDeviation(deviationBlockPosition, startingPosition, travelingDirection);
                ;
                if (deviation <= OffshootRoot.MAX_DEVIATION && deviation >= OffshootRoot.MIN_DEVIATION) {
                    offshootRoot.plotPoint(deviationBlockPosition, i);
                    continue;
                }
            }

            // cannot deviate
            offshootRoot.plotPoint(nextInlinePosition, i);
        }

        return offshootRoot;
    }

    private MainTrunkRoot createMainTrunk(Direction startingDirection, BlockPos startingPosition) {
        int size = NetherEndingEnergy.roll(MainTrunkRoot.MIN_LENGTH, MainTrunkRoot.MAX_LENGTH);
        MainTrunkRoot result = new MainTrunkRoot(startingPosition, startingDirection, size);
        int offshootCount = NetherEndingEnergy.roll(MIN_OFFSHOOT_COUNT, MAX_OFFSHOOT_COUNT);

        // plot the root
        // Start at 1 because 0 is already plotted
        for (int i = 1; i < size; i++) {
            BlockPos nextInlinePosition = result.getNextPosition(i - 1);
            int deviation = NetherEndingEnergy.random.nextInt(3) - 1;

            if (deviation != 0) {

                // Get deviation direction
                Direction deviationDirection = BlockHelpers.getDeviationDirection(deviation, startingDirection);
                BlockPos deviationBlockPosition = BlockHelpers.deviateBlockPosition(nextInlinePosition, deviation, deviationDirection);

                // can we deviate?
                if (result.canDeviate(deviationBlockPosition)) {

                    if (offshootCount > 0 && i >= MIN_OFFSHOOT_INDEX  && i <= MAX_OFFSHOOT_INDEX && NetherEndingEnergy.roll(0, 2) == 1) {
                        // try and add a bud
                        ArrayList<Direction> perpendicularDirections = BlockHelpers.getPerpendicularDirections(startingDirection);
                        int index = NetherEndingEnergy.roll(0, 1);
                        Direction offshootBudDirection = perpendicularDirections.get(index);
                        result.plotPoint(deviationBlockPosition, i, new OffshootBud(offshootBudDirection));
                        --offshootCount;
                    } else {
                        result.plotPoint(deviationBlockPosition, i);
                    }
                    continue;
                }
            }

            if (offshootCount > 0 && i >= MIN_OFFSHOOT_INDEX  && i <= MAX_OFFSHOOT_INDEX && NetherEndingEnergy.roll(0, 2) == 1) {
                // try and add a bud
                ArrayList<Direction> perpendicularDirections = BlockHelpers.getPerpendicularDirections(startingDirection);
                int index = NetherEndingEnergy.roll(0, 1);
                Direction offshootBudDirection = perpendicularDirections.get(index);
                result.plotPoint(nextInlinePosition, i, new OffshootBud(offshootBudDirection));
                --offshootCount;
            } else {
                result.plotPoint(nextInlinePosition, i);
            }
        }

        return result;
    }

    private boolean tryAdvanceState(BlockState state, BlockPos pos) {
        int currentStage = state.get(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5);

        if (currentStage < 5) {
            int nextStage = ++currentStage;
            world.setBlockState(pos, state.with(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5, nextStage), 3);
            return true;
        }

        return false;
    }

    private void tryCreateTendrilRoot(Direction startingDirection, BlockPos startingPosition) {

        MainTrunkRoot mainTrunkRoot = this.mainTrunkRoots.get(startingDirection);

        if (mainTrunkRoot == null) {
            mainTrunkRoot = createMainTrunk(startingDirection, startingPosition);

            this.mainTrunkRoots.put(startingDirection, mainTrunkRoot);
        }

        if (mainTrunkRoot.isDead() == true) {
            // leave it?
            return;
        }

        // start tracing main trunk
        for (int i = 0; i < mainTrunkRoot.size(); i++) {
            RootPoint point = mainTrunkRoot.get(i);

            BlockPos pos = point.getPosition();
            BlockState state = world.getBlockState(pos);
            Block block = state.getBlock();

            if (isStopBlock(block)) {
                return;// Stop?
            }

            // make sure up isnt dirt

            // head down if there is air in front
            if (block == Blocks.AIR) {
                pos.offset(Direction.DOWN);
            }

            if (block != ModBlocks.CAUSTIC_DIRT_BLOCK) {
                world.setBlockState(pos, ModBlocks.CAUSTIC_DIRT_BLOCK.getDefaultState(), 3);
                return;
            }

            if (tryAdvanceState(state, pos) == true) {
                return; // Stop
            }
        }

        // create off shoot or start new trunk,
        Direction randomDirection = this.getRandomDirection();
        MainTrunkRoot root = this.mainTrunkRoots.get(randomDirection);

        for (int i = 0; i < root.size(); i++) {
            RootPoint point = root.get(i);

            if (point.hasOffshootBud() == false) {
                continue;
            }

            OffshootRoot offshootRoot = point.getOffshootRoot();

            if (offshootRoot == null) {
                int size = NetherEndingEnergy.roll(OffshootRoot.MIN_LENGTH, OffshootRoot.MAX_LENGTH);
                offshootRoot = point.addAndGetOffshoot(point.getPosition(), randomDirection, size);

                // Plot the new offshoot root
                plotOffshootRoot(offshootRoot, randomDirection);
            }

            // Trace the offshoot
            for (int j = 0; j < offshootRoot.size(); j++) {
                RootPoint offshootRootPoint = offshootRoot.get(j);

                BlockPos pos = offshootRootPoint.getPosition();
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                if (isStopBlock(block)) {
                    return;// Stop?
                }

                // head down if there is air in front
                if (block == Blocks.AIR) {
                    pos.offset(Direction.DOWN);
                }

                if (block != ModBlocks.CAUSTIC_DIRT_BLOCK) {
                    world.setBlockState(pos, ModBlocks.CAUSTIC_DIRT_BLOCK.getDefaultState(), 3);
                    return;
                }

                if (tryAdvanceState(state, pos) == true) {
                    return; // Stop
                }
            }
        }
    }

    private boolean isStopBlock(Block block) {
        return block == Blocks.OBSIDIAN;
    }

    private Direction getRandomDirection(ArrayList<Direction> directions) {
        int randomIndex = NetherEndingEnergy.roll(0, directions.size() - 1);
        return directions.get(randomIndex);
    }

    private Direction getRandomDirection() {
        return getRandomDirection(spreadableDirections);
    }

    private void tryPoisonSurroundingEntities() {
        if (isNoxious()) {
            List<LivingEntity> entities = world.getEntitiesWithinAABB(LivingEntity.class, this.getRenderBoundingBox().grow(1.5D, 1.5D, 1.5D));

            int size = entities.size();
            for (int i = 0; i < size; i++) {
                LivingEntity entity = entities.get(i);
                entity.addPotionEffect(new EffectInstance(Effects.NAUSEA, 200));
            }
        }
    }

    public boolean isNoxious() {
        return this.superiorTrait == CausticBellTrait.NOXIOUS;
    }

    @Override
    public void read(CompoundNBT tag) {
        super.read(tag);
        readNBT(tag);
    }

    private void readNBT(CompoundNBT tag) {
        this.strength = tag.getFloat("strength");
        this.purity = tag.getFloat("purity");
        this.yield = tag.getFloat("yield");
        this.pHLevel = tag.getFloat("ph_level");
        this.stageAdvanceTimeTicks = tag.getInt("stage_advance");

        String superior = tag.getString("superior");
        String inferior = tag.getString("inferior");
        String recessive = tag.getString("recessive");

        // Default Them
        this.superiorTrait = CausticBellTrait.DORMANT;
        this.inferiorTrait = CausticBellTrait.DORMANT;
        this.recessiveTrait = CausticBellTrait.DORMANT;

        for (CausticBellTrait trait : CausticBellTrait.VALUES) {
            if (superior.equals(trait.getName())) {
                this.superiorTrait = trait;
            }

            if (inferior.equals(trait.getName())) {
                this.inferiorTrait = trait;
            }

            if (recessive.equals(trait.getName())) {
                this.recessiveTrait = trait;
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        writeNBT(tag);
        return super.write(tag);
    }

    private void writeNBT(CompoundNBT tag) {
        tag.putFloat("strength", this.strength);
        tag.putFloat("purity", this.purity);
        tag.putFloat("yield", this.yield);
        tag.putFloat("ph_level", this.pHLevel);
        tag.putInt("stage_advance", this.stageAdvanceTimeTicks);

        tag.putString("superior", this.superiorTrait.getName());
        tag.putString("inferior", this.inferiorTrait.getName());
        tag.putString("recessive", this.recessiveTrait.getName());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        writeNBT(tag);
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getNbtCompound();
        readNBT(tag);
    }
}
