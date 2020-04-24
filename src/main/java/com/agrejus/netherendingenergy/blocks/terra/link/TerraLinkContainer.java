package com.agrejus.netherendingenergy.blocks.terra.link;

import com.agrejus.netherendingenergy.blocks.ModBlocks;
import com.agrejus.netherendingenergy.client.gui.container.RedstoneActivatableContainer;
import com.agrejus.netherendingenergy.common.enumeration.RedstoneActivationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class TerraLinkContainer extends RedstoneActivatableContainer<TerraLinkTile> {

    private TerraLinkTile tileEntity;
    private PlayerEntity playerEntity;
    private IItemHandler playerInventory;
    private IIntArray tracking;
    private World world;
    private BlockPos pos;

    public TerraLinkContainer(int id, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(ModBlocks.TERRA_LINK_CONTAINER, id, world, pos, playerInventory, playerEntity);

        this.pos = pos;
        this.world = world;
        this.tileEntity = (TerraLinkTile)this.world.getTileEntity(pos);
        this.playerEntity = playerEntity;
    }

    @Override
    public RedstoneActivationType getRedstoneActivationType() {
        return null;
    }

    @Override
    public void changeRedstoneActivationType(RedstoneActivationType type) {

    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return super.canInteractWith(ModBlocks.TERRA_LINK_BLOCK);
    }

    public TerraLinkTile getLink() {
        return this.tileEntity;
    }
}
