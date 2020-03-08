package com.agrejus.netherendingenergy.blocks.terra.reactor;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreTile;
import com.agrejus.netherendingenergy.common.helpers.RedstoneHelpers;
import com.agrejus.netherendingenergy.common.interfaces.IReactorMultiBlockType;
import com.agrejus.netherendingenergy.common.models.BlockInformation;
import com.agrejus.netherendingenergy.common.models.TopLeftPos;
import com.agrejus.netherendingenergy.common.multiblock.MultiBlockTools;
import com.agrejus.netherendingenergy.common.reactor.IReactorConfig;
import com.agrejus.netherendingenergy.common.reactor.ReactorSlotType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IWorld;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.core.TerraReactorCoreBlock.FORMED;
import static net.minecraft.state.properties.BlockStateProperties.POWERED;
import static net.minecraft.state.properties.BlockStateProperties.POWER_0_15;

public class TerraReactorReactorMultiBlock implements IReactorMultiBlockType {

    public static TerraReactorReactorMultiBlock INSTANCE = new TerraReactorReactorMultiBlock();
    private static List<Block> menuAccessibleClasses;
    private static Map<ReactorSlotType, TopLeftPos> slotLocations;

    @Override
    public BlockPos getControllerPosition(BlockPos clickedBlockPos, TerraReactorPartIndex part) {

        if (part == TerraReactorPartIndex.P_0_0_0) {
            return clickedBlockPos;
        }

        int x = clickedBlockPos.getX();
        int y = clickedBlockPos.getY();
        int z = clickedBlockPos.getZ();

        return new BlockPos(x + -part.getDx(), y + -part.getDy(), z + -part.getDz());
    }

    @Override
    public BlockPos getRedstonePortPosition(IWorld world, BlockPos clickedBlockPos, TerraReactorPartIndex part, IReactorConfig config) {
        BlockPos controllerPosition = this.getControllerPosition(clickedBlockPos, part);
        return this.getBlockFromControllerPosition(world, controllerPosition, ModBlocks.REACTOR_REDSTONE_PORT_BLOCK, config);
    }

    @Override
    public void unformBlock(IWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);

        // the block we break will be air at this point, lets make sure the property we are trying
        // to set exists in case its air
        if (state.has(FORMED) && state.get(FORMED) != TerraReactorPartIndex.UNFORMED) {

            if (state.getBlock() == ModBlocks.TERRA_REACTOR_REDSTONE_OUTPUT_PORT_BLOCK) {
                BlockState newState = state.with(FORMED, TerraReactorPartIndex.UNFORMED).with(POWERED, Boolean.valueOf(false)).with(POWER_0_15, 0);
                world.setBlockState(pos, newState, 3);
                return;
            }

            world.setBlockState(pos, state.with(FORMED, TerraReactorPartIndex.UNFORMED), 3);
        }
    }

    @Override
    public void formBlock(IWorld world, BlockPos pos, BlockState state, TerraReactorPartIndex part, BlockPos core) {

        // need to set the powered 0-15 on the reactor output block so the correct
        // powered image renders to the client
        if (state.getBlock() == ModBlocks.TERRA_REACTOR_REDSTONE_OUTPUT_PORT_BLOCK) {
            TerraReactorCoreTile coreTile = (TerraReactorCoreTile) world.getTileEntity(core);

            if (coreTile != null) {
                coreTile.getCapability(CapabilityEnergy.ENERGY).ifPresent(w -> {
                    int current = w.getEnergyStored();
                    int max = w.getMaxEnergyStored();
                    int signalStrength = RedstoneHelpers.computeSignalStrength(current, max);
                    boolean powered = signalStrength > 0;
                    BlockState newState = state.with(BlockStateProperties.POWER_0_15, signalStrength).with(FORMED, part).with(POWERED, Boolean.valueOf(powered));
                    world.setBlockState(pos, newState, 3);
                    world.notifyNeighbors(pos, newState.getBlock());
                });
                return;
            }
        }

        world.setBlockState(pos, state.with(FORMED, part), 3);
    }

    @Override
    public Map<ReactorSlotType, TopLeftPos> getSlotLocations() {
        if (slotLocations == null) {
            slotLocations = new HashMap<>();
            slotLocations.put(ReactorSlotType.Main, new TopLeftPos(46, 78));
            slotLocations.put(ReactorSlotType.Injector1, new TopLeftPos(46, 48));
            slotLocations.put(ReactorSlotType.Injector2, new TopLeftPos(16, 78));
            slotLocations.put(ReactorSlotType.Injector3, new TopLeftPos(46, 108));
            slotLocations.put(ReactorSlotType.Injector4, new TopLeftPos(77, 78));
        }

        return slotLocations;
    }

    @Nullable
    public BlockInformation getBlockFromControllerPosition(IWorld world, BlockPos controllerPosition, TerraReactorPartIndex part) {
        int startX = controllerPosition.getX();
        int startY = controllerPosition.getY();
        int startZ = controllerPosition.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);

        BlockPos position = start.add(part.getDx(), part.getDy(), part.getDz());
        BlockState state = world.getBlockState(position);

        return new BlockInformation(position, state.getBlock(), state);
    }

    @Nullable
    public BlockPos getBlockFromControllerPosition(IWorld world, BlockPos controllerPosition, Block findBlock, IReactorConfig config) {
        int startX = controllerPosition.getX();
        int startY = controllerPosition.getY();
        int startZ = controllerPosition.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);

        for (TerraReactorPartIndex part : config.getParts().keySet()) {

            BlockPos position = start.add(part.getDx(), part.getDy(), part.getDz());
            BlockState state = world.getBlockState(position);
            Block block = state.getBlock();

            if (block == findBlock) {
                return position;
            }
        }

        return null;
    }

    @Override
    public boolean isValidUnformedMultiBlock(IWorld world, BlockPos pos, PlayerEntity player, IReactorConfig config) {
        int startX = pos.getX();
        int startY = pos.getY();
        int startZ = pos.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);
        Map<Block, Integer> counts = new HashMap<Block, Integer>();
        HashMap<TerraReactorPartIndex, List<Block>> parts = config.getParts();

        for (TerraReactorPartIndex part : parts.keySet()) {

            BlockPos position = start.add(part.getDx(), part.getDy(), part.getDz());
            BlockState state = world.getBlockState(position);
            Block block = state.getBlock();

            if (counts.containsKey(block) == false) {
                counts.put(block, 0);
            }

            int currentCount = counts.get(block);
            counts.replace(block, ++currentCount);

            List<Block> allowedBlocks = parts.get(part);

            if (allowedBlocks.contains(block) == false) {

                player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + String.format("Invalid Block at: %s, %s, %s", position.getX(), position.getY(), position.getZ())), false);
                player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + String.format("Expected: %s", getBlockNames(allowedBlocks))), false);
                player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + String.format("Actual: %s", block.getRegistryName().toString())), false);
                return false;
            }

            if (block != Blocks.AIR && state.get(FORMED) != TerraReactorPartIndex.UNFORMED) {
                return false;
            }
        }

        // check our block counts for required blocks
        Map<Block, Integer> requiredBlocks = config.getBlockCounts();
        for (Block block : requiredBlocks.keySet()) {

            int required = requiredBlocks.get(block);
            int count = counts.getOrDefault(block, 0);

            if (required != count) {
                player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + String.format("%s Count Incorrect - Expected: %s, Actual: %s", block.getRegistryName(), required, count)), false);
                return false;
            }
        }

        return true;
    }

    private String getBlockNames(List<Block> blocks) {
        List<String> names = blocks.stream().map(w -> w.getRegistryName().toString()).collect(Collectors.toList());
        return String.join(", ", names);
    }

    @Override
    public boolean isValidFormedMultiBlock(IWorld world, BlockPos pos, IReactorConfig config) {
        Map<Block, Integer> counts = new HashMap<Block, Integer>();
        int startX = pos.getX();
        int startY = pos.getY();
        int startZ = pos.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);
        HashMap<TerraReactorPartIndex, List<Block>> parts = config.getParts();

        for (TerraReactorPartIndex part : parts.keySet()) {

            BlockPos position = start.add(part.getDx(), part.getDy(), part.getDz());
            BlockState state = world.getBlockState(position);
            Block block = state.getBlock();

            if (counts.containsKey(block)) {
                counts.put(block, 0);
            }

            int currentCount = counts.get(block);
            counts.replace(block, currentCount++);

            List<Block> allowedBlocks = parts.get(part);

            if (allowedBlocks.contains(block) == false) {
                return false;
            }

            if (block != Blocks.AIR && state.get(FORMED) == TerraReactorPartIndex.UNFORMED) {
                return false;
            }
        }

        // check our block counts for required blocks
        Map<Block, Integer> requiredBlocks = config.getBlockCounts();
        for (Block block : requiredBlocks.keySet()) {

            int required = requiredBlocks.get(block);
            int count = counts.getOrDefault(block, 0);

            if (required != count) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean tryFormMultiBlock(IWorld world, BlockPos pos, IReactorConfig config) {
        int startX = pos.getX();
        int startY = pos.getY();
        int startZ = pos.getZ();
        BlockPos core = new BlockPos(startX, startY, startZ); // core is always the starting position
        HashMap<TerraReactorPartIndex, List<Block>> parts = config.getParts();

        for (TerraReactorPartIndex part : parts.keySet()) {

            List<Block> allowedBlocks = parts.get(part);
            if (allowedBlocks.contains(Blocks.AIR)) {
                continue;
            }

            BlockPos position = core.add(part.getDx(), part.getDy(), part.getDz());
            BlockState state = world.getBlockState(position);
            this.formBlock(world, position, state, part, core);
        }

        return true;
    }

    @Override
    public boolean tryUnFormMultiBlock(IWorld world, BlockPos pos, IReactorConfig config) {
        int startX = pos.getX();
        int startY = pos.getY();
        int startZ = pos.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);
        HashMap<TerraReactorPartIndex, List<Block>> parts = config.getParts();

        for (TerraReactorPartIndex part : parts.keySet()) {

            List<Block> allowedBlocks = parts.get(part);
            if (allowedBlocks.contains(Blocks.AIR) || part == TerraReactorPartIndex.UNFORMED) {
                continue;// Dont care whats here
            }

            BlockPos position = start.add(part.getDx(), part.getDy(), part.getDz());

            this.unformBlock(world, position);
        }

        return true;
    }

    @Override
    public List<Block> getMenuAccessibleBlockClasses() {

        if (this.menuAccessibleClasses != null) {
            return this.menuAccessibleClasses;
        }

        this.menuAccessibleClasses = new ArrayList<Block>();

        this.menuAccessibleClasses.add(ModBlocks.TERRA_REACTOR_CASING_BLOCK);
        this.menuAccessibleClasses.add(ModBlocks.TERRA_REACTOR_CORE_BLOCK);
        this.menuAccessibleClasses.add(ModBlocks.TERRA_REACTOR_ENERGY_PORT_BLOCK);
        this.menuAccessibleClasses.add(ModBlocks.REACTOR_REDSTONE_PORT_BLOCK);
        this.menuAccessibleClasses.add(ModBlocks.TERRA_HEAT_SINK_BLOCK);

        return this.menuAccessibleClasses;
    }

    @Override
    public boolean isController(Block block) {
        return block == ModBlocks.TERRA_REACTOR_CORE_BLOCK;
    }

    @Override
    public Item getFormationItem() {
        return Items.STICK;
    }

    public void toggleMultiBlock(IWorld world, BlockPos pos, BlockState state, PlayerEntity player) {
        // Form or break the multiblock
        if (!world.isRemote()) {
            TerraReactorPartIndex formed = state.get(FORMED);
            if (formed == TerraReactorPartIndex.UNFORMED) {
                if (MultiBlockTools.formMultiblock(TerraReactorReactorMultiBlock.INSTANCE, world, pos, player, TerraReactorConfig.INSTANCE)) {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Terra Reactor Successfully Formed"), false);
                }
                return;
            }

            MultiBlockTools.breakMultiblock(TerraReactorReactorMultiBlock.INSTANCE, world, pos, state.get(FORMED), TerraReactorConfig.INSTANCE);
            player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Terra Reactor Unformed"), false);
        }
    }
}
