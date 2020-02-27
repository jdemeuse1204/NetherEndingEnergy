package com.agrejus.netherendingenergy.blocks.terra.reactor;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.blocks.base.reactor.energyport.ReactorEnergyPortBlock;
import com.agrejus.netherendingenergy.blocks.base.reactor.redstoneport.ReactorRedstonePortBlock;
import com.agrejus.netherendingenergy.blocks.terra.heatsink.TerraHeatSinkBlock;
import com.agrejus.netherendingenergy.blocks.terra.machinecasing.TerraMachineCasingBlock;
import com.agrejus.netherendingenergy.common.interfaces.IMultiBlockType;
import com.agrejus.netherendingenergy.common.multiblock.MultiBlockTools;
import com.agrejus.netherendingenergy.common.reactor.IReactorConfig;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;

import static com.agrejus.netherendingenergy.blocks.terra.reactor.TerraReactorCoreBlock.FORMED;

public class TerraReactorMultiBlock implements IMultiBlockType {

    public static TerraReactorMultiBlock INSTANCE = new TerraReactorMultiBlock();
    private @Nullable List<Class> menuAccessibleClasses;

    @Nullable
    @Override
    public BlockPos getBottomLowerLeft(World world, BlockPos pos) {
        return null;
    }

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
    public BlockPos getRedstonePortPosition(World world, BlockPos clickedBlockPos, TerraReactorPartIndex part) {
        BlockPos controllerPosition = this.getControllerPosition(clickedBlockPos, part);
        return this.getBlockFromControllerPosition(world, controllerPosition, ModBlocks.REACTOR_REDSTONE_PORT_BLOCK);
    }

    @Override
    public void unformBlock(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        System.out.println(state);
        if (state.get(FORMED) != TerraReactorPartIndex.UNFORMED) {
            world.setBlockState(pos, state.with(FORMED, TerraReactorPartIndex.UNFORMED), 3);
        }
    }

    @Override
    public void formBlock(World world, BlockPos pos, TerraReactorPartIndex part) {
        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.with(FORMED, part), 3);
    }

    @Nullable
    public BlockPos getBlockFromControllerPosition(World world, BlockPos controllerPosition, Block findBlock) {
        int startX = controllerPosition.getX();
        int startY = controllerPosition.getY();
        int startZ = controllerPosition.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);

        for (TerraReactorPartIndex part : TerraReactorPartIndex.VALUES) {

            if (part.getAllowedBlockTypes() == null || part == TerraReactorPartIndex.UNFORMED) {
                continue;// Dont care whats here
            }

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
    public boolean isValidUnformedMultiBlock(World world, BlockPos pos) {
        int startX = pos.getX();
        int startY = pos.getY();
        int startZ = pos.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);

        for (TerraReactorPartIndex part : TerraReactorPartIndex.VALUES) {

            if (part.getAllowedBlockTypes() == null || part == TerraReactorPartIndex.UNFORMED) {
                continue;// Dont care whats here
            }

            BlockPos position = start.add(part.getDx(), part.getDy(), part.getDz());
            BlockState state = world.getBlockState(position);
            Block block = state.getBlock();
            Class blockClass = block.getClass();

            if (part.getAllowedBlockTypes().contains(blockClass) == false) {
                return false;
            }

            if (block != Blocks.AIR && state.get(FORMED) != TerraReactorPartIndex.UNFORMED) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isValidFormedMultiBlock(World world, BlockPos pos, IReactorConfig config) {

        Map<Class, Integer> counts = new HashMap<Class, Integer>();
        int startX = pos.getX();
        int startY = pos.getY();
        int startZ = pos.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);

        for (TerraReactorPartIndex part : TerraReactorPartIndex.VALUES) {

            if (part.getAllowedBlockTypes() == null || part == TerraReactorPartIndex.UNFORMED) {
                continue;// Dont care whats here
            }


            BlockPos position = start.add(part.getDx(), part.getDy(), part.getDz());
            BlockState state = world.getBlockState(position);
            Block block = state.getBlock();
            Class blockClass = block.getClass();

            if (counts.containsKey(blockClass)) {
                counts.put(blockClass, 0);
            }

            int currentCount = counts.get(blockClass);
            counts.replace(blockClass, currentCount++);

            if (part.getAllowedBlockTypes().contains(blockClass) == false) {
                return false;
            }

            if (block != Blocks.AIR && state.get(FORMED) == TerraReactorPartIndex.UNFORMED) {
                return false;
            }
        }

        // check our block counts for required blocks
        Map<Class, Integer> requiredBlocks = config.getBlockCounts();
        for (Class cls : requiredBlocks.keySet()) {

            int required = requiredBlocks.get(cls);
            int count = counts.getOrDefault(cls, 0);

            if (required != count) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean tryFormMultiBlock(World world, BlockPos pos) {
        int startX = pos.getX();
        int startY = pos.getY();
        int startZ = pos.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);

        for (TerraReactorPartIndex part : TerraReactorPartIndex.VALUES) {

            List<Class> allowedTypes = part.getAllowedBlockTypes();
            if (allowedTypes == null || allowedTypes.contains(AirBlock.class) || part == TerraReactorPartIndex.UNFORMED) {
                continue;// Dont care whats here
            }

            BlockPos position = start.add(part.getDx(), part.getDy(), part.getDz());
            this.formBlock(world, position, part);
        }

        return true;
    }

    @Override
    public boolean tryUnFormMultiBlock(World world, BlockPos pos) {
        int startX = pos.getX();
        int startY = pos.getY();
        int startZ = pos.getZ();
        BlockPos start = new BlockPos(startX, startY, startZ);

        for (TerraReactorPartIndex part : TerraReactorPartIndex.VALUES) {

            List<Class> allowedTypes = part.getAllowedBlockTypes();
            if (allowedTypes == null || allowedTypes.contains(AirBlock.class) || part == TerraReactorPartIndex.UNFORMED) {
                continue;// Dont care whats here
            }

            BlockPos position = start.add(part.getDx(), part.getDy(), part.getDz());
            this.unformBlock(world, position);
        }

        return true;
    }

    @Override
    public List<Class> getMenuAccessibleBlockClasses() {

        if (this.menuAccessibleClasses != null) {
            return this.menuAccessibleClasses;
        }

        this.menuAccessibleClasses = new ArrayList<Class>();

        this.menuAccessibleClasses.add(TerraMachineCasingBlock.class);
        this.menuAccessibleClasses.add(TerraReactorCoreBlock.class);
        this.menuAccessibleClasses.add(ReactorEnergyPortBlock.class);
        this.menuAccessibleClasses.add(ReactorRedstonePortBlock.class);
        this.menuAccessibleClasses.add(TerraHeatSinkBlock.class);

        return this.menuAccessibleClasses;
    }

    @Override
    public boolean isController(Class cls) {
        return cls == TerraReactorCoreBlock.class;
    }

    @Override
    public Item getFormationItem() {
        return Items.STICK;
    }

    public void toggleMultiBlock(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        // Form or break the multiblock
        if (!world.isRemote) {
            TerraReactorPartIndex formed = state.get(FORMED);
            if (formed == TerraReactorPartIndex.UNFORMED) {
                if (MultiBlockTools.formMultiblock(TerraReactorMultiBlock.INSTANCE, world, pos, player)) {
                    player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "Terra Reactor Successfully Formed"), false);
                }
                return;
            }

            MultiBlockTools.breakMultiblock(TerraReactorMultiBlock.INSTANCE, world, pos, state.get(FORMED));
            player.sendStatusMessage(new StringTextComponent(TextFormatting.RED + "Terra Reactor Unformed"), false);
        }
    }
}
