package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.NetherEndingEnergyBlockStateProperties;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.common.factories.RootFactory;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTraitConfig;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import com.agrejus.netherendingenergy.common.models.RootBud;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Direction, RootBud> mainTrunkRoots;

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

        // absorbing dirt/stone/wood, bring traits closer to 0
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
        if (spreadCounter >= 1) {
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

            // put down root
            world.setBlockState(startingPosition, ModBlocks.CAUSTIC_ROOTS_BLOCK.getDefaultState(), 3);

            // set the caustic roots variable so we can detect that is has roots, so we can't break and replace on other roots
            return;
        }

        // Don't spread if broken and replaced on roots
        if (startingBlock == ModBlocks.CAUSTIC_ROOTS_BLOCK) {

            Direction startingDirection = getRandomDirection();

            if (tryAdvanceState(startingState, startingPosition, startingDirection) == false) {
                // get base direction to spread
                this.spread(startingDirection, startingPosition);
            }
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

    private void absorbAboveBlocks(BlockPos pos, Direction travelingDirection) {

        ArrayList<Direction> perpendicularDirections = BlockHelpers.getPerpendicularDirections(travelingDirection);
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>() {
            {
                add(pos.offset(Direction.UP));
                add(pos.offset(Direction.UP).offset(Direction.UP));
                add(pos.offset(perpendicularDirections.get(0)).offset(Direction.UP));
                add(pos.offset(perpendicularDirections.get(1)).offset(Direction.UP));
            }
        };

        // Account for perpendicular blocks

        int size = positions.size();
        for (int i = 0; i < size; i++) {
            BlockPos blockPosition = positions.get(i);
            Block blockToEat = world.getBlockState(blockPosition).getBlock();
            Block belowBlock = world.getBlockState(blockPosition.offset(Direction.DOWN)).getBlock();

            if (isImpenetrable(blockToEat) == false && blockToEat != ModBlocks.CAUSTIC_BELL_BLOCK &&
                    (belowBlock == ModBlocks.CAUSTIC_ROOTS_BLOCK || belowBlock == ModBlocks.CAUSTIC_DIRT_BLOCK || belowBlock == Blocks.AIR)) {
                world.setBlockState(blockPosition, Blocks.AIR.getDefaultState(), 3);
            }
        }
    }

    private boolean tryAdvanceState(BlockState state, BlockPos pos, Direction travelingDirection) {
        if (state.has(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5) == false) {
            return false;
        }

        int currentStage = state.get(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5);

        if (currentStage < 5) {
            int nextStage = ++currentStage;

            if (nextStage == 5) {

                // eat Above Blocks
                absorbAboveBlocks(pos, travelingDirection);

                // turn blocks around it to caustic dirt
                ArrayList<BlockPos> spreadableDirections = spreadableSurroundingPositions(pos);
                int size = spreadableDirections.size();
                for (int i = 0; i < size; i++) {
                    BlockPos spreadablePosition = spreadableDirections.get(i);
                    BlockState blockToOvertakeState = world.getBlockState(spreadablePosition);
                    Block blockToOvertake = blockToOvertakeState.getBlock();

                    if (isImpenetrable(blockToOvertake) == false && blockToOvertake != Blocks.AIR && blockToOvertake != ModBlocks.CAUSTIC_ROOTS_BLOCK) {

                        float hardness = blockToOvertakeState.getBlockHardness(world, spreadablePosition);

                        if (hardness <= .8F) {
                            // .8f = sandstone
                            world.setBlockState(spreadablePosition, ModBlocks.CAUSTIC_DIRT_BLOCK.getDefaultState(), 3);
                        }
                    }
                }
            }

            world.setBlockState(pos, state.with(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5, nextStage), 3);
            return true;
        }

        return false;
    }

    private void spread(Direction startingDirection, BlockPos startingPosition) {

        RootBud mainBud = this.mainTrunkRoots.get(startingDirection);

        if (mainBud == null) {
            // Create the full root tree
            mainBud = RootFactory.createPlotAndGet(startingPosition, startingDirection);

            this.mainTrunkRoots.put(startingDirection, mainBud);
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

                if (tryAdvanceState(state, pos, startingDirection) == true) {
                    return; // Stop
                }

                // Roots only spread through caustic dirt
                if (isImpenetrable(block)) {
                    return;
                }

                // burrowing
                if (block != ModBlocks.CAUSTIC_DIRT_BLOCK && block != ModBlocks.CAUSTIC_ROOTS_BLOCK) {
                    world.setBlockState(pos, ModBlocks.CAUSTIC_ROOTS_BLOCK.getDefaultState(), 3);
                    return;
                }

                // account for up and down terrain
                if (block == ModBlocks.CAUSTIC_DIRT_BLOCK) {
                    if (block != ModBlocks.CAUSTIC_ROOTS_BLOCK) {
                        world.setBlockState(pos, ModBlocks.CAUSTIC_ROOTS_BLOCK.getDefaultState(), 3);
                        return;
                    }
                }
            }

            // Add any buds to the list.  Add here because we are done traversing root
            buds.addAll(root.getBuds());
        }
    }

    private boolean isImpenetrable(Block block) {
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
