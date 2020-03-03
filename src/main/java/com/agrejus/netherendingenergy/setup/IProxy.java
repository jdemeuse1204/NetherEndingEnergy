package com.agrejus.netherendingenergy.setup;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;

public interface IProxy {

    void init();

    World getClientWorld();

    PlayerEntity getClientPlayer();
}
