package com.agrejus.netherendingenergy.worldgen.feature;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.flowers.CausticBellBlock;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTrait;
import com.agrejus.netherendingenergy.common.flowers.CausticBellTraitHolder;
import com.agrejus.netherendingenergy.worldgen.feature.config.CausticBellConfig;
import com.mojang.datafixers.Dynamic;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class CausticBellFeature extends Feature<CausticBellConfig> {
    public CausticBellFeature(Function<Dynamic<?>, ? extends CausticBellConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(@Nonnull IWorld world, @Nonnull ChunkGenerator<? extends GenerationSettings> generator, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull CausticBellConfig config) {

        int plantedCount = 0;
        int patchMaxCount = 4;

        for (int j = 0; j < 64; ++j) {

            if (patchMaxCount > 4) {
                break;
            }

            int x = rand.nextInt(8) - rand.nextInt(8);
            int y = rand.nextInt(4) - rand.nextInt(4);
            int z = rand.nextInt(8) - rand.nextInt(8);
            BlockPos blockpos = pos.add(x, y, z);
            CausticBellTraitHolder randomTraits = this.createRandomBellTraits(rand);
            BlockState bell = ModBlocks.CAUSTIC_BELL_BLOCK.getDefaultState().with(CausticBellBlock.SUPERIOR_TRAIT, randomTraits.SuperiorTrait).with(CausticBellBlock.INFERIOR_TRAIT, randomTraits.InferiorTrait).with(CausticBellBlock.RECESSIVE_TRAIT, randomTraits.RecessiveTrait);

            if (canPlantFlower(world, blockpos, bell)) {

                // roll to see if we can plant
                if (rand.nextInt(20) == 19) {
                    world.setBlockState(blockpos, bell, 2);
                    ++plantedCount;

                    // get adjacent block
                    List<BlockPos> adjacentLocations = getNearByPlantLocations(blockpos);

                    for (BlockPos adjacentLocation : adjacentLocations) {

                        if (canPlantFlower(world, adjacentLocation, bell) && canPlantBell(rand, patchMaxCount, plantedCount)) {

                            randomTraits = this.createRandomBellTraits(rand);
                            bell = ModBlocks.CAUSTIC_BELL_BLOCK.getStateContainer().getBaseState().with(CausticBellBlock.SUPERIOR_TRAIT, randomTraits.SuperiorTrait).with(CausticBellBlock.INFERIOR_TRAIT, randomTraits.InferiorTrait).with(CausticBellBlock.RECESSIVE_TRAIT, randomTraits.RecessiveTrait);
                            world.setBlockState(blockpos, bell, 2);
                            ++plantedCount;
                        }

                        if (plantedCount >= patchMaxCount) {
                            break;
                        }
                    }
                }
            }
        }

        return plantedCount > 0;
    }

    public static CausticBellTraitHolder createRandomBellTraits(Random random) {
        // Superior Roll
        int superior = NetherEndingEnergy.roll(random, 1, 1000);
        CausticBellTrait superiorTrait = CausticBellTrait.getTrait(superior);

        // Inferior Roll
        int inferior = NetherEndingEnergy.roll(random, 1, 1000);
        CausticBellTrait inferiorTrait = CausticBellTrait.getTrait(inferior);

        // Recessive Roll
        int recessive = NetherEndingEnergy.roll(random, 1, 1000);
        CausticBellTrait recessiveTrait = CausticBellTrait.getTrait(recessive);

        return new CausticBellTraitHolder(superiorTrait, inferiorTrait, recessiveTrait);
    }

    private List<BlockPos> getNearByPlantLocations(BlockPos blockpos) {
        ArrayList<BlockPos> result = new ArrayList<BlockPos>();

        int originX = blockpos.getX();
        int originY = blockpos.getY();
        int originZ = blockpos.getZ();

        // Row 1
        result.add(new BlockPos(originX - 2, originY + 2, originZ));
        result.add(new BlockPos(originX - 1, originY + 2, originZ));
        result.add(new BlockPos(originX, originY + 2, originZ));
        result.add(new BlockPos(originX + 1, originY + 2, originZ));
        result.add(new BlockPos(originX + 2, originY + 2, originZ));

        // Row 2
        result.add(new BlockPos(originX - 2, originY + 1, originZ));
        result.add(new BlockPos(originX - 1, originY + 1, originZ));
        result.add(new BlockPos(originX, originY + 1, originZ));
        result.add(new BlockPos(originX + 1, originY + 1, originZ));
        result.add(new BlockPos(originX + 2, originY + 1, originZ));

        // Row 3
        result.add(new BlockPos(originX - 2, originY, originZ));
        result.add(new BlockPos(originX - 1, originY, originZ));
        result.add(new BlockPos(originX + 1, originY, originZ));
        result.add(new BlockPos(originX + 2, originY, originZ));

        // Row 4
        result.add(new BlockPos(originX - 2, originY - 1, originZ));
        result.add(new BlockPos(originX - 1, originY - 1, originZ));
        result.add(new BlockPos(originX, originY - 1, originZ));
        result.add(new BlockPos(originX + 1, originY - 1, originZ));
        result.add(new BlockPos(originX + 2, originY - 1, originZ));

        // Row 5
        result.add(new BlockPos(originX - 2, originY - 2, originZ));
        result.add(new BlockPos(originX - 1, originY - 2, originZ));
        result.add(new BlockPos(originX, originY - 2, originZ));
        result.add(new BlockPos(originX + 1, originY - 2, originZ));
        result.add(new BlockPos(originX + 2, originY - 2, originZ));

        return result;
    }

    private boolean canPlantFlower(@Nonnull IWorld world, @Nonnull BlockPos blockpos, @Nonnull BlockState bell) {
        return world.isAirBlock(blockpos) && blockpos.getY() < 255 && bell.isValidPosition(world, blockpos);
    }

    private boolean canPlantBell(Random rand, int maxCount, int totalPlanted) {
        int startingCount = 20;

        if (maxCount >= totalPlanted) {
            return false;
        }

        return rand.nextInt(startingCount - (totalPlanted * 3)) == 0;
    }
}