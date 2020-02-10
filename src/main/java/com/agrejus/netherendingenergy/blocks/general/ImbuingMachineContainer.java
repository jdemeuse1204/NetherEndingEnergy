package com.agrejus.netherendingenergy.blocks.general;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class ImbuingMachineContainer extends Container {

    private TileEntity tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;

    // Exists on both server and client
    // Has slots of inventory and their links
    public ImbuingMachineContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ModBlocks.IMBUING_MACHINE_CONTAINER, id);

        this.tileEntity = world.getTileEntity(pos);
        this.playerEntity = playerEntity;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(tileEntity.getWorld(), tileEntity.getPos()), playerEntity, ModBlocks.IMBUING_MACHINE_BLOCK);
    }
}
