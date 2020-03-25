package com.agrejus.netherendingenergy.blocks.flowers;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.NetherEndingEnergyBlockStateProperties;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CausticBellTile extends TileEntity implements ITickableTileEntity {

    private static Map<Block, BellTraits> consumableBlocks = new HashMap<Block, BellTraits>() {
        {
            put(Blocks.DIAMOND_BLOCK, new BellTraits(.75f, 2.3f, 2.5f));

            put(Blocks.EMERALD_BLOCK, new BellTraits(1, 3.5f, 4f));

            put(Blocks.IRON_BLOCK, new BellTraits(0, .15f, .001f));

            put(Blocks.LAPIS_BLOCK, new BellTraits(.45f, .02f, .55f));

            put(Blocks.REDSTONE_BLOCK, new BellTraits(.15f));

            put(Blocks.PURPUR_BLOCK, new BellTraits(3.1f, 1f, 2.1f));
            put(Blocks.END_STONE, new BellTraits(.95f));
            put(Blocks.NETHER_BRICKS, new BellTraits(.1f));
            put(Blocks.NETHERRACK, new BellTraits(.001f));
            put(Blocks.SOUL_SAND, new BellTraits(-.001f, 1.9f, 1.6f));
            put(Blocks.GLOWSTONE, new BellTraits(.45f));
            put(Blocks.BONE_BLOCK, new BellTraits(-.001f, -.001f, 3f));
            put(Blocks.BRICKS, new BellTraits(.01f));
            put(Blocks.COAL_BLOCK, new BellTraits(.05f, 1.5f, -.5f));
            put(Blocks.CHORUS_FLOWER, new BellTraits(0, 1f, 0));

            put(Blocks.DRAGON_EGG, new BellTraits(10f, 5f, 1f));
            put(Blocks.NETHER_WART_BLOCK, new BellTraits(.25f));
            put(Blocks.QUARTZ_BLOCK, new BellTraits(.1f));

            // Heads
            put(Blocks.CREEPER_HEAD, new BellTraits(.6f, .2f, -.1f));
            put(Blocks.DRAGON_HEAD, new BellTraits(2f, 3f, -.5f));
            put(Blocks.PLAYER_HEAD, new BellTraits(1f, .6f, -.3f));
            put(Blocks.ZOMBIE_HEAD, new BellTraits(.8f, .4f, -.25f));
        }
    };

    private static Map<Material, BellTraits> consumableMaterials = new HashMap<Material, BellTraits>() {
        {
            put(Material.WOOD, new BellTraits(-.001f));
            put(Material.EARTH, new BellTraits(-.01f));
            put(Material.ROCK, new BellTraits(-.01f));
            put(Material.BAMBOO, new BellTraits(-.001f));
            put(Material.BAMBOO_SAPLING, new BellTraits(-.001f));
            put(Material.CLAY, new BellTraits(.02f, 0, 0f));
            put(Material.IRON, new BellTraits(0, .02f, 0f));
            put(Material.LAVA, new BellTraits(0, .03f, -.001f));
            put(Material.WATER, new BellTraits(-.001f, -.001f, .01f));
            put(Material.LEAVES, new BellTraits(-.001f));
            put(Material.FIRE, new BellTraits(0, .03f, -.001f));
            put(Material.TNT, new BellTraits(.001f, .035f, -.001f));
            put(Material.ICE, new BellTraits(-.001f, -.001f, .01f));
            put(Material.SNOW, new BellTraits(-.001f, -.001f, .01f));
            put(Material.SNOW_BLOCK, new BellTraits(-.001f, -.001f, .01f));
        }
    };
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

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public float getStrength() {
        return strength;
    }

    public void setPurity(float purity) {
        this.purity = purity;
    }

    public float getPurity() {
        return purity;
    }

    private float getMaxStrength() {
        return CausticBellTraitConfig.getMaxStrength(this.superiorTrait);
    }

    private float getMaxPurity() {
        return CausticBellTraitConfig.getMaxPurity(this.superiorTrait);
    }

    private float getMaxYield() {
        return CausticBellTraitConfig.getMaxYield(this.superiorTrait);
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
        // stageAdvanceTime is also the time it takes to eat a block, better flowers = faster eating
        if (spreadCounter >= 1) {
            spreadCounter = 0; // reset
            this.trySpread();
        }

        // eat blocks at a different rate, depends on pH level

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

            // Create the root system only when bell is on caustic dirt
            if (this.rootSystem == null) {
                this.rootSystem = new RootSystem(20, startingPosition);
            }

            // spread like a flower, grow down and then out on top, upside down xmas tree

            Direction startingDirection = getRandomDirection();

            // Spreads down first
            if (tryAdvanceState(startingState, startingPosition, startingDirection) == false) {

                // Roll a random root
                int rootToSpreadTo = this.getRandomBranchingRoot();

                // get base direction to spread
                this.spread(startingDirection, rootToSpreadTo);
            }
        }
    }

    private int getRandomBranchingRoot() {

        NumberRoll[] ranges = new NumberRoll[]{
                new NumberRoll(0, 500, 0),
                new NumberRoll(501, 750, 5),
                new NumberRoll(751, 875, 10),
                new NumberRoll(876, 938, 15),
                new NumberRoll(939, 970, 19)
        };

        int random = NetherEndingEnergy.roll(0, 970);

        for (int i = 0; i < ranges.length; i++) {
            if (ranges[i].isBetween(random)) {
                return ranges[i].getResult();
            }
        }

        return -1;
        // if we pick a root and we arent spread down there yet, try to spread down
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

                if (tryAdvanceState(state, pos, null)) {
                    return;
                }
            }

            // root doesnt branch, needed so we can grow down
            if (sourceRoot instanceof SourceRoot) {
                randomSourceRootIndex = this.getRandomBranchingRoot();

                // we want to grow a different branch, don't waste the execution
                while(randomSourceRootIndex == 19){
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

                if (tryAdvanceState(state, pos, startingDirection) == true) {
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

        result.add(pos.offset(Direction.DOWN));

        return result;
    }

    private void addYield(float amount) {
        this.yield += amount;
        float max = this.getMaxYield();

        if (this.yield < 0) {
            this.yield = 0;
        }

        if (this.yield > max) {
            this.yield = max;
        }
    }

    private void addPurity(float amount) {
        this.purity += amount;
        float max = this.getMaxPurity();

        if (this.purity < 0) {
            this.purity = 0;
        }

        if (this.purity > max) {
            this.purity = max;
        }
    }

    private void addStrength(float amount) {
        this.strength += amount;
        float max = this.getMaxStrength();

        if (this.strength < 0) {
            this.strength = 0;
        }

        if (this.strength > max) {
            this.strength = max;
        }
    }

    private void absorbBlocks(BlockPos pos, Direction travelingDirection) {

        ArrayList<Direction> perpendicularDirections = BlockHelpers.getPerpendicularDirections(travelingDirection);
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>() {
            {
                add(pos.offset(Direction.UP));
                add(pos.offset(Direction.UP).offset(Direction.UP));

                add(pos.offset(perpendicularDirections.get(0)).offset(Direction.UP));
                add(pos.offset(perpendicularDirections.get(1)).offset(Direction.UP));
            }
        };

        // go back and up in each direction to account for kitty corner blocks

        int size = positions.size();
        for (int i = 0; i < size; i++) {
            BlockPos blockPosition = positions.get(i);
            BlockState blockToEatState = world.getBlockState(blockPosition);
            Block blockToEat = blockToEatState.getBlock();
            Block offsetBlock = world.getBlockState(blockPosition.offset(Direction.DOWN)).getBlock();
            Block offsetOffsetBlock = world.getBlockState(blockPosition.offset(Direction.DOWN).offset(Direction.DOWN)).getBlock();

            if (canAbsorbBlock(blockToEat, offsetBlock, offsetOffsetBlock) == true && i <= 1) {

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
    }

    private boolean tryAdvanceState(BlockState state, BlockPos pos, Direction travelingDirection) {
        if (state.has(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5) == false) {
            return false;
        }

        int currentStage = state.get(NetherEndingEnergyBlockStateProperties.CAUSTIC_0_5);

        if (currentStage < 5) {
            int nextStage = ++currentStage;

            if (nextStage == 5) {

                // Tick differently to absorb blocks
                //absorbBlocks(pos, travelingDirection);

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

    private boolean canAbsorbBlock(Block block, Block offsetBlock, Block offsetOffsetBlock) {
        return isImpenetrable(block) == false && block != ModBlocks.CAUSTIC_BELL_BLOCK && (offsetBlock == ModBlocks.CAUSTIC_ROOTS_BLOCK || offsetBlock == ModBlocks.CAUSTIC_DIRT_BLOCK || offsetOffsetBlock == ModBlocks.CAUSTIC_ROOTS_BLOCK);
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
