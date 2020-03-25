package com.agrejus.netherendingenergy.common.factories;

import com.agrejus.netherendingenergy.NetherEndingEnergy;
import com.agrejus.netherendingenergy.blocks.flowers.roots.MainTrunkRootConfiguration;
import com.agrejus.netherendingenergy.blocks.flowers.roots.OffshootRootConfiguration;
import com.agrejus.netherendingenergy.common.enumeration.RootType;
import com.agrejus.netherendingenergy.common.helpers.BlockHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IRoot;
import com.agrejus.netherendingenergy.common.interfaces.IRootConfiguration;
import com.agrejus.netherendingenergy.common.interfaces.ISourceRoot;
import com.agrejus.netherendingenergy.common.models.MainTrunkRoot;
import com.agrejus.netherendingenergy.common.models.OffshootRoot;
import com.agrejus.netherendingenergy.common.models.RootBud;
import com.agrejus.netherendingenergy.common.models.RootPoint;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

public class RootFactory {

    private static IRoot create(BlockPos origin, Direction travelingDirection, RootBud bud, IRootConfiguration rootConfiguration) {
        int size = NetherEndingEnergy.roll(rootConfiguration.getMinLength(), rootConfiguration.getMaxLength());
        switch (bud.getRootType()) {
            case OFFSHOOT:
                return new OffshootRoot(origin, travelingDirection, bud.getSideOfTrunkDirection(), size);
            default:
            case MAIN_TRUNK:
                return new MainTrunkRoot(origin, travelingDirection, size);
        }
    }

    public static IRootConfiguration createConfiguration(RootType type) {
        switch (type) {
            case OFFSHOOT:
                return new OffshootRootConfiguration();
            default:
                throw new IllegalStateException("no root type found");
            case MAIN_TRUNK:
                return new MainTrunkRootConfiguration();
        }
    }

    public static RootBud createPlotAndGet(BlockPos startingPosition, Direction startingDirection, ISourceRoot sourceRoot) {

        RootBud result = new RootBud(startingDirection, startingPosition, RootType.MAIN_TRUNK, null);

        // Start with main trunk
        ArrayList<RootBud> buds = new ArrayList<RootBud>() {
            {
                add(result);
            }
        };

        for (int budIndex = 0; budIndex < buds.size(); budIndex++) {
            RootBud parentBud = buds.get(budIndex);

            // use supplier to create new trunk root?
            BlockPos origin = parentBud.getLocation();
            IRootConfiguration configuration = createConfiguration(parentBud.getRootType());
            IRoot root = create(origin, startingDirection, parentBud, configuration);
            parentBud.setRoot(root);

            int offshootCount = NetherEndingEnergy.roll(configuration.getMinOffshootCount(), configuration.getMaxOffshootCount());
            int size = root.size();

            // plot the root
            // Start at 1 because 0 is already plotted
            for (int i = 1; i < size; i++) {
                BlockPos nextInlinePosition = root.getNextPosition(i - 1);
                int currentDeviation = BlockHelpers.getDeviation(nextInlinePosition, origin, startingDirection);

                // must we deviate?
                if (currentDeviation < configuration.getMinDeviation()) {
                    Direction budGrowthDirection = parentBud.getSideOfTrunkDirection();
                    int nextDeviationAmount = BlockHelpers.deviate(budGrowthDirection);
                    BlockPos deviationBlockPosition = BlockHelpers.deviateBlockPosition(nextInlinePosition, nextDeviationAmount, budGrowthDirection);

                    // travel in the direction of the main root
                    deviationBlockPosition.offset(startingDirection);

                    // Add the bud to the list to grow it
                    RootPoint plottedRootPoint = plotAndGetRootPoint(sourceRoot, root, offshootCount, i, (size - i) == 2, deviationBlockPosition, startingPosition, startingDirection, root.getLastBudSideOfTrunk(), configuration);
                    RootBud bud = plottedRootPoint.getOffshootBud();

                    if (bud != null) {
                        buds.add(bud);
                        --offshootCount;
                    }
                    continue;
                }

                int deviation = NetherEndingEnergy.random.nextInt(3) - 1;

                // Try and deviate away from straight line
                if (deviation != 0) {

                    // Get deviation direction
                    Direction deviationDirection = BlockHelpers.getDeviationDirection(deviation, startingDirection);
                    BlockPos deviationBlockPosition = BlockHelpers.deviateBlockPosition(nextInlinePosition, deviation, deviationDirection);

                    // Make sure we can deviate
                    if (canDeviate(deviationBlockPosition, root.getTravelingDirection(), root.getOrigin(), configuration)) {

                        // Add the bud to the list to grow it
                        RootPoint plottedRootPoint = plotAndGetRootPoint(sourceRoot, root, offshootCount, i, (size - i) == 2, deviationBlockPosition, startingPosition, startingDirection, root.getLastBudSideOfTrunk(), configuration);
                        RootBud bud = plottedRootPoint.getOffshootBud();

                        if (bud != null) {
                            buds.add(bud);
                            --offshootCount;
                        }
                        continue;
                    }
                }

                // Add the bud to the list to grow it
                RootPoint plottedRootPoint = plotAndGetRootPoint(sourceRoot, root, offshootCount, i, (size - i) == 2, nextInlinePosition, startingPosition, startingDirection, root.getLastBudSideOfTrunk(), configuration);
                RootBud bud = plottedRootPoint.getOffshootBud();

                if (bud != null) {
                    buds.add(bud);
                    --offshootCount;
                }
            }
        }

        return result;
    }

    private static boolean canDeviate(BlockPos position, Direction travelingDirection, BlockPos origin, IRootConfiguration configuration) {
        int currentDeviation;
        if (travelingDirection == Direction.NORTH || travelingDirection == Direction.SOUTH) {
            currentDeviation = Math.abs(position.getX() - origin.getX());
        } else {
            currentDeviation = Math.abs(position.getZ() - origin.getZ());
        }

        return currentDeviation >= configuration.getMinDeviation() && currentDeviation <= configuration.getMaxDeviation();
    }

    private static RootPoint plotAndGetRootPoint(ISourceRoot sourceRoot, IRoot root, int offshootCount, int index, boolean isTwoIndicesAwayFromEnd, BlockPos plotPosition, BlockPos startingPosition, Direction mainTravelingDirection, Direction lastBudGrowthDirection, IRootConfiguration configuration) {
        RootBud bud = null;

        // no growth beyond 32 blocks
        if (offshootCount > 0 && BlockHelpers.getDistance(plotPosition, startingPosition) < sourceRoot.getMaxSpread()) {
            bud = getOffshootRootBud(offshootCount, index, plotPosition, mainTravelingDirection, lastBudGrowthDirection, isTwoIndicesAwayFromEnd, configuration);
        }

        // Add the bud to the list to grow it
        RootPoint plottedRootPoint = root.plotAndGetPoint(plotPosition, index);

        if (bud != null) {
            plottedRootPoint.setOffshootBud(bud);
        }

        return plottedRootPoint;
    }

    private static RootBud getOffshootRootBud(int offshootCount, int index, BlockPos location, Direction travelingDirection, Direction lastBudDirection, boolean isTwoIndicesAwayFromEnd, IRootConfiguration rootConfiguration) {
        if (offshootCount > 0 && index >= rootConfiguration.getMinOffshootGrowthIndex() && index <= rootConfiguration.getMaxOffshootGrowthIndex()) {

            // try and add a bud
            boolean shouldAddBud = NetherEndingEnergy.rollBoolean();

            if (shouldAddBud == false && offshootCount >= 1 && isTwoIndicesAwayFromEnd) {
                shouldAddBud = true;
            }

            if (shouldAddBud) {
                return new RootBud(travelingDirection, location, rootConfiguration.getChildRootType(), lastBudDirection.getOpposite());
            }
        }
        return null;
    }
}
