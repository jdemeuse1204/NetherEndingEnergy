package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.NetherEndingEnergyBlockStateProperties;
import com.agrejus.netherendingenergy.NetherEndingEnergyConfig;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.flowers.roots.RootSystem;
import com.agrejus.netherendingenergy.common.factories.RootFactory;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTraitConfig;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import com.agrejus.netherendingenergy.common.interfaces.ISourceRoot;
import com.agrejus.netherendingenergy.common.models.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
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

public class CausticBellTile extends TileEntity implements ITickableTileEntity {

    private Map<BlockPos, AbsorbableBlock> absorbableBlocks = new HashMap<>();

    private int absorbCounter;
    private int spreadCounter;
    private int counter;

    private int terraSlurryCounter;
    private int chaoticSlurryCounter;
    private int abyssalSlurryCounter;

    // Statistics
    private NumberRange yield;
    private NumberRange strength;
    private NumberRange purity;
    private float pHLevel;
    private int spreadAdvanceTicks;
    private int absorbTicks;
    private RootSystem rootSystem;

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

        this.yield = CausticBellTraitConfig.getYield(this.superiorTrait);
        this.strength = CausticBellTraitConfig.getStrength(this.superiorTrait);
        this.purity = CausticBellTraitConfig.getPurity(this.superiorTrait);

        // add to NBT
        this.spreadAdvanceTicks = CausticBellTraitConfig.getStageAdvanceTimeTicks(this.superiorTrait);
        this.absorbTicks = CausticBellTraitConfig.getStageAdvanceTimeTicks(this.superiorTrait);
        this.pHLevel = CausticBellTraitConfig.getpHLevel(this.superiorTrait);
    }

    public CausticBellTile() {
        super(ModBlocks.CAUSTIC_BELL_TILE);

        yield = new NumberRange(.1f, .1f);
        strength = new NumberRange(.1f, .1f);
        purity = new NumberRange(.1f, .1f);
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


    public NumberRange getYield() {
        return yield;
    }

    public NumberRange getStrength() {
        return strength;
    }

    public NumberRange getPurity() {
        return purity;
    }

    private float getMaxStrength() {
        return this.strength.getMax();
    }

    private float getMaxPurity() {
        return this.purity.getMax();
    }

    private float getMaxYield() {
        return this.yield.getMax();
    }

    @Override
    public void tick() {

        if (world.isRemote) {
            return;
        }

        if (counter == 0) {
            counter = 20;
        }

        // stageAdvanceTime is also the time it takes to eat a block, better flowers = faster eating
        if (spreadCounter >= this.spreadAdvanceTicks) {
            spreadCounter = 0; // reset
            this.trySpread();
        }

        // Enqueue blocks to be absorbed
        if (this.absorbCounter >= this.absorbTicks) {
            this.absorbCounter = 0;
            this.enqueueAbsorbableBlocks();
        }

        // Dequeue blocks to be absorbed, absorb every 5 ticks?
        if (this.absorbableBlocks.size() > 0) {

            Map.Entry<BlockPos, AbsorbableBlock> entry = this.absorbableBlocks.entrySet().iterator().next();

            while (entry != null) {
                AbsorbableBlock absorbableBlock = this.absorbableBlocks.remove(entry.getKey());
                this.tryAbsorbBlock(absorbableBlock);

                if (this.absorbableBlocks.entrySet().iterator().hasNext() == false) {
                    break;
                }
                entry = this.absorbableBlocks.entrySet().iterator().next();
            }
        }

        // Poison surrounding entities
        if (counter == 20) {
            this.tryPoisonSurroundingEntities();
        }

        --this.counter;
        ++this.spreadCounter;
        ++this.absorbCounter;
    }

    private void trySpread() {

        BlockPos startingPosition = pos.down();
        BlockState startingState = world.getBlockState(startingPosition);
        Block startingBlock = startingState.getBlock();

        // Only spread if the below block is caustic dirt
        if (startingBlock == ModBlocks.CAUSTIC_DIRT_BLOCK) {

            // put down root
            world.setBlockState(startingPosition, ModBlocks.CAUSTIC_ROOTS_BLOCK.getDefaultState(), 3);
            return;
        }

        // Don't spread if broken and replaced on roots
        if (startingBlock == ModBlocks.CAUSTIC_ROOTS_BLOCK) {

            // Create the root system only when bell is on caustic dirt
            if (this.rootSystem == null) {
                this.rootSystem = new RootSystem(20, startingPosition);
            }

            Direction startingDirection = getRandomDirection();

            // Spreads down first
            if (tryAdvanceState(startingState, startingPosition) == false) {

                // Roll a random root
                int rootToSpreadTo = this.getRandomBranchingRoot();

                // get base direction to spread
                this.spread(startingDirection, rootToSpreadTo);
            }
        }
    }

    private int getRandomBranchingRoot() {

        int random = NetherEndingEnergy.roll(0, 970);

        NumberRoll[] ranges = NetherEndingEnergyConfig.CausticBell().ranges;

        for (int i = 0; i < ranges.length; i++) {
            if (ranges[i].isBetween(random)) {
                return ranges[i].getResult();
            }
        }

        return -1;
    }

    private void enqueueAbsorbableBlocks() {
        if (this.rootSystem == null) {
            return;
        }

        // this may cause lag, cycle through branches and enqueue them 1 at a time?
        Direction[] mainTrunkDirections = NetherEndingEnergyConfig.CausticBell().mainTrunkDirections;

        // traverse all branches and find blocks to absorb
        int rootSystemSize = this.rootSystem.size();
        int mainTrunkDirectionsSize = mainTrunkDirections.length;
        for (int i = 0; i < rootSystemSize; i++) {
            ISourceRoot sourceRoot = this.rootSystem.get(i);

            for (int j = 0; j < mainTrunkDirectionsSize; j++) {
                Direction rootDirection = mainTrunkDirections[j];
                RootBud mainBud = sourceRoot.getMainTrunk(rootDirection);

                if (mainBud == null) {
                    continue;
                }

                ArrayList<RootBud> buds = new ArrayList<RootBud>() {
                    {
                        add(mainBud);
                    }
                };

                for (int budIndex = 0; budIndex < buds.size(); budIndex++) {
                    IRoot root = buds.get(budIndex).getRoot();

                    if (root == null) {
                        continue;
                    }

                    int size = root.size();

                    // trace root, skip origin
                    for (int k = 1; k < size; k++) {
                        RootPoint point = root.get(k);

                        BlockPos pos = point.getPosition();
                        BlockState triggeringBlockState = world.getBlockState(pos);
                        Block triggeringBlock = triggeringBlockState.getBlock();

                        if (triggeringBlock != ModBlocks.CAUSTIC_ROOTS_BLOCK) {
                            break;
                        }

                        int stage = triggeringBlockState.get(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5);

                        if (stage != 5) {
                            break;
                        }

                        // Tick differently to absorb blocks
                        enqueueBlocksToAbsorb(pos, mainBud.getGrowthDirection());
                    }

                    // Add any buds to the list.  Add here because we are done traversing root
                    buds.addAll(root.getBuds());
                }
            }
        }
    }

    private void spread(Direction startingDirection, int randomSourceRootIndex) {

        ISourceRoot sourceRoot = this.rootSystem.get(randomSourceRootIndex);

        // make sure the way down to the position is root
        if (randomSourceRootIndex != 0) {
            ISourceRoot topSourceRoot = this.rootSystem.get(0);
            BlockPos topSourceRootOrigin = topSourceRoot.getPos();
            BlockPos sourceRootOrigin = sourceRoot.getPos();
            int currentY = topSourceRootOrigin.getY();
            int sourceY = sourceRootOrigin.getY();

            for (int y = currentY; y >= sourceY; y--) {
                BlockPos pos = new BlockPos(topSourceRootOrigin.getX(), y, topSourceRootOrigin.getZ());
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                // burrowing
                if (canBurrowThroughBlock(block)) {
                    world.setBlockState(pos, ModBlocks.CAUSTIC_ROOTS_BLOCK.getDefaultState(), 3);
                    return;
                }

                // spreading
                if (isSpreadableBlock(block)) {
                    world.setBlockState(pos, ModBlocks.CAUSTIC_ROOTS_BLOCK.getDefaultState(), 3);
                    return;
                }

                if (tryAdvanceState(state, pos)) {
                    return;
                }
            }

            // root doesn't branch
            if (sourceRoot instanceof SourceRoot) {
                randomSourceRootIndex = this.getRandomBranchingRoot();

                // we want to grow a different branch, don't waste the execution
                while (randomSourceRootIndex == 19) {
                    randomSourceRootIndex = this.getRandomBranchingRoot();
                }

                sourceRoot = this.rootSystem.get(randomSourceRootIndex);
            }
        }

        RootBud mainBud = sourceRoot.getMainTrunk(startingDirection);

        if (mainBud == null) {
            // Create the full root tree
            mainBud = RootFactory.createPlotAndGet(sourceRoot.getPos(), startingDirection, sourceRoot);

            sourceRoot.setMainTrunk(startingDirection, mainBud);
        }

        ArrayList<RootBud> buds = new ArrayList<>();
        buds.add(mainBud);

        for (int budIndex = 0; budIndex < buds.size(); budIndex++) {
            IRoot root = buds.get(budIndex).getRoot();

            if (root == null) {
                continue;
            }

            int size = root.size();

            // trace root, skip origin
            for (int i = 1; i < size; i++) {
                RootPoint point = root.get(i);

                BlockPos pos = point.getPosition();
                BlockState state = world.getBlockState(pos);
                Block block = state.getBlock();

                if (tryAdvanceState(state, pos) == true) {
                    return; // Stop
                }

                // Roots only spread through caustic dirt
                if (isImpenetrable(block)) {
                    return;
                }

                // burrowing
                if (canBurrowThroughBlock(block)) {
                    world.setBlockState(pos, ModBlocks.CAUSTIC_ROOTS_BLOCK.getDefaultState(), 3);
                    return;
                }

                // spreading
                if (isSpreadableBlock(block)) {
                    world.setBlockState(pos, ModBlocks.CAUSTIC_ROOTS_BLOCK.getDefaultState(), 3);
                    return;
                }
            }

            // Add any buds to the list.  Add here because we are done traversing root
            buds.addAll(root.getBuds());
        }
    }

    private ArrayList<BlockPos> spreadableSurroundingPositions(BlockPos pos) {
        ArrayList<BlockPos> result = new ArrayList<>();

        result.add(pos.offset(Direction.NORTH));
        result.add(pos.offset(Direction.SOUTH));
        result.add(pos.offset(Direction.EAST));
        result.add(pos.offset(Direction.WEST));

        result.add(pos.offset(Direction.NORTH).offset(Direction.EAST));
        result.add(pos.offset(Direction.EAST).offset(Direction.SOUTH));
        result.add(pos.offset(Direction.SOUTH).offset(Direction.WEST));
        result.add(pos.offset(Direction.WEST).offset(Direction.NORTH));

        return result;
    }

    private void addYield(float amount) {
        float yield = this.yield.getCurrent();
        yield += amount;

        if (this.yield.isWithinRange(yield)) {
            this.yield.setCurrent(yield);
        }
    }

    private void addPurity(float amount) {
        float yield = this.purity.getCurrent();
        yield += amount;

        if (this.purity.isWithinRange(yield)) {
            this.purity.setCurrent(yield);
        }
    }

    private void addStrength(float amount) {
        float yield = this.strength.getCurrent();
        yield += amount;

        if (this.strength.isWithinRange(yield)) {
            this.strength.setCurrent(yield);
        }
    }

    private void enqueueBlocksToAbsorb(BlockPos pos, Direction travelingDirection) {

        ArrayList<Direction> perpendicularDirections = BlockHelpers.getPerpendicularDirections(travelingDirection);
        Direction perpendicularDirectionOne = perpendicularDirections.get(0);
        Direction perpendicularDirectionTwo = perpendicularDirections.get(1);
        AbsorbableBlock[] blocksToTest = new AbsorbableBlock[]{
                new AbsorbableBlock(pos.offset(Direction.UP), pos, true),
                new AbsorbableBlock(pos.offset(Direction.UP).offset(Direction.UP), pos, true),

                new AbsorbableBlock(pos.offset(Direction.DOWN), pos, true),
                new AbsorbableBlock(pos.offset(Direction.DOWN).offset(Direction.DOWN), pos, true),

                new AbsorbableBlock(pos.offset(perpendicularDirectionOne).offset(Direction.UP), pos.offset(perpendicularDirectionOne), false),
                new AbsorbableBlock(pos.offset(perpendicularDirectionTwo).offset(Direction.UP), pos.offset(perpendicularDirectionTwo), false),

                new AbsorbableBlock(pos.offset(perpendicularDirectionOne).offset(Direction.DOWN), pos.offset(perpendicularDirectionOne), false),
                new AbsorbableBlock(pos.offset(perpendicularDirectionTwo).offset(Direction.DOWN), pos.offset(perpendicularDirectionTwo), false),

        };

        // go back and up in each direction to account for kitty corner blocks
        int size = blocksToTest.length;
        for (int i = 0; i < size; i++) {
            AbsorbableBlock absorbableBlock = blocksToTest[i];
            BlockPos position = absorbableBlock.getPos();

            // only remove once
            if (this.absorbableBlocks.containsKey(position)) {
                continue;
            }

            Block blockToAbsorb = world.getBlockState(position).getBlock();

            if (isImpenetrable(blockToAbsorb) == true) {
                continue;
            }

            Block triggeringBlock = world.getBlockState(absorbableBlock.getTriggeringPos()).getBlock();

            // If we are going to absorb the block lets make sure we can first
            if (absorbableBlock.isShouldAbsorb()) {
                boolean canAbsorb = canAbsorbAndDestroyBlock(blockToAbsorb, triggeringBlock);
                absorbableBlock.setShouldAbsorb(canAbsorb);
                absorbableBlocks.put(position, absorbableBlock);
                continue;
            }

            absorbableBlocks.put(position, absorbableBlock);
        }
    }

    private void tryAbsorbBlock(AbsorbableBlock absorbableBlock) {

        BlockPos blockPosition = absorbableBlock.getPos();
        BlockState blockToEatState = world.getBlockState(absorbableBlock.getPos());
        Block blockToEat = blockToEatState.getBlock();

        if (isImpenetrable(blockToEat) == true) {
            return;
        }

        Block triggeringBlock = world.getBlockState(absorbableBlock.getTriggeringPos()).getBlock();
        Map<Block, BellTraits> consumableBlocks = NetherEndingEnergyConfig.CausticBell().consumableBlocks;

        // check again
        if (canAbsorbAndDestroyBlock(blockToEat, triggeringBlock) == true) {
            // 0-1, block is above root
            BellTraits blockTraits = consumableBlocks.getOrDefault(blockToEat, null);
            if (blockTraits != null) {

                this.addStrength(blockTraits.getStrength());
                this.addPurity(blockTraits.getPurity());
                this.addYield(blockTraits.getYield());

                // Eat the block
                world.setBlockState(blockPosition, Blocks.AIR.getDefaultState(), 3);
                markDirty();
                return;
            }

            Map<Material, BellTraits> consumableMaterials = NetherEndingEnergyConfig.CausticBell().consumableMaterials;
            Material materialToEat = blockToEatState.getMaterial();
            BellTraits materialTraits = consumableMaterials.getOrDefault(materialToEat, null);
            if (materialTraits != null) {

                this.addStrength(materialTraits.getStrength());
                this.addPurity(materialTraits.getPurity());
                this.addYield(materialTraits.getYield());

                // Eat the block
                world.setBlockState(blockPosition, Blocks.AIR.getDefaultState(), 3);
                markDirty();
                return;
            }

            // just destroy, don't absorb
            world.setBlockState(blockPosition, Blocks.AIR.getDefaultState(), 3);
        }
    }

    private boolean tryAdvanceState(BlockState state, BlockPos pos) {
        if (state.has(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5) == false) {
            return false;
        }

        int currentStage = state.get(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5);

        if (currentStage < 5) {
            int nextStage = ++currentStage;

            if (nextStage == 5) {

                // turn blocks around it to caustic dirt
                ArrayList<BlockPos> spreadableDirections = spreadableSurroundingPositions(pos);
                int size = spreadableDirections.size();
                for (int i = 0; i < size; i++) {
                    BlockPos spreadablePosition = spreadableDirections.get(i);
                    BlockState blockToOvertakeState = world.getBlockState(spreadablePosition);
                    Block blockToOvertake = blockToOvertakeState.getBlock();

                    if (canOvertakeBlock(blockToOvertake, blockToOvertakeState, spreadablePosition) == true) {
                        changeToCausticDirt(spreadablePosition);
                    }
                }
            }

            world.setBlockState(pos, state.with(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5, nextStage), 3);
            return true;
        }

        return false;
    }

    private boolean canOvertakeBlock(Block block, BlockState blockState, BlockPos position) {
        // .8f = sandstone
        return isImpenetrable(block) == false && block != ModBlocks.CAUSTIC_ROOTS_BLOCK && blockState.getBlockHardness(world, position) <= .8f;
    }

    private void changeToCausticDirt(BlockPos pos) {
        world.setBlockState(pos, ModBlocks.CAUSTIC_DIRT_BLOCK.getDefaultState(), 3);
    }

    private boolean canAbsorbAndDestroyBlock(Block blockToAbsorb, Block triggeringBlock) {

        return isImpenetrable(blockToAbsorb) == false &&
                blockToAbsorb != ModBlocks.CAUSTIC_BELL_BLOCK &&
                blockToAbsorb != ModBlocks.CAUSTIC_ROOTS_BLOCK &&
                blockToAbsorb != ModBlocks.CAUSTIC_DIRT_BLOCK &&
                (triggeringBlock == ModBlocks.CAUSTIC_DIRT_BLOCK || triggeringBlock == ModBlocks.CAUSTIC_ROOTS_BLOCK);
    }

    private boolean isSpreadableBlock(Block block) {
        return block == ModBlocks.CAUSTIC_DIRT_BLOCK;
    }

    private boolean canBurrowThroughBlock(Block block) {
        return block != ModBlocks.CAUSTIC_DIRT_BLOCK && block != ModBlocks.CAUSTIC_ROOTS_BLOCK && block != Blocks.AIR;
    }

    private boolean isImpenetrable(Block block) {
        return block == Blocks.OBSIDIAN || block == Blocks.BEDROCK || block == Blocks.AIR;
    }

    private Direction getRandomDirection(ArrayList<Direction> directions) {
        int randomIndex = NetherEndingEnergy.roll(0, directions.size() - 1);
        return directions.get(randomIndex);
    }

    private Direction getRandomDirection() {
        return getRandomDirection(NetherEndingEnergyConfig.CausticBell().spreadableDirections);
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
        CompoundNBT strengthTag = (CompoundNBT)tag.get("strength");
        this.strength.deserializeNBT(strengthTag);

        CompoundNBT purityTag = (CompoundNBT)tag.get("purity");
        this.purity.deserializeNBT(purityTag);

        CompoundNBT yieldTag = (CompoundNBT)tag.get("yield");
        this.yield.deserializeNBT(yieldTag);

        this.pHLevel = tag.getFloat("ph_level");
        this.spreadAdvanceTicks = tag.getInt("stage_advance");

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

        if (tag.contains("root_system")) {
            CompoundNBT rootSystemNbt = (CompoundNBT) tag.get("root_system");
            this.rootSystem = new RootSystem();
            this.rootSystem.deserializeNBT(rootSystemNbt);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        writeNBT(tag);
        return super.write(tag);
    }

    private void writeNBT(CompoundNBT tag) {

        tag.put("strength", this.strength.serializeNBT());
        tag.put("purity", this.purity.serializeNBT());
        tag.put("yield", this.yield.serializeNBT());
        tag.putFloat("ph_level", this.pHLevel);
        tag.putInt("stage_advance", this.spreadAdvanceTicks);

        tag.putString("superior", this.superiorTrait.getName());
        tag.putString("inferior", this.inferiorTrait.getName());
        tag.putString("recessive", this.recessiveTrait.getName());
        if (this.rootSystem != null) {
            tag.put("root_system", this.rootSystem.serializeNBT());
        }
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
